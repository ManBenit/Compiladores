package compilador;

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.ClaseLexica;
import AnalizadorLexico.Estado;
import AnalizadorLexico.Transicion;
import AnalizadorSintactico.CreadorAutomatas;
import GUI.Monitor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import pruebas.Ejemplo;
import java.io.File; 
import java.io.IOException; 
import java.io.InputStream; 

public class Compilador {

    public static void main(String[] args) {
        //Prueba con la tarea realizada casi al inicio del curso
        //Ejemplo p= new Ejemplo();
        //p.ejemploRango();
        //p.pruebaGeneradorAfn();
        
//        AnalizadorLexico lexic= new AnalizadorLexico(generarAuxiliar());
        //String cad= "DD.DDTTLLDEMEEP";//DD.DDTTLLDEMEEP
        
        /*String rutaCarga= adaptarRuta("../gramatest.txt");
        ClaseLexica miclaselex= new ClaseLexica(rutaCarga);
        ArrayList<AFN> elementosUnificacion= new ArrayList();
        String cl="";

        cl= miclaselex.nombreClases()[2];
        AFN a1= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a2= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a3= new AFN(cl, miclaselex.tokenClase(cl));
        a1.crearBasico('A', 'Z');
        a2.crearBasico('0', '9');
        a1.unir(a2);
        a1.cEstrella();
        a3.crearBasico('A', 'Z');
        a3.concatenar(a1);
        elementosUnificacion.add(a3);
//        System.out.println(a3);
//        System.out.println("");
        cl= miclaselex.nombreClases()[4];
        AFN a4= new AFN(cl, miclaselex.tokenClase(cl));
        a4.crearBasico('0', '9');
        a4.cTransitiva();
        elementosUnificacion.add(a4);
//        System.out.println(a4);
//        System.out.println("");
        
        cl= miclaselex.nombreClases()[0];
        AFN a5= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a6= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a7= new AFN(cl, miclaselex.tokenClase(cl));
        a5.crearBasico('0', '9');
        a6.crearBasico('.');
        a7.crearBasico('0', '9');
        a5.cTransitiva();
        a7.cTransitiva();
        a5.concatenar(a6);
        a5.concatenar(a7);
        elementosUnificacion.add(a5);
//        System.out.println(a5);
//        System.out.println("");
        
        cl= miclaselex.nombreClases()[5];
        AFN a8= new AFN(cl, miclaselex.tokenClase(cl));
        a8.crearBasico('+');
        elementosUnificacion.add(a8);
//        System.out.println(a8);
//        System.out.println("");
        
        cl= miclaselex.nombreClases()[1];
        AFN a9= new AFN(cl, miclaselex.tokenClase(cl));
        a9.crearBasico('*');
        elementosUnificacion.add(a9);
//        System.out.println(a9);
//        System.out.println("");
        
        cl= miclaselex.nombreClases()[3];
        AFN a10= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a11= new AFN(cl, miclaselex.tokenClase(cl));
        a10.crearBasico(' ');
        a11.crearBasico((char)9);
        a10.unir(a11);
        a10.cTransitiva();
        elementosUnificacion.add(a10);
//        System.out.println(a10);
//        System.out.println("");
        
        
        AFN total= AFN.unificarInicial(elementosUnificacion);
        
        AFD afd= new AFD("Tarea autómata", miclaselex.alfabeto());
        afd.convertir(total);
        
        
        escribir(total, adaptarRuta("../AFN.txt"));
        escribir(afd, adaptarRuta("../AFD.txt"));
        escribirTablaEdos(afd.tablaDeEstados(), afd.alfabeto(), adaptarRuta("../TablaAFD.txt"));
        
        
        
        
        //Instancia del analizador
        AnalizadorLexico lexic= new AnalizadorLexico(afd);
        
        
        
        
        
        
        //La prueba :v
        String cad= "12.34      AB5 +  *";
        //String cad= "      12.34AB5 +  *";
        if(lexic.validarCadena(cad))
            System.out.println("CADENA VÁLIDA");
        else
            System.out.println("CADENA INVÁLIDA");
        
        int token=0;
        try{
            while((token=lexic.yylex())!=0){
                System.out.println(lexic.yytex()+", token "+token);
            }
        }catch(NoSuchElementException ex){
            System.out.println("Todo leído");
        }
        */
        /*try{
            //Runtime.getRuntime().exec("md E:\\GitHub\\Compiladores\\suCarpeta");
           // Runtime.getRuntime().exec("javac E:\\GitHub\\Compiladores\\elPrograma.java");
           Runtime.getRuntime().exec("java E:\\GitHub\\Compiladores\\elPrograma");
        }catch(IOException ex){
            System.out.println("nel karnal\n"+ex);
        }*/
        //new Monitor().setVisible(true);
        //CreadorAutomatas ca= new CreadorAutomatas("../gramatest.txt");
        
        
        //new Compilador(); 
        
        
        AFN afnAnalizador= new AFN("Total", 0);
        CreadorAutomatas accionSemantica= new CreadorAutomatas(adaptarRuta("../gramatest.txt"), generarAuxiliar());
        accionSemantica.iniciar(afnAnalizador);
        
    }
    
    //Genera el AFD que alimenta inicialmente el analizador léxico para así poder trabajar con los métodos booleanos (acciones semánticas)
    private static AFD generarAuxiliar(){
        //PRUEBA CON LAS EXPRESIONES PRECARGADAS (SOLO APRA GENERAR LOS AUTÓMATAS)
        String rutaCarga= adaptarRuta("../expresionesIniciales.txt"); //Archivo que contiene las expresiones iniciales para identificar futuros símbolos
        ClaseLexica miclaselex= new ClaseLexica(rutaCarga);
        ArrayList<AFN> elementosUnificacion= new ArrayList();
        String cl="";

        //Todos son AFN básicos de un caracter, excepto el de SIMB
        for(String clase: miclaselex.nombreClases()){
            if(clase.equals("SIMB")){
                String[] auxRegex= miclaselex.regexClase(clase).split("\\|");
                StringBuilder sb= new StringBuilder(auxRegex[1]);
                sb.replace(0, 1, "");
                sb.replace(sb.toString().length()-1, sb.toString().length(), "");
                String[] auxRango= sb.toString().split("-");
                
                AFN diagInv= new AFN(clase, miclaselex.tokenClase(clase));
                AFN rango1= new AFN(clase, miclaselex.tokenClase(clase));
                AFN rango2= new AFN(clase, miclaselex.tokenClase(clase));
                diagInv.crearBasico('\\');
                rango1.crearBasico((char)Integer.parseInt(auxRango[0]), (char)Integer.parseInt(auxRango[1]));
                rango2.crearBasico((char)Integer.parseInt(auxRango[0]), (char)Integer.parseInt(auxRango[1]));
                
                diagInv.concatenar(rango1);
                diagInv.unir(rango2);
                
//                System.out.println(diagInv);
                elementosUnificacion.add(diagInv);
            }
            else{
                AFN nAfn= new AFN(clase, miclaselex.tokenClase(clase));
                nAfn.crearBasico(miclaselex.regexClase(clase).charAt(1));
//                System.out.println(nAfn);
                elementosUnificacion.add(nAfn);
            }
        }
        
        AFN totalParaSemilla= AFN.unificarInicial(elementosUnificacion);
        
        AFD semilla= new AFD("Semilla", miclaselex.alfabeto());
        semilla.convertir(totalParaSemilla);
        
//        System.out.println("\n\n");
//        System.out.println(semilla);
        return semilla;
    }
    
    /*public Compilador() { 
     try { 
      int result = compile("compilador/elPrograma.java"); 
      System.out.println("javac returned " + result); 
      result = run("elPrograma"); 
     } catch (IOException | InterruptedException ex) { 
      ex.printStackTrace(); 
     } 
    } */

    public int run(String clazz) throws IOException, InterruptedException {   
     ProcessBuilder pb = new ProcessBuilder("java", clazz); 
     pb.redirectError(new File("E:\\GitHub\\Compiladores\\elError.txt")); 
     pb.directory(new File("E:/GitHub/Compiladores")); 
     Process p = pb.start(); 
     InputStreamConsumer consumer = new InputStreamConsumer(p.getInputStream()); 
     consumer.start(); 

     int result = p.waitFor(); 

     consumer.join(); 

     System.out.println(consumer.getOutput()); 

     return result; 
    } 

    public int compile(String file) throws IOException, InterruptedException {   
     ProcessBuilder pb = new ProcessBuilder("javac", file); 
     pb.redirectError(); 
     pb.directory(new File("src")); 
     Process p = pb.start(); 
     InputStreamConsumer consumer = new InputStreamConsumer(p.getInputStream()); 
     consumer.start(); 

     int result = p.waitFor(); 

     consumer.join(); 

     System.out.println(consumer.getOutput()); 

     return result;   
    } 

    public class InputStreamConsumer extends Thread { 

     private InputStream is; 
     private IOException exp; 
     private StringBuilder output; 

     public InputStreamConsumer(InputStream is) { 
      this.is = is; 
     } 

     @Override 
     public void run() { 
      int in = -1; 
      output = new StringBuilder(64); 
      try { 
       while ((in = is.read()) != -1) { 
        output.append((char) in); 
       } 
      } catch (IOException ex) { 
       ex.printStackTrace(); 
       exp = ex; 
      } 
     } 

     public StringBuilder getOutput() { 
      return output; 
     } 

     public IOException getException() { 
      return exp; 
     } 
    } 



    private static void escribir(Object afn, String ruta){
        try{
            FileOutputStream f= new FileOutputStream(new File(ruta));
            f.write(afn.toString().getBytes());
        }catch(IOException ex){
            System.out.println("Fallo al escribir autómata\n"+ex);
        }
    }
    
    private static void escribirTablaEdos(int [][] estados, ArrayList<Character> alfabeto, String ruta){
        File f= new File(ruta);
        BufferedWriter bw;
        try{
            bw= new BufferedWriter(new FileWriter(f));
            
            bw.write("\t");
            for(char s: alfabeto)
                bw.write(s+"\t");
            bw.newLine();
            for(int[] ae: estados){
                for(int e: ae)
                    bw.write(e+"\t");
                bw.newLine();
            }
                
            bw.flush();
            bw.close();
        }catch(IOException ex){
            //System.out.println("Fallo al escribir tabla\n"+ex);
        }
    }
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }    
    
}
