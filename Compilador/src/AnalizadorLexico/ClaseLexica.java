package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaseLexica {
    private static final Map<String, String[]> diccionario= new HashMap();
    public static final ArrayList<Character> ALFABETO= new ArrayList<>();
    
    public static void cargarClasesLexicas(String ruta){ //Cambiar parámmetro a File con GUI
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(ruta)));
            
            String str="";
            while((str= br.readLine()) != null){
                String[] div= str.split(String.valueOf((char)9)); //Separar con tabulador
                diccionario.put(div[0], new String[]{div[2], div[1]});
            }
                
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        hacerAlfabeto();
    }
    
    public static int tokenClase(String claseLexica){
        //Posición 2 del diccionario
        return Integer.parseInt( diccionario.get(claseLexica)[0] );
    }
    
    public static Object[] nombreClases(){
        //Posición 0 del diccionario
        return diccionario.keySet().toArray();
    }
    
    public static String regexClase(String claseLexica){
        //Posición 1 del diccionario
        return diccionario.get(claseLexica)[1];
    }
    
    private static void hacerAlfabeto(){
        for(Object clase: nombreClases()){
            String s= regexClase((String)clase);//[0-9]+.([0-9]\\+[0-9])
//            System.out.println(s);
            char[] expPartes= s.toCharArray();
            
            int i=0;
            while(i<expPartes.length){
                //Caso de un rango
                if(expPartes[i]=='['){ 
                    int[] lims= new int[3]; //limInf, limSup
                    int pos=0;
                    int j=i+1;
                    while(expPartes[j]!=']'){
                        if(expPartes[j]!='-'){
                            lims[pos]=(int)expPartes[j];
                            pos+=1;
                        }
                        j+=1;
                    }

                    for(int a=lims[0]; a<=lims[1]; a++)
                        if(!ALFABETO.contains((char)a))
                            ALFABETO.add((char)a);

                    i=j+1;
                }
                
                //Caso de la diagonal invertida
                else if(expPartes[i]=='\\'){ //Si encuentra una diagonal invertida,
                    if(!ALFABETO.contains(expPartes[i+1]))
                        ALFABETO.add(expPartes[i+1]); //el siguiente caracter es parte del alfabeto
                    i+=2; //y saltas lo que ya agregaste
                }
                
                else{
                    //Caso de símbolos distintos a operadores
                    if(
                            expPartes[i]!='*' && 
                            expPartes[i]!='+' &&
                            expPartes[i]!='?' &&
                            expPartes[i]!='(' &&
                            expPartes[i]!=')' &&
                            expPartes[i]!='|' 
                      ){
                        if(!ALFABETO.contains(expPartes[i]))
                            ALFABETO.add(expPartes[i]);
                    } 
                }

                i+=1;
            }
        }
    }
    
}
