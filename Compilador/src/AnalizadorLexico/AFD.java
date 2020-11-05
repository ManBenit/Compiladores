package AnalizadorLexico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AFD {
    private HashSet<Estado> estados;
    private ArrayList<Character> alfabeto;
    private Estado[][] tablaEstados;
    private final String nombre;
    private int id=1;
    
    public AFD(String nombre){
        alfabeto= new ArrayList();
        alfabeto.add('L');
        alfabeto.add('D');
        alfabeto.add('.');
        alfabeto.add('M');
        alfabeto.add('P');
        alfabeto.add('E');
        alfabeto.add('T');
        
        
        estados= new HashSet();
        this.nombre=nombre;
    }
    
    public void convertir(AFN afn){
//        Object[] conjEstados= obtConjEdos(afn).toArray();
        HashSet<HashSet<Estado>> conjEstados= obtConjEdos(afn);
        
        //LinkedList<Estado> conjEstados= obtConjEdos(afn);
        HashSet<Estado> origenes= new HashSet();
        HashSet<Estado> destinosEpsilon= new HashSet();
        
        
        for(HashSet<Estado> hse: conjEstados)
            for(Estado e: hse)
                destinosEpsilon.addAll(mover(e, '\0'));
            
        System.out.println("Dest\u0190: "+destinosEpsilon.size());
        
        
        Estado edoAux;
        for(HashSet<Estado> hse: conjEstados){
        //for(int i=conjEstados.length-1; i>=0; i--){
            System.out.println("\n*****************************");
            for(Estado e: hse){
            //for(Estado e: (HashSet<Estado>)conjEstados[i]){
                System.out.println(e);
                for(Transicion t: e.obtTransiciones()){
                    edoAux=e;
                    
                        if(destinosEpsilon.contains(t.destino())){
                            while(true){
                                edoAux= t.destino();///////////
                            }
                            
                        }
                    
                }
                
            }
            System.out.println("/////////////////////////////");
        }
        
        //tablaEstados= new Estado[estados.size()][alfabeto.size()+1];
        
        //int utiles= conjEstados.length;
        
//        HashSet<Estado> R= new HashSet();
//        for(HashSet<Estado> hs: conjEstados){
//            
//        }
//        
       
        
        //Renombrar estados
        
    }
    
    public Estado[][] tablaDeEstados(){
        return tablaEstados;
    }
    
    private void crearTabla(){
        
    }
    
    //Método que obtiene los conjuntos de estados y asigna los estados de AFD
    /*private LinkedList<Estado> obtConjEdos(AFN afn){
        HashSet<Estado> cEstados= new HashSet();
        LinkedList<Estado> R= new LinkedList();
        LinkedList<HashSet<Estado>> S= new LinkedList();
        
        boolean acept=false; //Saber si algún estado es aceptación
        //int cont=0; //verificar que en cada AFN hay un solo estado de aceptación
        Integer token=null;
        
        //Calcular S0
        cEstados= cerraduraEpsilon(afn.estadoInicial());
        S.add(cEstados);
        
        //Agregar estado inicial
        for(Estado e: cEstados)
            if(e.esAceptacion()){
                acept=true;
                token= e.obtToken();
            }
        Estado inicial= new Estado(true, acept, token);
        inicial.nuevoId(0);
        R.add(inicial);
        
        //Reinicio
        acept=false;
        token=null;
        
        int i=0;
        while(i<S.size()){
            cEstados= S.pop();
            
            System.out.println("\nAnálisis de S"+i);
            for(char s: alfabeto){
                
                HashSet<Estado> ira= irA(cEstados, s);
                if(ira.size()>0){
                    System.out.println("Símbolo "+s);
                    //Verificar acceptación
                    for(Estado e: ira)
                        if(e.esAceptacion()){
                            acept=true;
                            token= e.obtToken();
                        }
                    Estado nvoEdo= new Estado(false, acept, token);
                    nvoEdo.nuevoId(id);
                    R.get(i).agregarTransicion(s, nvoEdo);
                    R.add(nvoEdo);
                    S.add(ira);
//                    R.add(ira);
                    
                    for(Estado v: ira)
                        System.out.println(v);
                    
                    id+=1;
                }
                
                //Reiniciar variables
                acept=false;
                token=null;
            } 
            System.out.println("");
            i+=1;
        }
        
        return R;
    }*/
    
    private HashSet<HashSet<Estado>> obtConjEdos(AFN afn){
        HashSet<Estado> cEstados= new HashSet();
        LinkedList<HashSet<Estado>> S= new LinkedList();
        HashSet<HashSet<Estado>> utiles= new HashSet();
        
        //Calcular S0
        cEstados= cerraduraEpsilon(afn.estadoInicial());
        S.add(cEstados);
        utiles.add(cEstados);
        
        int i=0;
        while(i<S.size()){
            cEstados= S.pop();
            
            System.out.println("\nAnálisis de S"+i);
            for(char s: alfabeto){
                
                HashSet<Estado> ira= irA(cEstados, s);
                if(ira.size()>0){
                    System.out.println("Símbolo "+s);
                    
                    S.add(ira);
                    utiles.add(ira);
                    
                    
                    for(Estado v: ira)
                        System.out.println(v);
                    
                }
            }
            i+=1;
        }
        
        return utiles;
    }
    
    
    
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
