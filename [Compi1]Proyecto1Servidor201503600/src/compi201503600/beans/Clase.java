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
public class Clase {
    private String nombre;
    private ArrayList<Variable> variables;
    private ArrayList<Metodo> metodos;
    private ArrayList<String> comentarios;

    public Clase() {
        this.nombre = "";
        this.variables = new ArrayList<Variable>();
        this.metodos = new ArrayList<Metodo>();
        this.comentarios = new ArrayList<String>();
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setVariable(Variable var){
        this.variables.add(var);
    }
    
    public void setMetodo(Metodo metodo){
        this.metodos.add(metodo);
    }
    
    public void setComentario(String comentario){
        this.comentarios.add(comentario);
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public ArrayList<Metodo> getMetodos() {
        return metodos;
    }

    public ArrayList<String> getComentarios() {
        return comentarios;
    }
    
    
}
