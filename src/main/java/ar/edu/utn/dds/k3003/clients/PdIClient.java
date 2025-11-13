package ar.edu.utn.dds.k3003.clients;

import retrofit2.Call;
import retrofit2.http.POST;

public interface PdIClient {
    @POST("/api/pdis/worker")
    Call<String> anunciarse ();

}
