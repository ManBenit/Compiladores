package AnalizadorLexico;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AFD{
    private LinkedList<Estado> estados;
    private ArrayList<Character> alfabeto;
    private ArrayList<int[]> tablaEstados;
    private final String nombre;
    
    public AFD(String nombre){
        tablaEstados= new ArrayList<>();
        alfabeto= new ArrayList();
        alfabeto.add('L');
        alfabeto.add('D');
        alfabeto.add('.');
        alfabeto.add('M');
        alfabeto.add('P');
        alfabeto.add('E');
        alfabeto.add('T');
        
        
        estados= new LinkedList<>();
        this.nombre=nombre;
    }
    
    public void convertir(AFN afn){
        LinkedList<HashSet<Estado>> conjEstados= obtConjEdos(afn);
        LinkedList<HashSet<Estado>> conjEstadosAux= conjEstados;
        
        
//        for(HashSet<Estado> conjunto: conjEstados){
//            //System.out.print("S"+i+"= {");
//            System.out.print("{");
//            for(Estado e: conjunto)
//                System.out.print(e+", ");
//            System.out.println("}");
//        }
        
        //Crear estado para cada subconjunto
        int id=conjEstados.size()-1;
        for(HashSet<Estado> conjunto: conjEstados){
            boolean ini= false, acep=false;
            //int id=0;
            int token=0;
            Estado nEdo;
            
            for(Estado e: conjunto){
                id= e.id();
                if(e.esAceptacion()){
                    acep=true;
                    token= e.obtToken();
                    break;
                }
                if(e.esInicial()){
                    ini=true;
                    break;
                }
            }
            
            nEdo= new Estado(ini, acep, token);
            nEdo.nuevoId(id);
            estados.addFirst(nEdo);
            //id-=1;
        }
        
        System.out.println(estados.size());
        
        for(Estado e: estados){
            //System.out.print("S"+i+"= {");
            System.out.println(e+", ");
        }
        System.out.println("");
        
        LinkedList<HashSet<Estado>> pilaDeControl= new LinkedList<>();
        HashSet<Estado> conjAux;
        int i=1;
        while(i<conjEstados.size()){
            for(char s: alfabeto){
                conjAux= irA(conjEstados.get(i), s);
                if(conjAux.size()>0){
                    if(!pilaDeControl.contains(conjAux))
                        pilaDeControl.add(conjAux);
                    
                    HashSet<Estado> encontrado=null;
                    for(HashSet<Estado> buscar: pilaDeControl){
                        if(buscar.equals(conjAux)){
                            encontrado=buscar;
                            break;
                        }
                    }
                    
                    representante(conjEstados.get(i)).agregarTransicion(s, representante(conjAux));
                }
            }
            
            i+=1;
        }
        
        metodo();
    }
    
    private void metodo(){
        for(Estado e : estados){ //Conjunto general
            System.out.println(e+"*****************");
            for(Transicion t: e.obtTransiciones())
                System.out.println(t);
            System.out.println("vvvvvvvvvvvvvvv");
        }
    }
    
    //Estado creado que representa un conjunto de estados
    private Estado representante(HashSet<Estado> conjunto){
        Estado ret=null;
        for(Estado edoConj: conjunto)
            for(Estado e: estados)
                if(e.id()==edoConj.id())
                    ret=e;
        return ret;
    }
    
    public int[][] tablaDeEstados(){
        int[][] tabEstados= new int[tablaEstados.size()][alfabeto.size()+1+1];
        for(int i=0; i<tablaEstados.size(); i++)
            for(int j=0; j<alfabeto.size()+1+1; j++)
                tabEstados[i][j]= tablaEstados.get(i)[j];
            
        
        return tabEstados;
    }
    
    public ArrayList<Character> alfabeto(){
        return alfabeto;
    }
    
    private LinkedList<HashSet<Estado>> obtConjEdos(AFN afn){
        HashSet<Estado> cEstados; //Conjuntos de estados a analizar
        LinkedList<HashSet<Estado>> S= new LinkedList(); //Pila de comprobación
        HashSet<HashSet<Estado>> utiles= new HashSet(); //Estados que servirán para el AFD
        LinkedList<HashSet<Estado>> utilRet= new LinkedList<>();
        
        //Calcular S0
        cEstados= cerraduraEpsilon(afn.estadoInicial());
        S.add(cEstados);
        utiles.add(cEstados);
        
        //Análisis del resto de estados
        int i=0;
        while(i<S.size()){
            cEstados= S.pop();
            //*************
            int[] fila= new int[alfabeto.size()+2];
            fila[0]=0;
            fila[fila.length-1]=0;
            for(int j=1; j<fila.length-1; j++)
                fila[j]=-1;
            HashSet<Estado> conjaux;//Para el resultado del mover.
            //*************
            for(char s: alfabeto){
                HashSet<Estado> ira= irA(cEstados, s);
                    
               
                if(ira.size()>0){
                    //******
//                    conjaux= mover(ira, s); //Conjunto actual (va a ponerse en la tabla)
//                    if(conjaux.size()>0){
//                        for(Estado e: conjaux){
//                            System.out.printf("[%d: %s, %c]", i, e, s);
//                        
//    //                        if(!edosLeidos.contains(e)){
//    //                            edosLeidos.add(e);
//    //                        }
//    //    //                        if(!edosLeidos.contains(e.id()))
//    //    //                            edosLeidos.add(e.id());
//    //                        tablaEstados.get(indFil)[alfabeto.indexOf(c)+1]= e.id(); //En este moemento se agregan los idsTemporales
//                        }
//                        System.out.println("");
//                    }
                    //******
                    //************
//                    fila[alfabeto.indexOf(s)+1]=iAux;

                    //*************
                    S.add(ira);
                    utiles.add(ira);
                }
            }
            tablaEstados.add(fila);
            tablaEstados.get(i)[0]=i;
            i+=1;
            //********
//            for(int h: fila)
//              System.out.print(h+", ");
//            System.out.println("");
            //*******
        }
        
        for(HashSet<Estado> hs: utiles)
            utilRet.add(hs);
        
        return utilRet;
    }
    
//    //---
//    System.out.print("S"+i+"={");
//    for(Estado e: ira)
//        System.out.print(e.id()+", ");
//    System.out.println("}");
//    //----
    
    private HashSet<Estado> cerraduraEpsilon(Estado e){
        Estado p;
        
        LinkedList<Estado> S= new LinkedList();
        HashSet<Estado> R= new HashSet(); //Estados ya analizados
        S.push(e);
        
        //Se ve cada estado si hay transiciones
        while(!S.isEmpty()){
            p= S.pop(); //Sacar el propio elemento,
            R.add(p); //agregarlo a R
            
            //Analizar si p tiene transiciones épsilon
            for(Transicion t: p.obtTransiciones())
                if(t.simbolo()=='\0')
                    S.push(t.destino());    
        }
        
        return R;
    }
    
    private HashSet<Estado> cerraduraEpsilon(HashSet<Estado> E){
        HashSet R= new HashSet();
        
        for(Estado e: E)
            R.addAll(cerraduraEpsilon(e));
        
        return R;            
    }
    
    private HashSet<Estado> mover(Estado e, char s){
        HashSet<Estado> R= new HashSet();
        for(Transicion t: e.obtTransiciones())
            if(t.simbolo()==s)
                R.add(t.destino());
        
        return R;
    }
    
    private HashSet<Estado> mover(HashSet<Estado> E, char s){
        HashSet<Estado> R= new HashSet();
        for(Estado e: E)
            R.addAll(mover(e, s));
        
        return R;
    }
    
    private HashSet<Estado> irA(HashSet<Estado> E, char s){
        return cerraduraEpsilon(mover(E, s));
    }
    
    
    
    
    
    @Override
    public String toString(){
        ArrayList<Estado> ctrlImp= new ArrayList<>();
        StringBuilder sb= new StringBuilder();
        sb.append("***************** AFD ").append(nombre).append(" ***********************");
        
        sb.append("\n");
        sb.append(estados.size()).append(" estados.").append("\n");
        
        for(Estado e: estados){
            if(e.esInicial())
                sb.append(e).append("\n");
            
            if(!ctrlImp.contains(e))
                if(e.numTransiciones()>0)
                    for(Transicion t: e.obtTransiciones()){
                        sb.append(e.obtNombre()).append(t).append("\n");
                    }
                    
            ctrlImp.add(e);
        }
        
        sb.append("**************************************************");
        
        return sb.toString();
    }
}
