
package compi201503600.servidor;

import compi201503600.analisis.java.ScannerSintaxJava;
import compi201503600.analisis.java.ScannerLexJava;
import compi201503600.beans.Clase;
import compi201503600.beans.Metodo;
import compi201503600.beans.Variable;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class ConexionCliente extends Thread implements Observer{
    
    private Logger log = Logger.getLogger(ConexionCliente.class);
    private Socket socket; 
    private Sintonizador sintonizador;
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    private JTextPane consola;
    
    private String json;
    private ScannerSintaxJava sintax;
    
    public ConexionCliente (Socket socket, Sintonizador sint, JTextPane consola){
        this.socket = socket;
        this.sintonizador = sint;
        this.consola = consola;
        this.json = "";
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
            listfirst = new ArrayList<>();
            listsecond = new ArrayList<>();
            listclases = new ArrayList<>();
            listvars = new ArrayList<>();
            listcomments = new ArrayList<>();
            listmethods = new ArrayList<>();
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
                appendToPane(consola, "Iniciando analisis", Color.GRAY);
                this.analizar(mensajeRecibido.split(";")[0], mensajeRecibido.split(";")[1]);
                this.comparar();
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
    
    private ArrayList<Clase> listfirst, listsecond;
    private ArrayList<String> listclases;
    private ArrayList<Variable> listvars;
    private ArrayList<String> listcomments;
    private ArrayList<Metodo> listmethods;
    private double clases = 0, clasesRep = 0;
    private double vars = 0,varsRep = 0;
    private double comments = 0, commentsRep = 0;
    private double methods = 0, methodsRep = 0;
    private void analizar(String dir1, String dir2) {
        //Apertura del primer directorio
        File arch1 = new File(dir1);
        //Conseguimos un listado de los archivos y subdirectorios
        String[] ficheros1 = arch1.list();
        int i = 0;
        //Recorremos el listado de archivos y directorios para realizar la comparacion
        try {
            while (i < ficheros1.length) {
                File tmp1 = new File(arch1.getCanonicalPath() + "/" + ficheros1[i]);

                //Si el archivo es un directorio volvemos a invocar el metodo.
                if (tmp1.isDirectory()) {
                    this.analizar(arch1.getCanonicalPath() + "/" + ficheros1[i], dir2);    
                } else {
                    // Si el archivo es .java, se procede a recorrer el segundo directorio
                    if (FilenameUtils.getExtension(tmp1.getAbsolutePath()).equals("java")){
                        clases++;
                        //Apertura del segundo directorio
                        File arch2 = new File(dir2);
                        //Conseguimos un listado de los archivos y subdirectorios
                        String[] ficheros2 = arch2.list();
                        int j = 0;
                        //Recorremos el segundo listado para comparar los .java con el .java del primer directorio
                        try{
                            while(j < ficheros2.length){
                                File tmp2 = new File(arch2.getCanonicalPath() + "/" + ficheros2[j]);
                                //Si el archivo es un directorio volvemos a invocar al metodo
                                if (tmp2.isDirectory())
                                    this.analizar(dir1, arch2.getCanonicalPath() + "/" + ficheros2[j]);
                                else{
                                    //Si el archivo es .java, se procede a comparar ambos archivos
                                    if (FilenameUtils.getExtension(tmp2.getAbsolutePath()).equals("java")){
                                        // CODIGO DE COMPARACION
                                        appendToPane(consola, "Analizando " + tmp1.getName() + " ...", Color.GRAY);
                                        sintax = new ScannerSintaxJava(new ScannerLexJava(new FileReader(tmp1.getAbsolutePath())));
                                        sintax.parse();
                                        listfirst.add(sintax.getNuevaClase());
                                        appendToPane(consola, "Analizando " + tmp2.getName() + " ...", Color.GRAY);
                                        sintax = new ScannerSintaxJava(new ScannerLexJava(new FileReader(tmp2.getAbsolutePath())));
                                        sintax.parse();
                                        listsecond.add(sintax.getNuevaClase());
                                        
                                    }
                                }
                                j++;
                            }
                        }catch(IOException e2){
                            appendToPane(consola, e2.getMessage(), Color.RED);
                        } catch (Exception ex) {
                            appendToPane(consola, ex.getMessage(), Color.RED);
                            java.util.logging.Logger.getLogger(ConexionCliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                i++;
            }
        } catch (IOException e) {
            appendToPane(consola, e.getMessage(), Color.RED);
        }
    } 
    
    private void comparar(){
        
        for(Clase clase1:listfirst){
            comments += clase1.getComentarios().size();
            vars += clase1.getVariables().size();
            methods += clase1.getMetodos().size();
            for(Clase clase2:listsecond){
                appendToPane(consola, "Comparando " + clase1.getNombre() + " y " + clase2.getNombre() + " ...", Color.BLUE);
                
                 //Comparacion comentarios
                for (String com1 : clase1.getComentarios()) {
                    if (clase2.getComentarios().contains(com1)) {
                        commentsRep++;
                        listcomments.add(com1);
                    } else {
                        comments++;
                    }
                }
                
                //Comparación de variables
                for (Variable var1 : clase1.getVariables()) {
                    for (Variable var2 : clase2.getVariables()) {
                        if (var1 == clase1.getVariables().get(0)) {
                            vars++;
                            if (var1.getMetodo().equals("global") && var2.getMetodo().equals("global")) {
                                if (var1.getTipo().equals(var2.getTipo()) && var1.getNombre().equals(var2.getNombre())) {
                                    varsRep += 2;
                                    listvars.add(var1);
                                }
                            } else if (var1.getMetodo().equals("global") || var2.getMetodo().equals("global")) {
                                //vars++;
                            } else {
                                if (var1.getTipo().equals(var2.getTipo()) && var1.getNombre().equals(var2.getNombre())) {
                                    varsRep += 2;
                                    listvars.add(var1);
                                }
                            }
                        } else {
                            if (var1.getMetodo().equals("global") && var2.getMetodo().equals("global")) {
                                if (var1.getTipo().equals(var2.getTipo()) && var1.getNombre().equals(var2.getNombre())) {
                                    varsRep += 2;
                                    listvars.add(var1);
                                }
                            } else if (var1.getMetodo().equals("global") || var2.getMetodo().equals("global")) {
                                //No se hace nada, var2 ya fue agregada y no esta en el mismo ambito que var1
                            } else {
                                if (var1.getTipo().equals(var2.getTipo()) && var1.getNombre().equals(var2.getNombre())) {
                                    varsRep += 2;
                                    listvars.add(var1);
                                }
                            }
                        }
                    }
                }
                
                //Comparación de metodos
                boolean claseRep = false;
                for (Metodo met1 : clase1.getMetodos()) {
                    for (Metodo met2 : clase2.getMetodos()) {
                        if (met1 == clase1.getMetodos().get(0)) {
                            methods++;
                            if (met1.getNombre().equals(met2.getNombre()) && met1.getTipo().equals(met2.getTipo())
                                    && met1.getParametros().size() == met2.getParametros().size()) {
                                methodsRep += 2;
                                listmethods.add(met1);
                                claseRep = true;
                            }
                        } else {
                            if (met1.getNombre().equals(met2.getNombre()) && met1.getTipo().equals(met2.getTipo())
                                    && met1.getParametros().size() == met2.getParametros().size()) {
                                methodsRep += 2;
                                listmethods.add(met1);
                                claseRep = true;
                            }
                        }
                    }
                }
                
                //Comparación de clase
                if (claseRep && clase1.getNombre().equals(clase2.getNombre())) {
                    clasesRep++;
                    listclases.add(clase1.getNombre());
                } else {
                    clases++;
                }
                
            }
        }   
        appendToPane(consola, "\nGenerando Json ...", Color.GRAY);
        generarJson();
        appendToPane(consola, "Json generado!", Color.GREEN);
    }
    
    private void generarJson(){
        DecimalFormat format = new DecimalFormat("#.00");
        double score = 0.25*(commentsRep/comments + varsRep/vars + methodsRep/methods + clasesRep/clases);
        json += "{";
        
        //Generando codigo score
        json += "score:[" + format.format(score) + "]";
        
        //Generando codigo clases
        json += ", clases:[";
        for(String c:listclases)
            if (c.equals(listclases.get(0)))
                json += "{nombre:\"" + c + "\"}";
            else
                json += ", {nombre:\"" + c + "\"}";
        json += "]";
        
        //Generando codigo variables
        json += ", variables:[";
        for(Variable v:listvars)
            if (v == listvars.get(0))
                json += "{nombre:\"" + v.getNombre() + "\", tipo:\"" + v.getTipo() + "\", funcion:\"" + 
                        v.getMetodo() + "\", clase:\"" + v.getClase() + "\"}";
            else
                json += ", {nombre:\"" + v.getNombre() + "\", tipo:\"" + v.getTipo() + "\", funcion:\"" + 
                        v.getMetodo() + "\", clase:\"" + v.getClase() + "\"}";
        json += "]";
        
        //Generando codigo metodos
        json += ", metodos:[";
        for(Metodo m:listmethods)
            if (m == listmethods.get(0))
                json += "{nombre:\"" + m.getNombre() + "\", tipo:\"" + m.getTipo() + "\", parametros:\"" + 
                        m.getParametros().size() + "\"}";
            else
                json += ", {nombre:\"" + m.getNombre() + "\", tipo:\"" + m.getTipo() + "\", parametros:\"" + 
                        m.getParametros().size() + "\"}";
        json += "]";
        
        //Generando codigo comentarios
        json += ", comentarios:[";
        for(String c:listcomments){
            if (c.startsWith("//"))
                c = c.replace("\n", "");
            if ((c + "\n").equals(listcomments.get(0)))
                json += "{texto:\"" + c + "\"}";
            else
                json += ", {texto:\"" + c + "\"}";
        }
        json += "]";
        
        json += "}";
        
        clases = clasesRep = vars = varsRep = comments = commentsRep = methods = methodsRep = 0;
        listclases = new ArrayList<>();
        listvars = new ArrayList<>();
        listcomments = new ArrayList<>();
        listmethods = new ArrayList<>();
        listfirst = new ArrayList<>();
        listsecond = new ArrayList<>();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        try {
            // Envia el mensaje al cliente
            salidaDatos.writeUTF(json);
            json = "";
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
