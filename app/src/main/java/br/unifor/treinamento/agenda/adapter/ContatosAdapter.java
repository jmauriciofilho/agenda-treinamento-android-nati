package br.unifor.treinamento.agenda.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import br.unifor.treinamento.agenda.R;
import br.unifor.treinamento.agenda.bean.Contato;
import br.unifor.treinamento.agenda.service.ContatoService;
import br.unifor.treinamento.agenda.view.item.ItemContato;

@EBean
public class ContatosAdapter extends RecyclerView.Adapter<ItemContato> {

    @Bean
    ContatoService contatoService;

    private List<Contato> itens = new ArrayList<>();

    @Override
    public ItemContato onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contato, parent, false);

        ItemContato vh = new ItemContato(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ItemContato holder, int position) {
        holder.bind(itens.get(position), position);
    }


    @Override
    public int getItemCount() {
        return itens.size();
    }


    public Contato getItemAt(int position) {
        return itens.get(position);
    }

    @AfterInject
    public void refresh() {
        itens = contatoService.getContatos();
        notifyDataSetChanged();
    }
}
