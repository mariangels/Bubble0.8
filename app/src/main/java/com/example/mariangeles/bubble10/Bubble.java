package com.example.mariangeles.bubble10;

import android.graphics.Bitmap;
import android.util.Log;

public class Bubble {

    //todas tienen un bitmap asociado
    private Bitmap bitmap;
    private int color;
    // posicion y direccion para la que se mueve
    private float x;
    private float y;
    //para las de la matriz
    private int i;
    private int j;
    //ancho o alto de la pompa
    private int A = VistaJuego.A;
    private int borde = VistaJuego.borde;
    private int PASO = VistaJuego.PASO;


    /**** Constructores BUBBLE ****/

    public Bubble() {
    }

    public Bubble(int i, int j, int color, Bitmap bitmap) {
        this.i=i;
        this.j=j;
        this.color = color;
        this.bitmap=bitmap;
        this.colocar();
    }

    /**************************** BUBBLE MATRIZ **********************************/

    /**** Posicion en la matriz ****/

    public void colocar(){
        this.y = this.j *A;
        //if(this.j%2==0){
            this.x = this.i *A;
        //}else{
            //this.x = borde + this.i*A;
        //}
    }

    public int getI(){
        //if(getNextJ()%2==0) {
            return Math.round(x/A);
        //}
        //return (x-borde)/A;
    }

    public int getJ(){
        return Math.round(y/A);
    }

    /*************************** BUBBLE PRIMERA ************************************/
    // la que se mueve

    /**** Posicion  ****/

    public void setPosicion(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**** Siguiente paso ****/

    public void setNextPosicion(float direccion){
        //int base = x + Math.round(direccion);
        //if(getNextJ()%2==0) {
            this.x = x + Math.round(direccion);
        //}else{
           // this.x = (base-borde);
        //}
        this.y = y - PASO;
    }

    public int getNextI(float direccion) {
        //int base = this.x + Math.round(direccion);
        //if(getNextJ()%2==0) {
            return Math.round((this.x + Math.round(direccion))/A);
        //}
        //return (base-borde)/A;
    }

    public int getNextJ() {
        return Math.round((this.y - PASO)/A);
    }

    /********************************  ALL ****************************************/

    /**** Posicion  ****/

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**** Bitmap ****/

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap=bitmap;
    }

    /**** Color ****/

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
