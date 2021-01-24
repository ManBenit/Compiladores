package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ClaseLexica {
    private LinkedHashMap<String, String[]> diccionario;
    private LinkedList<Character> ALFABETO;
    private LinkedList<AFN> elementosUnificacion;
    
    public ClaseLexica(String ruta){ //Cambiar parámmetro a File con GUI
        diccionario= new LinkedHashMap();
        ALFABETO= new LinkedList();
        elementosUnificacion= new LinkedList();
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(ruta)));
            
            String str="";
            while((str= br.readLine()) != null){
                String[] div= str.split("sep"); //Separar con 
                diccionario.put(div[0].trim(), new String[]{div[2].trim(), div[1].trim()});
            }
                
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        hacerAlfabeto();
        for(char c: alfabeto())
            System.out.print(c+", ");
        System.out.println("");
    }
    
    
    public int tokenClase(String claseLexica){
        //Posición 2 del diccionario
        return Integer.parseInt( diccionario.get(claseLexica)[0] );
    }
    
    public String[] nombreClases(){
        //Posición 0 del diccionario
        Object[] lista= diccionario.keySet().toArray();
        String[] ret= new String[lista.length];
        
        for(int i=0; i<lista.length; i++)
            ret[i]= (String)lista[i];
            
        return ret;
    }
    
    public String regexClase(String claseLexica){
        //Posición 1 del diccionario
        return diccionario.get(claseLexica)[1];
    }
    
    public String[] listaRegex(){
        String[] ret= new String[diccionario.size()];
        int index=0;
        for(String regex: nombreClases()){
            ret[index]= regexClase(regex);
            index+=1;
        }            
        return ret;
    }
    
    private void hacerAlfabeto(){
        for(String c: nombreClases()){
            String[] a= diccionario.get(c);
            System.out.println(c+" "+a[0]+" "+a[1]);
        }
        
        for(String clase: nombreClases()){
            LinkedList<Character> operacion= new LinkedList();
            LinkedList<AFN> listaBasicos= new LinkedList();
            String s= regexClase(clase);//[0-9]+.([0-9]\\+[0-9])
//            System.out.println(s);
            char[] expPartes= s.toCharArray();
            
            
            int i=0;
            while(i<expPartes.length){
                //Caso de un rango
                if(expPartes[i]=='['){
                    int[] lims= new int[2]; //limInf, limSup
                    int pos=0;
                    int j=i+1;
                    if(clase.equals("SIMB")){ //Cuando se haga el alfabeto de los autómatas,
                        StringBuilder sb= new StringBuilder("");
                        while(expPartes[j]!=']'){
                            sb.append(expPartes[j]);
                            j+=1;
                        }
                        String[] nLims= sb.toString().split("-");
                        lims[0]= Integer.parseInt(nLims[0]);//se consideran los caracteres imprimibles de ASCII
                        lims[1]= Integer.parseInt(nLims[1]);
                    }
                    else{
                        while(expPartes[j]!=']'){
                            if(expPartes[j]!='-'){
                                lims[pos]=(int)expPartes[j];
                                pos+=1;
                            }
                            j+=1;
                        }
                    }

                    for(int a=lims[0]; a<=lims[1]; a++)
                        if(!ALFABETO.contains((char)a))
                            ALFABETO.add((char)a);
                    i=j;
                }
                
                //Caso de la diagonal invertida
                else if(expPartes[i]=='\\'){ //Si encuentra una diagonal invertida,
                    if(!ALFABETO.contains(expPartes[i+1])){
                        ALFABETO.add(expPartes[i+1]); //el siguiente caracter es parte del alfabeto
                    }
                    
                    i+=1; //y saltas lo que ya agregaste
                }
                
                else{
                    //Caso de símbolos distintos a operadores
                    if(
                            expPartes[i]!='*' && 
                            expPartes[i]!='+' &&
                            expPartes[i]!='?' &&
                            expPartes[i]!='(' &&
                            expPartes[i]!=')' &&
                            expPartes[i]!='|' &&
                            expPartes[i]!='&'
                      ){
                        if(!ALFABETO.contains(expPartes[i])){
                            ALFABETO.add(expPartes[i]);
                        }
                    }
                }

                i+=1;
            }
        }
    }
    
    public LinkedList<Character> alfabeto(){
        return ALFABETO;
    }
    
}
