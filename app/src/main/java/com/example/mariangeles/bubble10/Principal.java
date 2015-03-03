package com.example.mariangeles.bubble10;

import android.app.Activity;
import android.os.Bundle;


public class Principal extends Activity {

    private VistaJuego vjuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vjuego = new VistaJuego(this);
        setContentView(vjuego);
    }
}