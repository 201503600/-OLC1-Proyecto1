
package compi201503600.servidor;

import compi201503600.analisis.java.ScannerSintaxJava;
import compi201503600.analisis.java.ScannerLexJava;
import compi201503600.beans.Clase;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.apache.log4j.Logger;

public class ConexionCliente extends Thread implements Observer{
    
    private Logger log = Logger.getLogger(ConexionCliente.class);
    private Socket socket; 
    private Sintonizador sintonizador;
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    private JTextPane consola;
    
    public ConexionCliente (Socket socket, Sintonizador sint, JTextPane consola){
        this.socket = socket;
        this.sintonizador = sint;
        this.consola = consola;
        
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            appendToPane(consola, "Error al crear los stream de entrada y salida: " + ex.getMessage(), Color.RED);
            log.error("Error al crear los stream de entrada y salida : " + ex.getMessage());
        }
    }
    
    @Override
    public void run(){
        String mensajeRecibido;
        boolean conectado = true;
        // Se apunta a la lista de observadores de mensajes
        sintonizador.addObserver(this);
        
        while (conectado) {
            try {
                // Lee un mensaje enviado por el cliente
                mensajeRecibido = entradaDatos.readUTF();
                File archivo1 = new File(mensajeRecibido.split(";")[0]);
                File archivo2 = new File(mensajeRecibido.split(";")[1]);
                appendToPane(consola, "Proyecto " + archivo1.getName() + " recibido", Color.BLUE);
                appendToPane(consola, "Proyecto " + archivo2.getName() + " recibido", Color.BLUE);
                
                ScannerSintaxJava sintax = new ScannerSintaxJava(new ScannerLexJava(new FileReader("test.txt")));
                Object result = sintax.parse().value;
                //ScannerSintaxJava.main(archivoPrueba); 
                Clase c = sintax.getNuevaClase();
                // Pone el mensaje recibido en mensajes para que se notifique 
                // a sus observadores que hay un nuevo mensaje.
                sintonizador.setMensaje(mensajeRecibido);
            } catch (IOException ex) {
                appendToPane(consola, "Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.", Color.RED);
                log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
                conectado = false; 
                // Si se ha producido un error al recibir datos del cliente se cierra la conexion con el.
                try {
                    entradaDatos.close();
                    salidaDatos.close();
                } catch (IOException ex2) {
                    appendToPane(consola, "Error al cerrar los stream de entrada y salida :" + ex2.getMessage(), Color.BLUE);
                    log.error("Error al cerrar los stream de entrada y salida :" + ex2.getMessage());
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ConexionCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    
    @Override
    public void update(Observable o, Object arg) {
        try {
            // Envia el mensaje al cliente
            salidaDatos.writeUTF("ANALISIS COMPLETADO :D");
        } catch (IOException ex) {
            log.error("Error al enviar mensaje al cliente (" + ex.getMessage() + ").");
        }
    }
    
    
     // Agrega un texto al editor de Java de un color especifico
    private void appendToPane(JTextPane tp, String msg, Color c){
        tp.setEditable(true);
        int lineas = msg.split("\n").length;
        
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg + "\n");
        tp.setEditable(false);
    }
}
