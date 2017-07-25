package br.unifor.treinamento.agenda.service;

import java.util.List;

import br.unifor.treinamento.agenda.bean.Contato;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {

    @GET("contacts_schedule/{idAgenda}/contacts.json")
    Call<List<Contato>> getContact(@Path("idAgenda") Integer idAgenda);

    @POST("contacts.json")
    Call<Contato> createContact(@Body Contato contato);

    @PUT("contacts/{idContato}.json")
    Call<Contato> updateContact(@Path("idContato") Integer idContato, @Body Contato contato);

    @DELETE("contacts/{idContato}.json")
    Call<Contato> deleteContact(@Path("idContato") Integer idContato);


}
