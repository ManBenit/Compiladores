package compilador;

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import AnalizadorLexico.ClaseLexica;
import AnalizadorLexico.Estado;
import AnalizadorLexico.Transicion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pruebas.Ejemplo;


public class Compilador {

    public static void main(String[] args) {
        //Prueba con la tarea realizada casi al inicio del curso
        Ejemplo p= new Ejemplo();
////        p.verEjemplo();
//
//        //Prueba con rango de caracteres
        p.ejemploRango();
        
//        
        AFD afd= p.afd();
//        String cad= "D.D.";
        //String cad= "DD.DDTTLLDEMEEP";//DD.DDTTLLDEMEEP
//        String cad= "222pq";//2+ | [p-s]
//        if(afd.validarCadena(cad))
//            System.out.println("CADENA VÁLIDA");
//        else
//            System.out.println("CADENA INVÁLIDA");
        
        
        /////////////////////////////////////////////////////////
        
        
        
        //Carga automática de alfabeto
//        ClaseLexica cl= new ClaseLexica();
//        cl.cargarClasesLexicas(adaptarRuta("../gramatest.txt"));

//        Estado edo1= new Estado(true, false, 0);
//        Estado edo2= new Estado(false, false, 0);
//        Estado edo3= new Estado(false, false, 0);
//        Estado edo4= new Estado(false, true, 0);
//        
//        edo1.agregarTransicion('c', edo2);
//        edo1.agregarTransicion(edo3);
//        edo1.agregarTransicion('a', 'h', edo4);
//        
//        
//        System.out.println(edo1);
//        for(Transicion t: edo1.obtTransiciones())
//            System.out.println(t);
        
        
        
    }    
    
    
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
