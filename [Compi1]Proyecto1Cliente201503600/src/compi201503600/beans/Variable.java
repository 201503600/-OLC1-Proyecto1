/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi201503600.beans;

/**
 *
 * @author ed_ci
 */
public class Variable {
    private String nombre;
    private String tipo;
    private String metodo;
    private String clase;

    public Variable() {
        this.nombre = "";
        this.tipo = "";
        this.metodo = "";
        this.clase = "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
    
    

    public Variable(String nombre, String tipo, String metodo, String clase) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.metodo = metodo;
        this.clase = clase;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMetodo() {
        return metodo;
    }

    public String getClase() {
        return clase;
    }
    
    
}
