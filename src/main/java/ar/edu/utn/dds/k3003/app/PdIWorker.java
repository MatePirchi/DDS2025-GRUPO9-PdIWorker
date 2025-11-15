package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.analizadores.Procesador;
import ar.edu.utn.dds.k3003.config.MetricsConfig;
import ar.edu.utn.dds.k3003.model.PdI;
import ar.edu.utn.dds.k3003.repository.InMemoryPdIRepo;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PdIWorker {
    @Getter
    private final PdIRepository pdiRepository;


    @Autowired
    private MetricsConfig metrics;
    @Autowired
    private Procesador procesador;


    protected PdIWorker() {
        this.pdiRepository = new InMemoryPdIRepo();
    }

    @Autowired
    public PdIWorker(PdIRepository pdiRepository) {
        this.pdiRepository = pdiRepository;
    }

    public void poner_a_procesar(PdI pdi){
        Timer.Sample tiempoProc = metrics.startTimer();

        procesador.procesar(pdi, this.pdiRepository);

        metrics.guardarTiempoDeProc(tiempoProc);
    }
}
