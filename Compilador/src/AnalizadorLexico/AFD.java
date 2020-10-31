package AnalizadorLexico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AFD {
    private LinkedList<Estado> estados;
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
        
        
        estados= new LinkedList();
        this.nombre=nombre;
    }
    
    public void convertir(AFN afn){
        HashSet<HashSet<Estado>> conjEstados= obtConjEdos(afn);
        //tablaEstados= new Estado[estados.size()][alfabeto.size()+1];
        
    }
    
    public Estado[][] tablaDeEstados(){
        return tablaEstados;
    }
    
    private void crearTabla(){
        
    }
    
    //Método que obtiene los conjuntos de estados y asigna los estados de AFD
    private HashSet<HashSet<Estado>> obtConjEdos(AFN afn){
        HashSet<Estado> cEstados= new HashSet();
        HashSet<HashSet<Estado>> R= new HashSet();
        LinkedList<HashSet<Estado>> S= new LinkedList();
        
        boolean acept=false; //Saber si algún estado es aceptación
        boolean ini=false; //Saber si algún estado es inicial (solo debe haber uno)
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
        estados.add(inicial);
        
        //Reinicio
        acept=false;
        token=null;
        
        
        int i=0;
        while(i<S.size()){
            cEstados= S.pop();
            
            System.out.println("\nAnálisis de S"+i);
            for(char s: alfabeto){
                System.out.println("Símbolo "+s);
                HashSet<Estado> ira= irA(cEstados, s);
                if(ira.size()>0){
                    
                    //Verificar acceptación
                    for(Estado e: ira)
                        if(e.esAceptacion()){
                            acept=true;
                            token= e.obtToken();
                        }
                    Estado nvoEdo= new Estado(false, acept, token);
                    nvoEdo.nuevoId(id);
                    estados.get(i).agregarTransicion(s, nvoEdo);
                    estados.add(nvoEdo);
                    S.add(ira);
                    R.add(ira);
                    
                    for(Estado v: ira)
                        System.out.println(v);
                    
                    id+=1;
                }
                
                //Reiniciar variables
                acept=false;
                ini=false;
                token=null;
            } 
            System.out.println("");
            i+=1;
        }
        
        return R;
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
                    S.add(t.destino());    
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
