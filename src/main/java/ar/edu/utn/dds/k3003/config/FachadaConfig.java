package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.app.PdIWorker;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FachadaConfig {


    @Bean
    public PdIWorker fachadaProcesadorPdI(PdIRepository repo) {
        return new PdIWorker(repo);

    }
}
