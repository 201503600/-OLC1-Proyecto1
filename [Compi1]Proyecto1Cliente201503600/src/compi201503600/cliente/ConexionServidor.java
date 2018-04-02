package compi201503600.cliente;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.apache.log4j.Logger;

public class ConexionServidor {
    
    private Logger log = Logger.getLogger(ConexionServidor.class);
    private Socket socket; 
    private DataOutputStream salidaDatos;
    private JTextPane consola;
    
    public ConexionServidor(Socket socket, JTextPane consola) {
        this.socket = socket;
        this.consola = consola;
        try {
            this.salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            appendToPane(consola, "Error al crear el stream de salida: " + ex.getMessage(), Color.RED);
            log.error("Error al crear el stream de salida : " + ex.getMessage());
        } catch (NullPointerException ex) {
            appendToPane(consola, "El socket no se creo correctamente", Color.RED);
            log.error("El socket no se creo correctamente. ");
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

    public void enviarMensaje(String msg) {
        try {
            salidaDatos.writeUTF(msg);
        } catch (IOException ex) {
            appendToPane(consola, "Error al intentar enviar un mensaje: " + ex.getMessage(), Color.RED);
            log.error("Error al intentar enviar un mensaje: " + ex.getMessage());
        }
    }

}