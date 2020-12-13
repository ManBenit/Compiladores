package compilador;

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import AnalizadorLexico.ClaseLexica;
import AnalizadorLexico.Estado;
import AnalizadorLexico.Transicion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import pruebas.Ejemplo;


public class Compilador {

    public static void main(String[] args) {
        //Prueba con la tarea realizada casi al inicio del curso
        Ejemplo p= new Ejemplo();
        p.ejemploRango();
        
        
        AFD afd= p.afd();
        //String cad= "DD.DDTTLLDEMEEP";//DD.DDTTLLDEMEEP
        String cad= "12.34      AB5 +  *";
        if(afd.validarCadena(cad))
            System.out.println("CADENA VÁLIDA");
        else
            System.out.println("CADENA INVÁLIDA");
        
        int token=0;
        try{
            while((token=afd.yylex())!=0){
                System.out.println(afd.yytex()+", token "+token);
            }
        }catch(NoSuchElementException ex){
            System.out.println("Todo leído");
        }
    }    
    
    
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
