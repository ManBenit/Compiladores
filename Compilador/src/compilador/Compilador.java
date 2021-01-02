package compilador;

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.ClaseLexica;
import AnalizadorLexico.Estado;
import AnalizadorLexico.Transicion;
import AnalizadorSintactico.CreadorAutomatas;
import GUI.Monitor;
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
        //p.pruebaGeneradorAfn();
        
        AnalizadorLexico lexic= new AnalizadorLexico(p.afd());
        //String cad= "DD.DDTTLLDEMEEP";//DD.DDTTLLDEMEEP
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
        
        
        new Monitor().setVisible(true);
        //CreadorAutomatas ca= new CreadorAutomatas("../gramatest.txt");
        
    }    
    
}
