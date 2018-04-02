package compi201503600.main;

import compi201503600.cliente.VentanaCliente;
import org.apache.log4j.PropertyConfigurator;

public class Compi1Proyecto1Cliente201503600 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");        
               
        VentanaCliente vc = new VentanaCliente();
        vc.setVisible(true);
    }
    
}
