package ar.edu.utn.dds.k3003.clients;

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

    public boolean anunciarse() {
        try {
            var response = service.anunciarse().execute();
            return response.isSuccessful();
        }
        catch (Exception e){
            System.out.println("Fallo al anunciarse, error: " + e.getMessage());
            return false;
        }

    }
}
