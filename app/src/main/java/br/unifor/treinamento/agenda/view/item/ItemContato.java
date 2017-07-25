package br.unifor.treinamento.agenda.view.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import br.unifor.treinamento.agenda.R;
import br.unifor.treinamento.agenda.bean.Contato;
import br.unifor.treinamento.agenda.event.ContatoClicadoEvent;

public class ItemContato extends RecyclerView.ViewHolder {

    private TextView nome;
    private TextView telefone;
    private TextView idade;

    public ItemContato(View itemView) {
        super(itemView);
        nome = (TextView) itemView.findViewById(R.id.nome);
        telefone = (TextView) itemView.findViewById(R.id.telefone);
        idade = (TextView) itemView.findViewById(R.id.idade);
    }

    public void bind(Contato contato, final int position) {
        nome.setText(contato.getNome());
        telefone.setText(contato.getTelefone());
        idade.setText(contato.getIdade() + "");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ContatoClicadoEvent(position));
            }
        });

    }


}


