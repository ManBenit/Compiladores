package pruebas;

//Este ejemplo crea los autómatas de las clases IDENT, INT, FLOAT, MAS, PROD, TAB
//vistas en el ejercicio que dejó de tarea hace un tiempo

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import AnalizadorLexico.ClaseLexica;
import AnalizadorLexico.ClaseLexicaPrueba;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Ejemplo {
    private AFD afd;

    public void verEjemplo(){
        String rutaCarga= adaptarRuta("../DescriptorClases/claslex.txt");
        ArrayList<AFN> elementosUnificacion= new ArrayList();
        ClaseLexica miclaselex= new ClaseLexica(rutaCarga);
        String cl="";

        cl= miclaselex.nombreClases()[0];
        AFN a1= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a2= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a3= new AFN(cl, miclaselex.tokenClase(cl));
        a1.crearBasico('L');
        a2.crearBasico('D');
        a1.unir(a2);
        a1.cEstrella();
        a3.crearBasico('L');
        a3.concatenar(a1);
        elementosUnificacion.add(a3);
        //System.out.println(a3);
        //System.out.println("");
        
        cl= miclaselex.nombreClases()[1];
        AFN a4= new AFN(cl, miclaselex.tokenClase(cl));
        a4.crearBasico('D');
        a4.cTransitiva();
        elementosUnificacion.add(a4);
        //System.out.println(a4);
        //System.out.println("");
        
        cl= miclaselex.nombreClases()[2];
        AFN a5= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a6= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a7= new AFN(cl, miclaselex.tokenClase(cl));
        a5.crearBasico('D');
        a6.crearBasico('.');
        a7.crearBasico('D');
        a5.cTransitiva();
        a7.cTransitiva();
        a5.concatenar(a6);
        a5.concatenar(a7);
        elementosUnificacion.add(a5);
        //System.out.println(a5);
        //System.out.println("");
        
        cl= miclaselex.nombreClases()[3];
        AFN a8= new AFN(cl, miclaselex.tokenClase(cl));
        a8.crearBasico('M');
        elementosUnificacion.add(a8);
        //System.out.println(a8);
        //System.out.println("");
        
        cl= miclaselex.nombreClases()[4];
        AFN a9= new AFN(cl, miclaselex.tokenClase(cl));
        a9.crearBasico('P');
        elementosUnificacion.add(a9);
        //System.out.println(a9);
        //System.out.println("");
        
        cl= miclaselex.nombreClases()[5];
        AFN a10= new AFN(cl, miclaselex.tokenClase(cl));
        AFN a11= new AFN(cl, miclaselex.tokenClase(cl));
        a10.crearBasico('E');
        a11.crearBasico('T');
        a10.unir(a11);
        a10.cTransitiva();
        elementosUnificacion.add(a10);
        //System.out.println(a10);
        //System.out.println("");
        
        
        AFN total= AFN.unificarInicial(elementosUnificacion);
        escribir(total, adaptarRuta("../AFN.txt"));
        
        afd= new AFD("Tarea autómata", miclaselex.alfabeto());
        afd.convertir(total);
        escribir(afd, adaptarRuta("../AFD.txt"));
        
        
        escribirTablaEdos(afd.tablaDeEstados(), afd.alfabeto(), adaptarRuta("../TablaAFD.txt"));
        
    }
    
    public void ejemploRango(){
        String rutaCarga= adaptarRuta("../gramatest.txt");
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
        escribir(total, adaptarRuta("../AFN.txt"));
        
        afd= new AFD("Tarea autómata", miclaselex.alfabeto());
        afd.convertir(total);
        escribir(afd, adaptarRuta("../AFD.txt"));
        
        
        escribirTablaEdos(afd.tablaDeEstados(), afd.alfabeto(), adaptarRuta("../TablaAFD.txt"));
        
        
        
        
        ///////////////// T E S T //////////////////////////////////////////
        
//        a1.crearBasico('L');
//        a2.crearBasico('D');
//        a1.unir(a2);
//        a1.cEstrella();
//        a3.crearBasico('L');
//        a3.concatenar(a1);
//        elementosUnificacion.add(a3);
//        //System.out.println(a3);
//        //System.out.println("");
//        
//        AFN a4= new AFN(ClaseLexica.nombreClases()[4]);
//        a4.crearBasico('D');
//        a4.cTransitiva();
//        elementosUnificacion.add(a4);
//        //System.out.println(a4);
//        //System.out.println("");
//        
//        AFN a5= new AFN(ClaseLexica.nombreClases()[0]);
//        AFN a6= new AFN(ClaseLexica.nombreClases()[0]);
//        AFN a7= new AFN(ClaseLexica.nombreClases()[0]);
//        a5.crearBasico('D');
//        a6.crearBasico('.');
//        a7.crearBasico('D');
//        a5.cTransitiva();
//        a7.cTransitiva();
//        a5.concatenar(a6);
//        a5.concatenar(a7);
//        elementosUnificacion.add(a5);
//        //System.out.println(a5);
//        //System.out.println("");
//        
//        AFN a8= new AFN(ClaseLexica.nombreClases()[5]);
//        a8.crearBasico('M');
//        elementosUnificacion.add(a8);
//        //System.out.println(a8);
//        //System.out.println("");
//        
//        AFN a9= new AFN(ClaseLexica.nombreClases()[1]);
//        a9.crearBasico('P');
//        elementosUnificacion.add(a9);
//        //System.out.println(a9);
//        //System.out.println("");
//        
//        AFN a10= new AFN(ClaseLexica.nombreClases()[3]);
//        AFN a11= new AFN(ClaseLexica.nombreClases()[3]);
//        a10.crearBasico('E');
//        a11.crearBasico('T');
//        a10.unir(a11);
//        a10.cTransitiva();
//        elementosUnificacion.add(a10);
//        //System.out.println(a10);
//        //System.out.println("");
//        
//        
//        AFN total= AFN.unificarInicial(elementosUnificacion);
//        escribir(total, adaptarRuta("../AFN.txt"));
//        
//        afd= new AFD("Tarea autómata");
//        afd.convertir(total);
//        escribir(afd, adaptarRuta("../AFD.txt"));
//        
//        
//        escribirTablaEdos(afd.tablaDeEstados(), afd.alfabeto(), adaptarRuta("../TablaAFD.txt"));
    }
    
    public void pruebaGeneradorAfn(){
        String rutaCarga= adaptarRuta("../gramatest.txt");
        ClaseLexica miclaselex= new ClaseLexica(rutaCarga);
        for(String s: miclaselex.nombreClases())
            System.out.println(s+"\t"+miclaselex.regexClase(s));
//        for(AFN f: miclaselex.afnBasicos())
//            System.out.println(f);
        ArrayList<AFN> elementosUnificacion= new ArrayList();
        String cl="";
        
    }
    
    public AFD afd(){
        return afd;
    }
    
    
    
    
    private void escribir(Object afn, String ruta){
        try{
            FileOutputStream f= new FileOutputStream(new File(ruta));
            f.write(afn.toString().getBytes());
        }catch(IOException ex){
            System.out.println("Fallo al escribir autómata\n"+ex);
        }
    }
    
    private void escribirTablaEdos(int [][] estados, ArrayList<Character> alfabeto, String ruta){
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
    
    private String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
    
}
