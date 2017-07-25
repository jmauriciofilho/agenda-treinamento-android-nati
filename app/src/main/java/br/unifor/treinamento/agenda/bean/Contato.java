package br.unifor.treinamento.agenda.bean;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Contato extends RealmObject {

    @PrimaryKey
    private Integer id;

    @SerializedName("name")
    private String nome;

    @SerializedName("phone")
    private String telefone;

    @SerializedName("age")
    private Integer idade;

    @SerializedName("contact_schedule_id")
    private Integer contactScheduleId;

    public Contato() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Integer getContactScheduleId() {
        return contactScheduleId;
    }

    public void setContactScheduleId(Integer contactScheduleId) {
        this.contactScheduleId = contactScheduleId;
    }
}
