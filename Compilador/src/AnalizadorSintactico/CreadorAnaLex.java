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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CreadorAnaLex {
//    private LinkedList<String> ladoIzquierdo;
//    private LinkedList<String> ladoDerecho;
    private boolean flagTerminal=false; //true: terminal, false: no terminal
    //private LinkedHashMap< String, Map<Integer, LinkedList<String>> > gramLeida;
    private LinkedHashMap< String, LinkedList<Object[]> > gramLeida;
    private int numeroRegla=0; //Indica la cantidad de reglas que hay
    
    public CreadorAnaLex(String ruta){
        gramLeida= new LinkedHashMap();
        mapearGramatica(ruta);
        
        int nr=0;
        String r="";
        boolean term=false;
        for(String llave: gramLeida.keySet()){
            System.out.println(llave);
            LinkedList<Object[]> lista= gramLeida.get(llave);
            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);
                nr= (int)ao[0];
                r= (String)ao[1];
                term= (boolean)ao[2];
                System.out.print("\t"+nr);
                System.out.print("\t"+r);
                System.out.println("\t"+term);
            }
        }
    }
    
    private void mapearGramatica(String ruta){
        numeroRegla=1;
        boolean elBool;
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(adaptarRuta(ruta))));
            
            String str="";
            while((str= br.readLine()) != null){
                str.replace(" ", "");
                String[] splitline= str.split("->"); //Separar ambos lados
                String[] ladosDerechos= splitline[1].split("\\|"); //Separar lados derechos
                
                LinkedList<Object[]> auxList= new LinkedList();
                
                for(String laDer: ladosDerechos){
                    Object[] mapa= new Object[3]; //Integer, String
                    mapa[0]= numeroRegla;
                    mapa[1]= laDer.trim();
                    mapa[2]= esTerminal(laDer.trim().charAt(0));
                    auxList.add(mapa);
                    numeroRegla+=1;
                }
                gramLeida.put(splitline[0], auxList);
                
            } 
            firstest();
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private boolean esTerminal(char c){
        return !((int)c >= 65 && (int)c <= 90);
    }
    
    private HashSet<String> first(String nodo){
        HashSet<String> R=null;
        String aux;
        
        if( esTerminal(nodo.charAt(0)) || (int)nodo.charAt(0)==400 ){ //400 es el Unicode de épsilon
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
    
    private String firstest(){        
        
        StringBuilder sb= new StringBuilder("");
        for(String llave: gramLeida.keySet()){
            LinkedList<Object[]> lista= gramLeida.get(llave);
            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);


                    if((boolean)ao[2]){
                        String regla= (String)ao[1];
                        
                        //Obtener solo operadores
                        for(int j=0; j<regla.toCharArray().length; j++){
                            if(esTerminal(regla.charAt(j))){
                                sb.append(regla.charAt(j));
                            }
                            else
                                break;
                        }
                        System.out.println("Regla #"+(int)ao[0]+" -> "+sb.toString());
                        if(sb.toString().equalsIgnoreCase("ep")){
                            System.out.println("Entrar al follow");
                            //Cuchau :v
                            
                        }else{
                            System.out.println("Entratr al first");
                        }
                        sb.delete(0, sb.length());
                    }
            }
            sb.delete(0, sb.length());
        }
        
        
        return "";
    }
}
