package br.unifor.treinamento.agenda.event;


public class RequestFinishedEvent {

    String mensagem;

    public RequestFinishedEvent() {
    }

    public RequestFinishedEvent(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
