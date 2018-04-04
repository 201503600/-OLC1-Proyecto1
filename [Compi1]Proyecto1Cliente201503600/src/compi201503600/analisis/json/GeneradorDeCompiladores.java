package compi201503600.analisis.json;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author esvux
 */

public class GeneradorDeCompiladores {
    
    public static void main(String[] args) {
        generarCompilador();
    }
    
    private static void generarCompilador(){
        System.out.println("\n*** Generando ***\n");
        String archLexico = "";
        String archSintactico = "";
               
        System.out.println("\n*** Procesando archivo default ***\n");
        archLexico = "src/compi201503600/analisis/json/lexicoJson.jflex";
        archSintactico = "src/compi201503600/analisis/json/sintacticoJson.cup";
        String[] alexico = {archLexico};
        String[] asintactico = {"-parser", "ScannerSintaxJson", archSintactico};
        jflex.Main.main(alexico);
        try {
            java_cup.Main.main(asintactico);
        } catch (Exception ex) {
            Logger.getLogger(GeneradorDeCompiladores.class.getName()).log(Level.SEVERE, null, ex);
        }
        //movemos los archivos generados
        boolean mvAL = moverArch("ScannerLexJson.java");
        boolean mvAS = moverArch("ScannerSintaxJson.java");
        boolean mvSym = moverArch("sym.java");
        if (mvAL && mvAS && mvSym) {
            System.exit(0);
        }
        System.out.println("Generado!");

    }
    
    public static boolean moverArch(String archNombre) {
        boolean efectuado = false;
        File arch = new File(archNombre);
        if (arch.exists()) {
            System.out.println("\n*** Moviendo " + arch + " \n***");
            Path currentRelativePath = Paths.get("");
            String nuevoDir = currentRelativePath.toAbsolutePath().toString()
                    + File.separator + "src" + File.separator
                    + "compi201503600" + File.separator + "analisis" +
                    File.separator + "json" + File.separator + arch.getName();
            File archViejo = new File(nuevoDir);
            archViejo.delete();
            if (arch.renameTo(new File(nuevoDir))) {
                System.out.println("\n*** Generado " + archNombre + "***\n");
                efectuado = true;
            } else {
                System.out.println("\n*** No movido " + archNombre + " ***\n");
            }

        } else {
            System.out.println("\n*** Codigo no existente ***\n");
        }
        return efectuado;
    }
}
