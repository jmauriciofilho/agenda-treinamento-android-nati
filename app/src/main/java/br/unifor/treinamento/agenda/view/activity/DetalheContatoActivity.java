package br.unifor.treinamento.agenda.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.unifor.treinamento.agenda.R;
import br.unifor.treinamento.agenda.bean.Contato;
import br.unifor.treinamento.agenda.event.ContatoApagadoEvent;
import br.unifor.treinamento.agenda.event.ContatoCriadoEvent;
import br.unifor.treinamento.agenda.event.RequestBegunEvent;
import br.unifor.treinamento.agenda.event.RequestFinishedEvent;
import br.unifor.treinamento.agenda.service.ContatoService;


@EActivity(R.layout.activity_detalhe_contato)
public class DetalheContatoActivity extends AppCompatActivity {

    @Bean
    ContatoService contatoService;

    private static final String TAG = "DtlContact";

    @ViewById(R.id.campo_nome)
    protected TextView campoNome;

    @ViewById(R.id.campo_telefone)
    protected TextView campoTelefone;

    @ViewById(R.id.campo_idade)
    protected TextView campoIdade;

    protected ProgressDialog progressDialog;

    @Extra
    protected Integer idContato;

    private Contato contato;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @AfterViews
    protected void afterViews() {

        contato = contatoService.getContato(idContato);

        if (contato != null) {
            campoNome.setText(contato.getNome());
            campoTelefone.setText(contato.getTelefone());
            campoIdade.setText(String.valueOf(contato.getIdade()));
        } else {
            contato = new Contato();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detalhe_contato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_excluir:
                excluir();
                break;
            case R.id.action_salvar:
                salvar();
                break;
            default:
                Log.d(TAG, "Eu não conheço esse Menu");
                return false;

        }
        return true;
    }

    private void salvar() {

        contato.setNome(campoNome.getText().toString());
        contato.setTelefone(campoTelefone.getText().toString());
        contato.setIdade(Integer.parseInt(campoIdade.getText().toString()));

        if (contato.getId() == null) {
            contatoService.requestCriaContato(contato);
        } else {
            contatoService.requestAtualizaContato(contato);
        }
    }

    private void excluir() {

        if (contato == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção!!");
        builder.setMessage("Deseja apagar esse contato?");
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                contatoService.requestDeletaContato(contato);
                finish();
            }
        });

        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    @Subscribe
    public void onEvent(RequestBegunEvent event) {
        progressDialog.show();
    }

    @Subscribe
    public void onEvent(RequestFinishedEvent event) {
        progressDialog.dismiss();
        if (event.getMensagem() != null) {
            Snackbar.make(campoNome, event.getMensagem(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(ContatoCriadoEvent event) {
        Snackbar.make(campoNome, R.string.contato_salvo, Snackbar.LENGTH_SHORT).show();
    }

    public void onEvent(ContatoApagadoEvent event) {
        Snackbar.make(campoNome, R.string.contato_apagado, Snackbar.LENGTH_SHORT).show();
    }


}
