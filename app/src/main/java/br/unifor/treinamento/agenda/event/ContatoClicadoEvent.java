package br.unifor.treinamento.agenda.event;


public class ContatoClicadoEvent {

    private int position;

    public ContatoClicadoEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
