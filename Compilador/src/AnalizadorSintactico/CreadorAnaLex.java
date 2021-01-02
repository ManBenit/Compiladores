package AnalizadorSintactico;

import AnalizadorLexico.AFD;
import AnalizadorLexico.AFN;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class CreadorAnaLex {
//    private LinkedList<String> ladoIzquierdo;
//    private LinkedList<String> ladoDerecho;
    private boolean flagTerminal=false; //true: terminal, false: no terminal
    private Map<String, LinkedList<String>> gramLeida;
    
    public CreadorAnaLex(){
        //ladoIzquierdo= new LinkedList<>();
        //ladoDerecho= new LinkedList<>();
        gramLeida= new HashMap();
    }
    
    private void mapearGramatica(String ruta){
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(adaptarRuta(ruta))));
            
            String str="";
            while((str= br.readLine()) != null){
                str.replace(" ", "");
                String[] splitline= str.split("->"); //Separar ambos lados
                String[] ladosDerechos= splitline[1].split("|"); //Separar lados derechos
                gramLeida.put(splitline[0], new LinkedList(Arrays.asList(ladosDerechos)));
            } 
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private boolean esTerminal(char c){
        return !( (int)c >= 65 && (int)c <= 90 );
    }
    
    private HashSet<String> first(String nodo){
        HashSet<String> R=null;
        String aux;
        
        if( esTerminal(nodo.charAt(0)) || (int)nodo.charAt(0)==400 ){
            R.add(nodo);
            return R;
        }
        return null;
    }
 
    private String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
