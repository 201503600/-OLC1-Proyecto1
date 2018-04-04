/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi201503600.beans;

import java.util.ArrayList;

/**
 *
 * @author ed_ci
 */
public class Metodo {
    private String nombre;
    private String tipo;
    private int parametros;

    public Metodo() {
        this.nombre = "";
        this.tipo = "";
        this.parametros = 0;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    
    public Metodo(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.parametros = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
    
    public void setParametro(int parametro){
        this.parametros = parametro;
    }

    public int getParametros() {
        return parametros;
    }
}
