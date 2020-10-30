package AnalizadorLexico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AFD {
    private ArrayList<Estado> estados;
    private ArrayList<Character> alfabeto;
    private Estado[][] tablaEstados;
    private String nombre;
    private int id=0;
    
    public AFD(String nombre){
        estados= new ArrayList();
        this.nombre=nombre;
    }
    
    public AFD convertir(AFN afn){
        HashSet<HashSet<Estado>> conjEdos= obtConjEdos(afn);
        tablaEstados= new Estado[conjEdos.size()][alfabeto.size()+1];
        boolean posible=true;
        
        Estado inicial= new Estado(true, false, null);
        inicial.nuevoId(id);
        estados.add(inicial);
        id+=1;
        
        for(HashSet<Estado> E: conjEdos){
            boolean acept=false; //Saber si algún estado es aceptación
            //int cont=0; //verificar que en cada AFN hay un solo estado de aceptación
            int id= 0; //Nuevo id de los estados del AFD
            Integer token=null;
            
            for(Estado e: E){
                if(e.esAceptacion()){
                    acept=true; //Hay un estado de aceptación del AFN
                    token= e.obtToken();
                }
            }
            
            Estado nvoEdoAFD= new Estado(false, acept, token);
            nvoEdoAFD.nuevoId(id);
            id+=1;
            
        }
        
        return this;
    }
    
    public Estado[][] tablaDeEstados(){
        return tablaEstados;
    }
    
    private void crearTabla(){
        
    }
    
    private HashSet<HashSet<Estado>> obtConjEdos(AFN afn){
        HashSet<HashSet<Estado>> R= new HashSet();
        
        boolean acept=false; //Saber si algún estado es aceptación
        //int cont=0; //verificar que en cada AFN hay un solo estado de aceptación
        int id= 0; //Nuevo id de los estados del AFD
        Integer token=null;
        
        HashSet<Estado> c_e_0= cerraduraEpsilon(afn.estadoInicial());
        
        
        
        
        
        int i=0;
        while(i<R.size()){
            for(HashSet<Estado> C: R){
                if(R.size()==1){ //Si solo hay 1, es el inicial y a fuerza se incluye
                    for(Estado e: C){
                        if(e.esAceptacion()){
                            acept=true; //Hay un estado de aceptación del AFN
                            token= e.obtToken();
                        }
                    }
                    Estado inicial= new Estado(true, acept, token);
                    inicial.nuevoId(id);
                    estados.add(inicial);
                    id+=1;
                    
                    
                    //Reiniciar variables de control
                    acept=false;
                    token=null;
                }
                else{
                    for(char s: alfabeto){
                        if(irA(C, s).size()>0){
                            Estado nvoEdoAFD= new Estado(false, acept, token);
                            nvoEdoAFD.nuevoId(id);
                            id+=1;

                            R.add(C);
                        }
                    
                    }
                }
            }
            i+=1;
        }
        
        
        
        
        
        
        /*for(HashSet<Estado> E: conjEdos){
            boolean acept=false; //Saber si algún estado es aceptación
            //int cont=0; //verificar que en cada AFN hay un solo estado de aceptación
            int id= 0; //Nuevo id de los estados del AFD
            Integer token=null;
            
            for(Estado e: E){
                if(e.esAceptacion()){
                    acept=true; //Hay un estado de aceptación del AFN
                    token= e.obtToken();
                }
            }
            
            Estado nvoEdoAFD= new Estado(false, acept, token);
            nvoEdoAFD.nuevoId(id);
            id+=1;
            
        }
        
        */
        
        
        
        return R;
    }
    
    
    
    private HashSet<Estado> cerraduraEpsilon(Estado e){
        Estado p;
        
        LinkedList S= new LinkedList();
        HashSet R= new HashSet(); //Estados ya analizados
        S.push(e);
        
        //Se ve cada estado si hay transiciones
        while(!S.isEmpty()){
            p= (Estado)S.pop(); //Sacar el propio elemento,
            R.add(p); //agregarlo a R
            
            //Analizar si p tiene transiciones épsilon
            for(Transicion t: p.obtTransiciones())
                R.add(t.destino());      
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
        return "AFD "+nombre;
    }
}
