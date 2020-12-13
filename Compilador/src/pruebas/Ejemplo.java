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
        ClaseLexicaPrueba.cargarClasesLexicas(rutaCarga);
        ArrayList<AFN> elementosUnificacion= new ArrayList();

        AFN a1= new AFN(ClaseLexicaPrueba.obtNomClases()[0]);
        AFN a2= new AFN(ClaseLexicaPrueba.obtNomClases()[0]);
        AFN a3= new AFN(ClaseLexicaPrueba.obtNomClases()[0]);
        a1.crearBasico('L');
        a2.crearBasico('D');
        a1.unir(a2);
        a1.cEstrella();
        a3.crearBasico('L');
        a3.concatenar(a1);
        elementosUnificacion.add(a3);
        //System.out.println(a3);
        //System.out.println("");
        
        AFN a4= new AFN(ClaseLexicaPrueba.obtNomClases()[1]);
        a4.crearBasico('D');
        a4.cTransitiva();
        elementosUnificacion.add(a4);
        //System.out.println(a4);
        //System.out.println("");
        
        AFN a5= new AFN(ClaseLexicaPrueba.obtNomClases()[2]);
        AFN a6= new AFN(ClaseLexicaPrueba.obtNomClases()[2]);
        AFN a7= new AFN(ClaseLexicaPrueba.obtNomClases()[2]);
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
        
        AFN a8= new AFN(ClaseLexicaPrueba.obtNomClases()[3]);
        a8.crearBasico('M');
        elementosUnificacion.add(a8);
        //System.out.println(a8);
        //System.out.println("");
        
        AFN a9= new AFN(ClaseLexicaPrueba.obtNomClases()[4]);
        a9.crearBasico('P');
        elementosUnificacion.add(a9);
        //System.out.println(a9);
        //System.out.println("");
        
        AFN a10= new AFN(ClaseLexicaPrueba.obtNomClases()[5]);
        AFN a11= new AFN(ClaseLexicaPrueba.obtNomClases()[5]);
        a10.crearBasico('E');
        a11.crearBasico('T');
        a10.unir(a11);
        a10.cTransitiva();
        elementosUnificacion.add(a10);
        //System.out.println(a10);
        //System.out.println("");
        
        
        AFN total= AFN.unificarInicial(elementosUnificacion);
        escribir(total, adaptarRuta("../AFN.txt"));
        
        afd= new AFD("Tarea autómata");
        afd.convertir(total);
        escribir(afd, adaptarRuta("../AFD.txt"));
        
        
        escribirTablaEdos(afd.tablaDeEstados(), afd.alfabeto(), adaptarRuta("../TablaAFD.txt"));
        
    }
    
    public void ejemploRango(){
        String rutaCarga= adaptarRuta("../gramatest.txt");
        ClaseLexica.cargarClasesLexicas(rutaCarga);
        /*for(char c: ClaseLexica.ALFABETO)
            System.out.print(c+", ");
        System.out.println("");*/
        ArrayList<AFN> elementosUnificacion= new ArrayList();
        /*for(Object s: ClaseLexica.nombreClases())
            System.out.print((String)s+", ");
        System.out.println("");*/

        AFN a1= new AFN((String)ClaseLexica.nombreClases()[2]);
        AFN a2= new AFN((String)ClaseLexica.nombreClases()[2]);
        AFN a3= new AFN((String)ClaseLexica.nombreClases()[2]);
        a1.crearBasico('A', 'Z');
        a2.crearBasico('0', '9');
        a1.unir(a2);
        a1.cEstrella();
        a3.crearBasico('A', 'Z');
        a3.concatenar(a1);
        elementosUnificacion.add(a3);
//        System.out.println(a3);
//        System.out.println("");
        
        AFN a4= new AFN((String)ClaseLexica.nombreClases()[4]);
        a4.crearBasico('0', '9');
        a4.cTransitiva();
        elementosUnificacion.add(a4);
//        System.out.println(a4);
//        System.out.println("");
        
        AFN a5= new AFN((String)ClaseLexica.nombreClases()[0]);
        AFN a6= new AFN((String)ClaseLexica.nombreClases()[0]);
        AFN a7= new AFN((String)ClaseLexica.nombreClases()[0]);
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
        
        AFN a8= new AFN((String)ClaseLexica.nombreClases()[5]);
        a8.crearBasico('+');
        elementosUnificacion.add(a8);
//        System.out.println(a8);
//        System.out.println("");
        
        AFN a9= new AFN((String)ClaseLexica.nombreClases()[1]);
        a9.crearBasico('*');
        elementosUnificacion.add(a9);
//        System.out.println(a9);
//        System.out.println("");
        
        AFN a10= new AFN((String)ClaseLexica.nombreClases()[3]);
        AFN a11= new AFN((String)ClaseLexica.nombreClases()[3]);
        a10.crearBasico(' ');
        a11.crearBasico((char)9);
        a10.unir(a11);
        a10.cTransitiva();
        elementosUnificacion.add(a10);
//        System.out.println(a10);
//        System.out.println("");
        
        
        AFN total= AFN.unificarInicial(elementosUnificacion);
        escribir(total, adaptarRuta("../AFN.txt"));
        
        afd= new AFD("Tarea autómata");
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
//        AFN a4= new AFN((String)ClaseLexica.nombreClases()[4]);
//        a4.crearBasico('D');
//        a4.cTransitiva();
//        elementosUnificacion.add(a4);
//        //System.out.println(a4);
//        //System.out.println("");
//        
//        AFN a5= new AFN((String)ClaseLexica.nombreClases()[0]);
//        AFN a6= new AFN((String)ClaseLexica.nombreClases()[0]);
//        AFN a7= new AFN((String)ClaseLexica.nombreClases()[0]);
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
//        AFN a8= new AFN((String)ClaseLexica.nombreClases()[5]);
//        a8.crearBasico('M');
//        elementosUnificacion.add(a8);
//        //System.out.println(a8);
//        //System.out.println("");
//        
//        AFN a9= new AFN((String)ClaseLexica.nombreClases()[1]);
//        a9.crearBasico('P');
//        elementosUnificacion.add(a9);
//        //System.out.println(a9);
//        //System.out.println("");
//        
//        AFN a10= new AFN((String)ClaseLexica.nombreClases()[3]);
//        AFN a11= new AFN((String)ClaseLexica.nombreClases()[3]);
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
