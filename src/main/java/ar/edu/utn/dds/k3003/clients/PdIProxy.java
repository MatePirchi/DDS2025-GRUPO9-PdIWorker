package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.clients.dtos.WorkerUrlDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PdIProxy {


    private final PdIClient service;

    public PdIProxy(ObjectMapper objectMapper, String baseURL) {

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(PdIClient.class);
    }

    public boolean anunciarse(String urlPropia) {
        try {

            var response = service.anunciarse(new WorkerUrlDTO(urlPropia)).execute();

            if (response.isSuccessful()) {
                System.out.println("Anunciado desde url: " + urlPropia);
                return true;
            }
            return false;
        }
        catch (Exception e){
            System.out.println("Fallo al anunciarse, error: " + e.getMessage());
            return false;
        }

    }
}
