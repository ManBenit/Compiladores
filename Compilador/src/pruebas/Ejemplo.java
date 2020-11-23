package pruebas;

//Este ejemplo crea los autómatas de las clases IDENT, INT, FLOAT, MAS, PROD, TAB
//vistas en el ejercicio que dejó de tarea hace un tiempo

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import AnalizadorLexico.ClaseLexica;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Ejemplo {
    public static void main(String[] args){
        String rutaCarga= adaptarRuta("../DescriptorClases/claslex.txt");
        ClaseLexica.cargarClasesLexicas(rutaCarga);
        ArrayList<AFN> elementosUnificacion= new ArrayList();

        AFN a1= new AFN(ClaseLexica.obtNomClases()[0]);
        AFN a2= new AFN(ClaseLexica.obtNomClases()[0]);
        AFN a3= new AFN(ClaseLexica.obtNomClases()[0]);
        a1.crearBasico('L');
        a2.crearBasico('D');
        a1.unir(a2);
        a1.cEstrella();
        a3.crearBasico('L');
        a3.concatenar(a1);
        elementosUnificacion.add(a3);
        System.out.println(a3);
        System.out.println("");
        
        AFN a4= new AFN(ClaseLexica.obtNomClases()[1]);
        a4.crearBasico('D');
        a4.cTransitiva();
        elementosUnificacion.add(a4);
        System.out.println(a4);
        System.out.println("");
        
        AFN a5= new AFN(ClaseLexica.obtNomClases()[2]);
        AFN a6= new AFN(ClaseLexica.obtNomClases()[2]);
        AFN a7= new AFN(ClaseLexica.obtNomClases()[2]);
        a5.crearBasico('D');
        a6.crearBasico('.');
        a7.crearBasico('D');
        a5.cTransitiva();
        a7.cTransitiva();
        a5.concatenar(a6);
        a5.concatenar(a7);
        elementosUnificacion.add(a5);
        System.out.println(a5);
        System.out.println("");
        
        AFN a8= new AFN(ClaseLexica.obtNomClases()[3]);
        a8.crearBasico('M');
        elementosUnificacion.add(a8);
        System.out.println(a8);
        System.out.println("");
        
        AFN a9= new AFN(ClaseLexica.obtNomClases()[4]);
        a9.crearBasico('P');
        elementosUnificacion.add(a9);
        System.out.println(a9);
        System.out.println("");
        
        AFN a10= new AFN(ClaseLexica.obtNomClases()[5]);
        AFN a11= new AFN(ClaseLexica.obtNomClases()[5]);
        a10.crearBasico('E');
        a11.crearBasico('T');
        a10.unir(a11);
        a10.cTransitiva();
        elementosUnificacion.add(a10);
        System.out.println(a10);
        System.out.println("");
        
        
        AFN total= AFN.unificarInicial(elementosUnificacion);
        escribir(total, adaptarRuta("../AFN.txt"));
        
        AFD afd= new AFD("Tarea autómata");
        afd.convertir(total);
        escribir(afd, adaptarRuta("../AFD.txt"));
        
        imprimirTablaEdos(afd.tablaDeEstados(), afd.alfabeto());
        
    }
    
    private static void escribir(Object afnd, String ruta){
        try{
            FileOutputStream f= new FileOutputStream(new File(ruta));
            f.write(afnd.toString().getBytes());
        }catch(IOException ex){
            System.out.println("Fallo al escribir\n"+ex);
        }
    }
    
    private static void imprimirTablaEdos(int [][] estados, ArrayList<Character> alfabeto){
        for(char s: alfabeto)
            System.out.print(s+"\t");
        System.out.println("");
        for(int[] ae: estados){
            for(int e: ae){
                System.out.print(e+"\t");
            }
            System.out.println("");
        }
    }
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
