package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.clients.dtos.WorkerUrlDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PdIClient {
    @POST("/api/pdis/worker")
    Call<Void> anunciarse (@Body WorkerUrlDTO workerUrlDTO);

}
