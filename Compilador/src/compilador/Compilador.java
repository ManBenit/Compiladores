package compilador;

import AnalizadorLexico.AFD;
import AnalizadorLexico.ClaseLexica;
import pruebas.Ejemplo;


public class Compilador {

    public static void main(String[] args) {
        //Pruebas con la tarea realizada casi al inicio del curso
        Ejemplo p= new Ejemplo();
        p.verEjemplo();
        
        AFD afd= p.afd();
        String cad= "asumadre h";
        afd.validarCadena(cad);
        /////////////////////////////////////////////////////////
        
        //Carga automática de alfabeto
        ClaseLexica cl= new ClaseLexica();
        cl.cargarGramatica(adaptarRuta("../gramatest.txt"));
        
    }    
    
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
