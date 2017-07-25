package br.unifor.treinamento.agenda.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.unifor.treinamento.agenda.R;
import br.unifor.treinamento.agenda.adapter.ContatosAdapter;
import br.unifor.treinamento.agenda.bean.Contato;
import br.unifor.treinamento.agenda.event.ContatoClicadoEvent;
import br.unifor.treinamento.agenda.event.ListaCriadaEvent;
import br.unifor.treinamento.agenda.event.RequestBegunEvent;
import br.unifor.treinamento.agenda.event.RequestFinishedEvent;
import br.unifor.treinamento.agenda.service.ContatoService;


@EActivity(R.layout.activity_agenda)
public class AgendaActivity extends AppCompatActivity {

    @Bean
    ContatoService contatoService;

    @ViewById(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @ViewById
    protected FloatingActionButton fab;

    @ViewById
    protected Toolbar toolbar;

    @Bean
    protected ContatosAdapter adapter;

    protected ProgressDialog progressDialog;

    @AfterViews
    protected void afterViews() {

        setSupportActionBar(toolbar);

        // Configurar o click do FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalheContatoActivity_.intent(getApplicationContext()).start();
            }
        });

        // Configura Adapter
        recyclerView.setAdapter(adapter);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);

        contatoService.requestListaContatos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.refresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ContatoClicadoEvent event) {
        Contato contato = adapter.getItemAt(event.getPosition());

        DetalheContatoActivity_.intent(this).idContato(contato.getId()).start();
    }


    @Subscribe
    public void onEvent(RequestBegunEvent event) {
        progressDialog.show();
    }

    @Subscribe
    public void onEvent(RequestFinishedEvent event) {
        progressDialog.dismiss();
        if (event.getMensagem() != null) {
            Snackbar.make(recyclerView, event.getMensagem(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent (ListaCriadaEvent event){
        adapter.refresh();
    }

}
