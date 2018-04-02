package compi201503600.cliente;

import com.icesoft.faces.component.tree.IceUserObject;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class VentanaCliente extends javax.swing.JFrame implements ActionListener, ChangeListener, MouseListener{

    private Logger log = Logger.getLogger(VentanaCliente.class);
    private Socket socket;
    
    private int puerto;
    private String host;
    private ConexionServidor output;
    
    private DefaultMutableTreeNode raiz;
    private JPopupMenu popMenu;
    
    public VentanaCliente() {        
        initComponents();
        
        raiz = new DefaultMutableTreeNode();
        IceUserObject detailNode = new IceUserObject(raiz);
        detailNode.setText("Proyectos");
        detailNode.setExpanded(true);
        detailNode.setLeaf(false);
        raiz.setUserObject(detailNode);
        DefaultTreeModel model = new DefaultTreeModel(raiz);
        arbolProyectos.setModel(model);
        
        popMenu = new JPopupMenu();
        popMenu.add(new AccionMenu("Cargar proyecto"));
        popMenu.add(new AccionMenu("Cancelar"));
        
        mn_create.addActionListener(this);
        mn_open.addActionListener(this);
        mn_save.addActionListener(this);
        mn_delete.addActionListener(this);
        mn_chargeProject.addActionListener(this);
        mn_analizeProject.addActionListener(this);
        tabReport.addChangeListener(this);
        arbolProyectos.addMouseListener(this);
        
        pathFolder = new ArrayList<String>();
        archivos = new ArrayList<String>();
        
        // Ventana de configuracion inicial
        VentanaConfiguracion vc = new VentanaConfiguracion(this);
        host = vc.getHost();
        puerto = vc.getPuerto();
        
        log.info("Conectando a " + host + " en el puerto " + puerto);
        appendToPane(jTextPane1, "Conectando a " + host + ":" + puerto, Color.GRAY);
        
        // Se crea el socket para conectar con el Sevidor del Chat
        try {
            socket = new Socket(host, puerto);
            Cliente c = new Cliente();
            c.start();
            appendToPane(jTextPane1, "Conexion establecida", Color.BLUE);
        } catch (UnknownHostException ex) {
            appendToPane(jTextPane1, "No se ha podido conectar con el servidor (" + ex.getMessage() + ").", Color.RED);
            log.error("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            appendToPane(jTextPane1, "No se ha podido conectar con el servidor (" + ex.getMessage() + ").", Color.RED);
            log.error("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
        
        output = new ConexionServidor(socket, jTextPane1);
    }
        
    private JFileChooser seleccionador;
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mn_create){
            
        }
        else if (e.getSource() == mn_open){
            seleccionador = new JFileChooser();
            seleccionador.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int opcion = seleccionador.showOpenDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION){
                File archivo = seleccionador.getSelectedFile();
                
                
            }
        }
        else if (e.getSource() == mn_save){
            
        }
        else if (e.getSource() == mn_delete){
            
        }
        else if (e.getSource() == mn_chargeProject){
            seleccionador = new JFileChooser();
            seleccionador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int opcion = seleccionador.showOpenDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION){
                DefaultMutableTreeNode proyecto = new DefaultMutableTreeNode();
                IceUserObject detailNode = new IceUserObject(proyecto);
                detailNode.setText(seleccionador.getSelectedFile().getName());
                detailNode.setExpanded(true);
                detailNode.setLeaf(false);
                proyecto.setUserObject(detailNode);
                pathFolder.add(seleccionador.getSelectedFile().getAbsolutePath());  
                //Directorio a visualizar
                this.get_tree(seleccionador.getSelectedFile().getAbsolutePath(), proyecto);

                DefaultTreeModel model = (DefaultTreeModel) this.arbolProyectos.getModel();
                model.insertNodeInto(proyecto, raiz, 0);
            }
        }
        else if (e.getSource() == mn_analizeProject){
            if (archivos.size() == 2){
                output.enviarMensaje(archivos.get(0) + ";" + archivos.get(1));
            }else
                JOptionPane.showMessageDialog(this, "Deben cargarse dos archivos", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void get_tree(String dir, DefaultMutableTreeNode parent) {
        //Apertura del directorio
        File a = new File(dir);
        //Conseguimos un listado de los archivos y subdirectorios
        String[] ficheros = a.list();
        int i = 0;

        //Agregamos los nodos al arbol, por cada archivo encontrado
        try {
            while (i < ficheros.length) {
                File tmp = new File(a.getCanonicalPath() + "/" + ficheros[i]);

                DefaultMutableTreeNode nodo = new DefaultMutableTreeNode();
                IceUserObject objeto = new IceUserObject(nodo);
                objeto.setText(ficheros[i]);
                objeto.setExpanded(false);

                //Si el archivo es un directorio volvemos a invocar el metodo.
                if (tmp.isDirectory()) {
                    objeto.setLeaf(false);
                    this.get_tree(a.getCanonicalPath() + "/" + ficheros[i], nodo);
                    nodo.setUserObject(objeto);
                    parent.add(nodo);          
                } else {
                    objeto.setLeaf(true);
                    if (FilenameUtils.getExtension(tmp.getAbsolutePath()).equals("java")){
                        nodo.setUserObject(objeto);
                        parent.add(nodo);                    
                    }
                }
                
                i++;
            }
        } catch (IOException e) {

        }
    }    

    private ArrayList<JPanel> contentTabs;
    private ArrayList<String> pathTabs;
    @Override
    public void stateChanged(ChangeEvent e) {
        
    }
    
    public JTextPane getConsola(){
        return jTextPane1;
    }
    
     // Agrega un texto al editor de Java de un color especifico
    public void appendToPane(JTextPane tp, String msg, Color c){
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem7 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        scrollProject = new javax.swing.JScrollPane();
        arbolProyectos = new javax.swing.JTree();
        tabReport = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mn_create = new javax.swing.JMenuItem();
        mn_open = new javax.swing.JMenuItem();
        mn_save = new javax.swing.JMenuItem();
        mn_delete = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mn_chargeProject = new javax.swing.JMenuItem();
        mn_analizeProject = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        jMenuItem7.setText("jMenuItem7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Detector de copias USAC");
        setMaximumSize(new java.awt.Dimension(1200, 700));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setAutoscrolls(true);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        arbolProyectos.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        scrollProject.setViewportView(arbolProyectos);

        jTabbedPane1.addTab("Proyectos", scrollProject);

        jScrollPane1.setViewportView(jTextPane1);

        jTabbedPane3.addTab("Consola", jScrollPane1);

        jMenu1.setText("Archivo");

        mn_create.setText("Crear");
        jMenu1.add(mn_create);

        mn_open.setText("Abrir");
        jMenu1.add(mn_open);

        mn_save.setText("Guardar");
        jMenu1.add(mn_save);

        mn_delete.setText("Eliminar");
        jMenu1.add(mn_delete);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Proyectos");

        mn_chargeProject.setText("Cargar Proyectos");
        jMenu2.add(mn_chargeProject);

        mn_analizeProject.setText("Analizar Proyectos");
        jMenu2.add(mn_analizeProject);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Analizar");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Reportes");
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabReport, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                    .addComponent(jTabbedPane3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabReport, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree arbolProyectos;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JMenuItem mn_analizeProject;
    private javax.swing.JMenuItem mn_chargeProject;
    private javax.swing.JMenuItem mn_create;
    private javax.swing.JMenuItem mn_delete;
    private javax.swing.JMenuItem mn_open;
    private javax.swing.JMenuItem mn_save;
    private javax.swing.JScrollPane scrollProject;
    private javax.swing.JTabbedPane tabReport;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            int row = arbolProyectos.getClosestRowForLocation(e.getX(), e.getY());
            arbolProyectos.setSelectionRow(row);
            TreePath tp = arbolProyectos.getSelectionPath();
            MutableTreeNode mtn = (MutableTreeNode) tp.getLastPathComponent();
            if (mtn.getParent() == raiz){
                popMenu.setLocation(e.getLocationOnScreen());
                popMenu.setVisible(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    private ArrayList<String> pathFolder;
    private ArrayList<String> archivos;
    public class AccionMenu extends AbstractAction {

        private String textoOpcion;

        public AccionMenu(String textoOpcion) {
            this.textoOpcion = textoOpcion;
            this.putValue(Action.NAME, textoOpcion);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (textoOpcion.equals("Cargar proyecto")){
                for(String name : pathFolder){                    
                    if (name.endsWith(arbolProyectos.getSelectionPath().getLastPathComponent().toString())){
                        cargarArchivo(name);
                        break;
                    }
                }
            }
            
            popMenu.setVisible(false);
        }
        
        private void cargarArchivo(String archivo){
            if (archivos.size() <= 1)
                archivos.add(archivo);
            else if (archivos.size() == 2){
                archivos.set(0, archivos.get(1));
                archivos.set(1, archivo);
            }
        }
    }
    
    public class Cliente extends Thread {
        
        public Cliente() {
            super();
        }

        public void run() {
            recibirMensaje();
        }
        
        public void recibirMensaje() {
            // Obtiene el flujo de entrada del socket
            DataInputStream entradaDatos = null;
            String response;
            try {
                entradaDatos = new DataInputStream(socket.getInputStream());
            } catch (IOException ex) {
                appendToPane(jTextPane1, "Error al crear el stream de entrada", Color.RED);
                log.error("Error al crear el stream de entrada: " + ex.getMessage());
            } catch (NullPointerException ex) {
                appendToPane(jTextPane1, "El socket no se creo correctamente", Color.RED);
                log.error("El socket no se creo correctamente. ");
            }

            // Bucle infinito que recibe mensajes del servidor
            boolean conectado = true;
            while (conectado) {
                try {
                    response = entradaDatos.readUTF();
                    JOptionPane.showMessageDialog(null, response);
                    
                } catch (IOException ex) {
                    appendToPane(jTextPane1, "Error al leer del stream de entrada: " + ex.getMessage(), Color.RED);
                    log.error("Error al leer del stream de entrada: " + ex.getMessage());
                    conectado = false;
                } catch (NullPointerException ex) {
                    appendToPane(jTextPane1, "El socket no se creo correctamente", Color.RED);
                    log.error("El socket no se creo correctamente. ");
                    conectado = false;
                }
            }
    }
        
    }

}
