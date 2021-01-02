package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClaseLexica {
    private Map<String, String[]> diccionario;
    private ArrayList<Character> ALFABETO;
    private ArrayList<AFN> elementosUnificacion;
    
    public ClaseLexica(String ruta){ //Cambiar parámmetro a File con GUI
        diccionario= new HashMap();
        ALFABETO= new ArrayList();
        elementosUnificacion= new ArrayList();
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(ruta)));
            
            String str="";
            while((str= br.readLine()) != null){
                String[] div= str.split("sep"); //Separar con 
                diccionario.put(div[0], new String[]{div[2], div[1]});
            }
                
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        hacerAlfabeto();
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
//                    //De paso, crear el AFN básico del rango encontrado
//                    AFN basicoRango= new AFN(clase, tokenClase(clase));
//                    basicoRango.crearBasico( (char)lims[0], (char)lims[1] );
////                    if(operacion.getFirst()=='&'){
////                        listaBasicos.getFirst().concatenar(basicoRango);
////                        operacion.pop();
////                    }
////                    else if(operacion.getFirst()=='|'){
////                        listaBasicos.getFirst().unir(basicoRango);
////                        operacion.pop();
////                    }
////                    else
//                        listaBasicos.push(basicoRango);
////                            listaBasicos.add( String.valueOf((char)lims[0])+String.valueOf((char)lims[1]) );    

                    i=j;
                }
                
                //Caso de la diagonal invertida
                else if(expPartes[i]=='\\'){ //Si encuentra una diagonal invertida,
                    if(!ALFABETO.contains(expPartes[i+1])){
                        ALFABETO.add(expPartes[i+1]); //el siguiente caracter es parte del alfabeto
                    
//                        //De paso, crear el AFN básico del símbolo especificado con \
//                        AFN basicoDiagInv= new AFN(clase, tokenClase(clase));
//                        basicoDiagInv.crearBasico( expPartes[i+1] );
////                        if(operacion.getFirst()=='&'){
////                            listaBasicos.getFirst().concatenar(basicoDiagInv);
////                            operacion.pop();
////                        }
////                        else if(operacion.getFirst()=='|'){
////                            listaBasicos.getFirst().unir(basicoDiagInv);
////                            operacion.pop();
////                        }
////                        else
//                            listaBasicos.push(basicoDiagInv);
////                        listaBasicos.add(expPartes[i+1]);
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
                        
//                            //De paso, crear el AFN básico del símbolo
//                            AFN basico= new AFN(clase, tokenClase(clase));
//                            basico.crearBasico( expPartes[i] );
////                            if(operacion.getFirst()=='&'){
////                                listaBasicos.getFirst().concatenar(basico);
////                                operacion.pop();
////                            }
////                            else if(operacion.getFirst()=='|'){
////                                listaBasicos.getFirst().unir(basico);
////                                operacion.pop();
////                            }
////                            else
//                                listaBasicos.push(basico);
////                            listaBasicos.add(expPartes[i]);
                        }
                    }
                    //Caso de los operadores (considera *, +, ?, | y &)
                    else{
                        
                        
//                        switch(expPartes[i]){
//                            case '+':
//                                listaBasicos.getFirst().cTransitiva();
//                                break;
//                                
//                            case '*':
//                                listaBasicos.getFirst().cEstrella();
//                                break;
//                                
//                            case '?':
//                                listaBasicos.getFirst().opcional();
//                                break;
//                                
//                            case '&':
//                                operacion.push(expPartes[i]);
//                                break;
//                                
//                            case '|':
//                                operacion.push(expPartes[i]);
//                                break;
//                                
//                            case '(':
//                                operacion.push(expPartes[i]);
//                                break;
//                                
//                            case ')':
//                                operacion.pop(); //Sacar el paréntesis izquierdo      
//                                break;
//                        }
                        
                        
                    }
                }

                i+=1;
            }
            
//            if(!operacion.isEmpty()){
//                AFN pend= listaBasicos.pop();
//                if(operacion.getFirst()=='&')
//                    listaBasicos.getFirst().concatenar(pend);
//                else if(operacion.getFirst()=='|')
//                    listaBasicos.getFirst().unir(pend);
//                
//                elementosUnificacion.add(pend);
//            }
//            else{
//                elementosUnificacion.add(listaBasicos.pop());
//            }
            
            
            
        }
        
        try{
            FileWriter fw= new FileWriter(new File("..\\sumadre.txt"));
            for(char c: ALFABETO){
                fw.append(c);
                fw.append('\n');
            }
            fw.close();
        }catch(IOException ex){
            System.out.println("error");
        }
    }
    
//    public LinkedList<AFN> afnBasicos(){
//        return listaBasicos;
//    }
    
    public ArrayList<Character> alfabeto(){
        return ALFABETO;
    }
    
}
