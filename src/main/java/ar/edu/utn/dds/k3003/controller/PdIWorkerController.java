package ar.edu.utn.dds.k3003.controller;
import ar.edu.utn.dds.k3003.app.PdIWorker;
import ar.edu.utn.dds.k3003.clients.PdIProxy;
import ar.edu.utn.dds.k3003.clients.dtos.PDIDTO;

import ar.edu.utn.dds.k3003.model.PdI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class PdIWorkerController {
    private final PdIWorker fachada;
    @Value("urlPdI:http://localhost")
    private String urlPdI;

    private String lastUrl = null;
    private PdIProxy pdiProxy = null;

    @Autowired
    public PdIWorkerController(PdIWorker fachada) {
        this.fachada = fachada;
    }

    @PostMapping
    public ResponseEntity<String> recibirPdI (@RequestBody PDIDTO req){
        try {
            fachada.poner_a_procesar(recibirPdIDTO(req));
            return ResponseEntity.ok().body("String Procesado");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("anun")
    public ResponseEntity<String> anunciarse (@RequestBody String req){
        String urlProxy = req == null ? urlPdI : req;
        if (lastUrl != null && lastUrl.equals(urlProxy)) {
            return ResponseEntity.badRequest().body("El worker ya se anuncio a esa URL");
        }

        if (pdiProxy == null) {
            pdiProxy = new PdIProxy(new ObjectMapper(), urlProxy);
            lastUrl = urlProxy;
        }

        if (!pdiProxy.anunciarse()){
            return ResponseEntity.internalServerError().body("Fallo al anunciarse");
        }

        return ResponseEntity.ok().body("Anunciado y recibido en url: " + urlProxy);
    }

    private PdI recibirPdIDTO(PDIDTO pdiDTO) {

        return new PdI(
                pdiDTO.id(),
                pdiDTO.hechoId(),
                pdiDTO.descripcion(),
                pdiDTO.lugar(),
                pdiDTO.momento(),
                pdiDTO.urlImagen(),
                pdiDTO.textoImagen(),
                pdiDTO.etiquetas());
    }

}
