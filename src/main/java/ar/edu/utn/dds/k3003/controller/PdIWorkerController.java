package ar.edu.utn.dds.k3003.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class PdIWorkerController {


    @PostMapping
    public ResponseEntity<String> wake (){
        return ResponseEntity.ok("Despierto");
    }

}
