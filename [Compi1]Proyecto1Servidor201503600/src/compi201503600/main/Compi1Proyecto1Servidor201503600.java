package compi201503600.main;

import compi201503600.servidor.VentanaServidor;

public class Compi1Proyecto1Servidor201503600 {
    
    final static int PORT = 1234;
    final static int MAXCONNECTIONS = 20;

    public static void main(String[] args) {
        VentanaServidor v = new VentanaServidor();
        v.setVisible(true);
    }
    
}
