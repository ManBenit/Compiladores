package AnalizadorSintactico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CreadorAnaLex {
    private LinkedHashMap< String, LinkedList<Object[]> > gramLeida;
    private int numeroRegla=0; //Indica la cantidad de reglas que hay
    private String[][] tablaLL1;
    private HashSet<String> alfaGram; //"alfabeto" de la gramática (obtener los símbolos necesarios para la tabla LL1)
    
    public CreadorAnaLex(String ruta){
        gramLeida= new LinkedHashMap();
        mapearGramatica(ruta);
//        cargarSimbolos(); //Para hacer tabla LL1
//        for(String x: alfaGram)
//            System.out.println(x);
        
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
                gramLeida.put(splitline[0].trim(), auxList);
                
            } 
            firstest(); //debemos quitar esta invocación de aquí
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void cargarSimbolos(){
        alfaGram= new HashSet<>(); //Alfabeto de la gramática
        StringBuilder sb= new StringBuilder();
        
        for(String llave: gramLeida.keySet()){
            LinkedList<Object[]> lista= gramLeida.get(llave);
            char[] charArray;
            int aux;
            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);
                charArray= ((String)ao[1]).toCharArray();
                for(char c: charArray)
                    System.out.print(c+", ");
                System.out.println("");
                
                for(int j=0; j<charArray.length; j++){
                    System.out.println("aux"+j);
                    if(esTerminal(charArray[j]) && charArray[j]!='\''){
                        System.out.println("entrado");
                        if((int)charArray[j]>=97 && (int)charArray[j]<=122){
                            System.out.println("esmin");
                            aux= j;
                            System.out.println("j="+j);
                            System.out.println("char0"+charArray[aux]);
                            while((int)charArray[aux]>=97 && (int)charArray[aux]<=122){
                                sb.append(charArray[aux]);
                                aux+=1;
                            }
                            j= aux;
                            alfaGram.add(sb.toString());
                            sb.delete(0, sb.length());
                        }
                        else
                            alfaGram.add(String.valueOf(charArray[j]));
                    }
                    else
                        System.out.println("nel karnal: "+charArray[j]);
                }
            }
        }
    }
    
    private String[] first(String nodo){
        int tam= nodo.toCharArray().length;
        String[] retFirst= new String[25];
        int posRet=0; //Para asignar las posiciones de retFirst
        StringBuilder sb= new StringBuilder("");
        
        if(esTerminal(nodo.charAt(0))){ //Caso donde el primer símbolo (de 1 o más caracteres) es un terminal
            for(int j=0; j<nodo.toCharArray().length; j++){
                if(esTerminal(nodo.charAt(j))){
                    sb.append(nodo.charAt(j));
                }
                else
                    break;
            }
            retFirst[posRet]=sb.toString();
        }
        else{ //Caso donde se busca dentro de las reglas, el first del símbolo hasta llegar a un terminal
            LinkedList<Object[]> lista;
            if(tam==2 && nodo.charAt(1)=='\'') //Lado izquierdo de un solo caracter (X), X=cualquier no terminal
                lista= gramLeida.get(nodo);
            else
                lista= gramLeida.get( String.valueOf(nodo.charAt(0)) );

            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);

                char primerSim= ((String)ao[1]).charAt(0);
                if( esTerminal(primerSim) ){

                    //Ciclo por si a caso se trata de un símbolo de varios caracteres
                    for(int j=0; j<((String)ao[1]).toCharArray().length; j++){
                        if(esTerminal(((String)ao[1]).charAt(j))){
                            //Si es minúscula, es símbolo de varios caracteres, entonces concatena todos esos caracteres
                            if( (int)((String)ao[1]).charAt(j)>=97 && (int)((String)ao[1]).charAt(j)<=122 )
                                sb.append(((String)ao[1]).charAt(j));
                            else{ //Si es un caracter diferente a minúsculas, obtenlo y sal del ciclo
                                sb.append(((String)ao[1]).charAt(j)); //Solo lleja a j=0, es decir, solo el primer caracter
                                break;
                            }
                        }
                    }
                    retFirst[posRet]= String.valueOf(sb.toString());
                    posRet+=1;
                    sb.delete(0, sb.length());
                }
                else{
                    retFirst= first(String.valueOf(primerSim));
                }
            }
        }
        
        return retFirst;
    }
    
    private HashSet<String> follow(String A){
        HashSet<String> R= new HashSet<>();
        String[] aux;
        String[] auxArray;
        Object[] N=null;//Modificar
        for(int i=1; i<numeroRegla-1; i++){
            N= buscarSimbolo(A, i); //revLista.get(j);
            if(N!=null){
                auxArray= ((String)N[1]).split(A);
                if(auxArray.length==1){ //Si es el último símbolo de la regla
                    System.out.println("simon karnal");
                    if(A.equals( obtLlavePorLadDer( (String)N[1] ) ))//ArrReglas[i].strSimb
                        continue;
                    R.addAll(follow( obtLlavePorLadDer( (String)N[1] ) ));
                }
                else{
                    aux= first(auxArray[1]); //Siguiente símbolo del símbolo analizado (A)
                    for(String auxF: aux){
                        if(auxF!=null){
                            if(auxF.contains("ep")){
                                auxF= auxF.replace("ep", "\u0190");
                                R.add(auxF);
                                if(!A.equals(aux))
                                    R.addAll(follow(auxF));
                            }
                            else
                                R.add(auxF);
                        }
                    }
                }
            }
        }
        
        return R;
    }
    
    private String firstest(){    
//        Object[] o= buscarSimbolo("ep", 8); // 1    TE' false
//        System.out.print("elNodo:\t");
//        System.out.print((int)o[0]);
//        System.out.print("\t"+(String)o[1]);
//        System.out.println("\t"+(boolean)o[2]);

//        //for(int i=0; i<numeroRegla-1; i++){
//            String encontrado= buscarIzqEnDer("T'", 5);
//            if(!encontrado.equals(""))
//                System.out.println(encontrado);
//        //}
        
//        System.out.println(obtLlavePorLadDer("FT'"));
        
//        for(String s: first("F"))
//            if(s!=null)
//                System.out.println("elFirst: "+s);

        for(String s: follow("F"))
            System.out.println("elFollow: "+s);

        
        return "";
    }
    
    private boolean esTerminal(char c){
        return !((int)c >= 65 && (int)c <= 90);
    }
    
    //Devuelve el lado derecho que contenga a 'buscar' en la regla numReg
    private String buscarIzqEnDer(String buscar, int numReg){
        Object[] aux= buscarSimbolo(buscar, numReg);
        if( aux==null )
            return "";
        else
            return (String) aux[1];
    }
    
    private String obtLlavePorLadDer(String ladDer){
        //obtIzqPorDer, con Der String, sin saber numRegla (obtener la llave de Der)
        String llave="";
        int in=0;
        for(String izq: gramLeida.keySet()){
            LinkedList<Object[]> lista= gramLeida.get(izq);
            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);
                if( ((String)ao[1]).equals(ladDer) ){
                    llave= izq;
                    in=numeroRegla-1;
                    break;
                }
            }
            if(in==numeroRegla-1)
                break;
            in+=1;
        }
        
        return llave;
    }
    
    //Buscar el símbolo (llave) "entra" en la regla número numreg
    private Object[] buscarSimbolo(String entra, int numreg){
        int tam= entra.toCharArray().length;
        boolean contenido;
        
        Object[] retNodo=null;
        int auxIndex=0;
        for(String llave: gramLeida.keySet()){
            LinkedList<Object[]> lista= gramLeida.get(llave);
            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);
                
                String regla= (String)ao[1];
                int num= (int)ao[0];
                
                if(tam==2) //si tiene prima, solo confirma si contiene la cadena
                    contenido= regla.contains(entra);
                else //si no contiene prima, verifica que lo contenga y además que el siguiente caracter no sea la prima
                    contenido= regla.contains(entra) && regla.charAt( regla.indexOf(entra)+1 )!='\'';
                
                if(contenido && num==numreg){
                    //Como X' (X= cualquier no terminal) está siempre al final, puede darse el caso donde entra=X, por lo que
                    //se detectaría que contains es true si la regla es alfaX', eso implica un error, así que si
                    //entra=X cuando la regla es alfaX', hay que ignorar el índice del apóstrofe
                    retNodo= ao;
                    auxIndex= gramLeida.size()-1;
                    break;
                }
            }
            
            if(auxIndex==gramLeida.size()-1)
                break;
            
            auxIndex+=1;
        }
        return retNodo;
    }
    
    private String obtReglaPorNumero(int numeroDeRegla){
        String regla="";
        
        for(String llave: gramLeida.keySet()){
            LinkedList<Object[]> lista= gramLeida.get(llave);
            for(int i=0; i<lista.size(); i++){
                Object[] ao= lista.get(i);
                if( (int)ao[0]==numeroDeRegla ){
                    regla= (String)ao[1];
                    break;
                }
            }
        }
        
        return regla;
    }
    
    private String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}
