/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compi201503600.analisis.java;

/**
 *
 * @author jose_
 */
public class ErroresLexicos {
     String valor ="";
    int linea =0;
    int columna = 0;

    public ErroresLexicos(String valor , int linea , int columna) {
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    
}
