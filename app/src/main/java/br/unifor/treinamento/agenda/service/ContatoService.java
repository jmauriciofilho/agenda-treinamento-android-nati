package br.unifor.treinamento.agenda.service;

import android.util.Log;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import br.unifor.treinamento.agenda.bean.Contato;
import br.unifor.treinamento.agenda.event.ContatoApagadoEvent;
import br.unifor.treinamento.agenda.event.ContatoCriadoEvent;
import br.unifor.treinamento.agenda.event.ListaCriadaEvent;
import br.unifor.treinamento.agenda.event.RequestBegunEvent;
import br.unifor.treinamento.agenda.event.RequestFinishedEvent;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.RealmClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

@EBean
public class ContatoService {


    private final static int idAgenda = 8;
    private static API api;

    ContatoService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.9.222:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }

    public List<Contato> getContatos() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Contato> contatos = realm
                .where(Contato.class)
                .findAllSorted("idade", Sort.DESCENDING);


        if (contatos != null) {
            return realm.copyFromRealm(contatos);
        } else {
            return new ArrayList<>();
        }

    }

    public Contato getContato(Integer id) {
        Realm realm = Realm.getDefaultInstance();
        Contato contato = realm
                .where(Contato.class)
                .equalTo("id", id)
                .findFirst();

        if (contato != null) {
            return realm.copyFromRealm(contato);
        } else {
            return null;
        }
    }

    public void criaContatos() {
        ArrayList<Contato> contatos = new ArrayList<Contato>();

        for (int i = 0; i < 30; i++) {
            Contato contato = new Contato();

            contato.setId(i);
            contato.setNome("Meu nome é " + i);
            contato.setIdade(i * 10);
            contato.setTelefone(Math.random() + "");

            contatos.add(contato);
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(contatos);
        realm.commitTransaction();
    }


    public void salvaContato(Contato contato) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (contato.getId() == null) {
            Number max = realm.where(Contato.class).max("id");
            Integer nextId = 0;
            if (max != null) {
                nextId = max.intValue() + 1;
            }
            contato.setId(nextId);
        }
        realm.copyToRealmOrUpdate(contato);
        realm.commitTransaction();

    }

    public void salvaContato(List<Contato> contatoList) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(contatoList);
        realm.commitTransaction();
    }

    public void apagaContato(Contato contato) {
        Realm realm = Realm.getDefaultInstance();


        Contato contatoApagar = realm
                .where(Contato.class)
                .equalTo("id", contato.getId()).findFirst();


        realm.beginTransaction();
        contatoApagar.deleteFromRealm();
        realm.commitTransaction();
    }


    // Requests

    public void requestCriaContato(Contato contato) {

        contato.setContactScheduleId(idAgenda);

        EventBus.getDefault().post(new RequestBegunEvent());


        api.createContact(contato).enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                if (response.code() == 201) {
                    Contato contato = response.body();
                    salvaContato(contato);
                    EventBus.getDefault().post(new ContatoCriadoEvent());
                    EventBus.getDefault().post(new RequestFinishedEvent());

                } else {
                    Log.e(TAG, "requestCriaContato - onResponse: " + response.errorBody());
                    RequestFinishedEvent requestFinishedEvent = new RequestFinishedEvent();
                    requestFinishedEvent.setMensagem("Deu ruim!");
                    EventBus.getDefault().post(new RequestFinishedEvent());
                }

            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                Log.e(TAG, "requestCriaContato - onFailure: ", t);

                RequestFinishedEvent requestFinishedEvent = new RequestFinishedEvent();
                requestFinishedEvent.setMensagem("Deu ruim!");
                EventBus.getDefault().post(new RequestFinishedEvent());


            }
        });
    }

    public void requestListaContatos() {

        EventBus.getDefault().post(new RequestBegunEvent());

        api.getContact(idAgenda).enqueue(new Callback<List<Contato>>() {
            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                if (response.code() == 200) {
                    List<Contato> contatoList = response.body();
                    salvaContato(contatoList);
                    EventBus.getDefault().post(new ListaCriadaEvent());
                    EventBus.getDefault().post(new RequestFinishedEvent());


                } else {
                    EventBus.getDefault().post(new RequestFinishedEvent("Não foi 200"));
                }
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
                EventBus.getDefault().post(new RequestFinishedEvent("Falha na requisição!"));
            }
        });
    }

    public void requestAtualizaContato(Contato contato) {

        EventBus.getDefault().post(new RequestBegunEvent());

        api.updateContact(contato.getId(), contato).enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {

                if (response.code() == 200) {
                    Contato contato = response.body();
                    salvaContato(contato);
                    EventBus.getDefault().post(new ContatoCriadoEvent());
                    EventBus.getDefault().post(new RequestFinishedEvent());

                } else {
                    Log.e(TAG, "requestAtualizaContato - onResponse: " + response.errorBody());
                    RequestFinishedEvent requestFinishedEvent = new RequestFinishedEvent();
                    requestFinishedEvent.setMensagem("Deu ruim!");
                    EventBus.getDefault().post(new RequestFinishedEvent());
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                Log.e(TAG, "requestAtualizaContato - onFailure: ", t);

                RequestFinishedEvent requestFinishedEvent = new RequestFinishedEvent();
                requestFinishedEvent.setMensagem("Deu ruim!");
                EventBus.getDefault().post(new RequestFinishedEvent());
            }
        });
    }

    public void requestDeletaContato(Contato contato) {
        EventBus.getDefault().post(new RequestBegunEvent());

        api.deleteContact(contato.getId()).enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {

                if (response.code() == 200 || response.code() == 204) {
                    Contato contato = response.body();
                    apagaContato(contato);
                    EventBus.getDefault().post(new ContatoApagadoEvent());
                    EventBus.getDefault().post(new RequestFinishedEvent());

                } else {
                    Log.e(TAG, "requestApagaContato - onResponse: " + response.errorBody());
                    RequestFinishedEvent requestFinishedEvent = new RequestFinishedEvent();
                    requestFinishedEvent.setMensagem("Deu ruim!");
                    EventBus.getDefault().post(new RequestFinishedEvent());
                }

            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                Log.e(TAG, "requestApagaContato - onFailure: ", t);

                RequestFinishedEvent requestFinishedEvent = new RequestFinishedEvent();
                requestFinishedEvent.setMensagem("Deu ruim!");
                EventBus.getDefault().post(new RequestFinishedEvent());
            }
        });

    }

}
