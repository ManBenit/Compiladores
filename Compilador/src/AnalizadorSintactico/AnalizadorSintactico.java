package AnalizadorSintactico;

import AnalizadorLexico.AnalizadorLexico;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class AnalizadorSintactico extends Thread{
    private String[][] tablaLL1;
    //private LinkedList<String> pila;
    private LinkedList<Integer> pila;
    private AnalizadorLexico lexic;
    
    public AnalizadorSintactico(String rutaGramatica, AnalizadorLexico al){
        pila= new LinkedList();
        CreadorAnaLex cargaGram= new CreadorAnaLex(adaptarRuta(rutaGramatica));
        tablaLL1= cargaGram.tablaLL1();
        lexic=al;
    }
    
    @Override
    public void run(){
        pedirToken(); //??????
    }
    
    public void pedirToken(){
        //alguna cosa cuando el token no sea el que se esperaba
        //entra la gramática
        int token=0;
        while(true){
            if(lexic.encontrado()){
                token=lexic.yylex();
                System.out.println("tok= "+token);
                break; //(Realizar analisis sintáctico)
            }
            System.out.println("fsdf");
        }
    }
    
    public void pedirLexema(){
        
    }
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
