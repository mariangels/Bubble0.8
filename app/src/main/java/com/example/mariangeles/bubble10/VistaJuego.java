package com.example.mariangeles.bubble10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;


public class VistaJuego extends SurfaceView implements SurfaceHolder.Callback {

    private HebraJuego hebraJuego;
    private ArrayList<Bubble> pompas;
    private int altoPantalla;
    private int anchoPantalla;
    private Bubble primera;
    private float direccion;
    private boolean stop=false;
    private boolean matrizVacia[][];
    private boolean matrizSeleccionada[][];


    static int A=48;//ancho y alto de la pompa
    static int borde=24;//sobra de dividir la pantalla entre 12
    //se queda a la derecha en los impares y a la izquierda en los pares

    private int FILAS = 24;
    private int COLUMNAS = 12;
    static int PASO = 10;

    public VistaJuego(Context context) {
        super(context);
        getHolder().addCallback(this);

        pompas= new ArrayList<Bubble>();
        matrizVacia = new boolean[12][FILAS];//inicializada a true  -> false para las que estan ocupadas
        inicializa();
    }

    public void inicializa(){

        for(int i=0; i<COLUMNAS; i++){
            for(int j=0; j<FILAS; j++){
                matrizVacia[i][j]=true;
            }
        }
    }

    /*****  Métodos de la clase surface ****/

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        movimiento();
        pintarPompas(canvas);
    }

    /*********  Métodos SurfaceHolder Callback  *********/

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
        altoPantalla=height;
        anchoPantalla=width-24;

        nuevaPrimera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean reintentar = true;
        hebraJuego.setFuncionando(false);
        //mientras pueda, seguira pintando
        while (reintentar) {
            try {
                hebraJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }
    }

    /************  Touch Event  **********/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x, y;
        x = event.getX()-A/2;
        y = event.getY()-A/2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                //dibujamos una linea o algo

                break;
            case MotionEvent.ACTION_UP:
                if(!stop){
                    direccion(x, y);
                    movimiento();
                    hebraJuego = new HebraJuego(this);
                    hebraJuego.setFuncionando(true);
                    hebraJuego.start();
                    stop=true;
                    //mostrar otra pompa
                }
                break;
        }
        return true;
    }

    /**** recursividad ****/

    public void recursividad(int fila, int columna){
        if(matrizVacia[fila][columna]){
            //si esta vacia
            if(matrizSeleccionada[fila][columna]==false){
                //si no esta seleccionada, se marca como si
                matrizSeleccionada[fila][columna]=true;
            }
        }else{
            matrizSeleccionada[fila][columna]=true;
            int color = pompas.get(pompas.indexOf(getBubble(fila, columna))).getColor();
            Log.v("Pasa por ","fila: "+ fila+", columna"+columna);

            if(matrizSeleccionada[fila-1][columna]){
                Log.v("derecha", (fila-1)+", "+columna);
                Bubble b= pompas.get(pompas.indexOf(getBubble(fila-1, columna)));
                if(b.getColor() == color) {
                    Log.v("false", " y color ");
                    recursividad(fila - 1, columna);
                    explotar(b);
                }
            }
            if(matrizSeleccionada[fila+1][columna]){
                Log.v("izquierda ", (fila+1)+", "+columna);
                Bubble b= pompas.get(pompas.indexOf(getBubble(fila+1, columna)));
                if(b.getColor() == color) {
                    Log.v("false", " y color ");
                    recursividad(fila + 1, columna);
                    explotar(b);
                }
            }
            if(matrizSeleccionada[fila][columna-1]){
                Log.v("arriba ", fila+", "+(columna-1));
                Bubble b= pompas.get(pompas.indexOf(getBubble(fila, columna-1)));
                if(b.getColor() == color) {
                    Log.v("false", " y color ");
                    recursividad(fila, columna-1);
                    explotar(b);
                }
            }
            if(matrizSeleccionada[fila][columna+1]){
                Log.v("abajo ", fila+", "+(columna+1));
                Bubble b= pompas.get(pompas.indexOf(getBubble(fila, columna+1)));
                if(b.getColor() == color) {
                    Log.v("false", " y color ");
                    recursividad(fila, columna+1);
                    explotar(b);
                }
            }
        }
    }

    public void inicializaSeleccionada(){
        matrizSeleccionada = new boolean[COLUMNAS][FILAS];
        for(int i=0; i<COLUMNAS; i++){
            for(int j=0; j<FILAS; j++){
                //if(matrizVacia[i][j])
                matrizSeleccionada[i][j]=true;
                //else
                //    matrizSeleccionada[i][j]=false;

            }
        }
    }

    public void explotar(Bubble bubble){
        for(Bubble b:pompas){
            if(b.getI()== bubble.getI() && b.getJ()== bubble.getJ()){
                pompas.remove(pompas.indexOf(b));
                matrizVacia[b.getI()][b.getJ()]=false;
            }
        }
    }

    public Bubble getBubble(int i, int j){
        for(Bubble b:pompas){
            if(b.getI()==i && b.getJ()==j){
                return b;
            }
        }
        return null;
    }

    /**** Direccion ****/

    public void direccion(float x, float y){
        float xPrimera = primera.getX()+A/2;
        float yPrimera = primera.getY()+A/2;
        direccion = (-(xPrimera- x) / (yPrimera - y) * PASO);
    }

    /**** Movimiento ****/

    public void movimiento(){
        //rebote
        if(primera.getX() + direccion < 0 || primera.getX() > anchoPantalla -A -direccion)
            direccion = -direccion;

        //primera.setPosicion(primera.getNextX(direccion), primera.getNextY());
        primera.setNextPosicion(direccion);

        //si choca arriba se coloca O si hay otra en la siguiente
        if(primera.getY() < A || nextOcupada()){
            colocar();
            nuevaPrimera();
            //la paramos
            if(hebraJuego.isFuncionando())
                hebraJuego.setFuncionando(false);
        }
    }

    public boolean nextOcupada(){
        int j = primera.getNextJ();
        int i = primera.getNextI(direccion);

        if(j<FILAS)
            if (matrizVacia[i][j] == false)
                //esta ocupada
                return true;
        return false;
    }

    public void colocar(){
        int i=primera.getI();
        int j=primera.getJ();
        Bubble pompa= new Bubble(i, j, primera.getColor(), primera.getBitmap());
        pompas.add(pompa);

        //ponemos matriz ocupada a false
        matrizVacia[i][j]=false;

        //llamamos a recursividad
        //inicializaSeleccionada();
        //recursividad(i,j);

        //hasta que no este colocada la bola no pasa nada por mucho que toques la pantalla
        stop=false;
    }

    public void nuevaPrimera(){
        //dibujamos la primera pompa que lanzamos
        primera = new Bubble();

        // 0 hasta 0.9999 de forma que *5 = 0 hasta 4.9999
        int rnd = (int)( Math.random()*5);

        primera.setColor(rnd);
        primera.setBitmap(setBitmap(rnd));

        //colocamos la primera pompa en en centro abajo de la pantalla
        //A*2 es un pequeño margen por abajo
        primera.setPosicion((anchoPantalla/2)-(A/2), altoPantalla-(A/2) -A*2 );
    }

    /**** Pintamos ****/

    public void pintarPompas(Canvas canvas){
        canvas.drawColor(Color.parseColor("#ff4ff5ff"));
        canvas.drawBitmap(primera.getBitmap(), primera.getX(), primera.getY(), null);
        for(Bubble b: pompas){
            canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), null);
        }
    }

    /**** Bitmap ****/

    public Bitmap setBitmap(int color){
        Bitmap bitmap = null;
        switch(color) {
            case 0:
                //blue
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
                break;
            case 1:
                //green
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green);
                break;
            case 2:
                //purple
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.purple);
                break;
            case 3:
                //red
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red);
                break;
            case 4:
                //yellow
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
                break;
        }
        return(bitmap);

    }

    /**** Tostada ****/

    public void tostada(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

}
