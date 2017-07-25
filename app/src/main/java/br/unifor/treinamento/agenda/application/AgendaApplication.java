package br.unifor.treinamento.agenda.application;

import android.app.Application;

import io.realm.Realm;

public class AgendaApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
