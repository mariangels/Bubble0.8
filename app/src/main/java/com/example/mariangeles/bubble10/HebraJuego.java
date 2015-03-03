package com.example.mariangeles.bubble10;


import android.graphics.Canvas;

public class HebraJuego extends Thread {

    private VistaJuego vista;
    //para que el acceso sea mas rapido
    // transient/volatile
    private boolean funcionando = false;
    private static final long FPS = 20;


    public HebraJuego(VistaJuego vj) {
        this.vista = vj;
    }

    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    public boolean isFuncionando() {
        return funcionando;
    }

    @Override
    public void run() {
        long inicio;
        long ticksPS = 1000 / FPS;
        long tiempoEspera;
        while (funcionando) {
            Canvas canvas = null;
            inicio = System.currentTimeMillis();
            // el tiempo, para que sean 20 fps es de 50s.
            // pintamos y dormimos hasta que pasen los 50s.
            try {
                canvas = vista.getHolder().lockCanvas();
                //nadie mas puede tocar mientras se dibuja, pero estamos
                //dentro de una hebra, necesitamos hacerlo de forma asincrona
                synchronized (vista.getHolder()) {
                    vista.draw(canvas);
                }
            } catch (NullPointerException e) {
                funcionando=false;
            } finally{
                if (canvas != null) {
                    vista.getHolder().unlockCanvasAndPost(canvas);
                }
            }

            tiempoEspera = ticksPS - (System.currentTimeMillis() - inicio);
            //pausamo el tiempo que nos sobra
            try {
                if (tiempoEspera > 0)
                    sleep(tiempoEspera);
                else
                    sleep(10);
            } catch (InterruptedException e) {}
        }
    }


    /*
    1. Obtenemos el canvas al tiempo que se congela.
    2. Recalculamos la posición de los elementos.
    3. Redibujamos los elementos en la nueva posición.
    4. Desbloqueamos el canvas.
     */

    //Deberemos cambiar algunas implementaciones de la vista del juego.

}
