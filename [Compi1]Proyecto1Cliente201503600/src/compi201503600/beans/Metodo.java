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
    private ArrayList<Parametros> parametros;

    public Metodo(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.parametros = new ArrayList<Parametros>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
    
    public void setParametro(Parametros parametro){
        this.parametros.add(parametro);
    }

    public ArrayList<Parametros> getParametros() {
        return parametros;
    }
    
    public class Parametros{
        private String nombre;
        private String tipo;
        
        public Parametros(String nombre, String tipo){
            this.nombre = nombre;
            this.tipo = tipo;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }
        
        
    }
}
