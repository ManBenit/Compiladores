package AnalizadorLexico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AFD {
    private HashSet<Estado> estados;
    private ArrayList<Character> alfabeto;
    private int[][] tablaEstados;
    private final String nombre;
    private int id=0;
    
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
        HashSet<HashSet<Estado>> conjEstados= obtConjEdos(afn);
        tablaEstados= new int[conjEstados.size()][alfabeto.size()+1];
        //Iniciar tabla con todos los vlaores en -1, excepto el vector de tokens
        for(int i=0; i<conjEstados.size(); i++)
            for(int j=0; j<alfabeto.size(); j++)
                tablaEstados[i][j]=-1;
        
        LinkedList<Integer> pilaCompuebaNoms= new LinkedList();
             
        int indFil=conjEstados.size()-1; //Se llena de abajo a arriba para que el estado inicial quede siempre en la primera fila
        int tokenRelacionado;
        for(HashSet<Estado> conjunto: conjEstados){//Recorrer cada conjunto de estados
            //Obtener token para el vector de tokens (última columna)
            tokenRelacionado=0; //si no hay, será 0
            for(Estado e: conjunto)
                if(e.esAceptacion()){
                    tokenRelacionado=e.obtToken();
                    break;
                }
            tablaEstados[indFil][alfabeto.size()]= tokenRelacionado;

            //Posición en la table de estados
            HashSet<Estado> conjaux;//Para el resultado del mover.
            for(char c: alfabeto){
                conjaux= mover(conjunto, c);
                for(Estado e: conjaux){
//                    if(!pilaCompuebaNoms.contains(e.id())){
//                        pilaCompuebaNoms.add(e.id());
//                    }
                    
                    //tablaEstados[indFil][alfabeto.indexOf(c)]= pilaCompuebaNoms.indexOf(e.id());
                    tablaEstados[indFil][alfabeto.indexOf(c)]= e.id();
                }
            }
            
            for(int i: pilaCompuebaNoms){
                System.out.print(i+", ");
            }
            System.out.println("");
               
            indFil-=1;
        }
       
        //Renombrar estados
        
        for(int i=0; i<conjEstados.size(); i++){
            for(int j=0; j<alfabeto.size()+1; j++){
                
            }
        }
        
    }
    
    public int[][] tablaDeEstados(){
        return tablaEstados;
    }
    
    public ArrayList<Character> alfabeto(){
        return alfabeto;
    }
    
    private HashSet<HashSet<Estado>> obtConjEdos(AFN afn){
        HashSet<Estado> cEstados= new HashSet(); //Conjuntos de estados a analizar
        LinkedList<HashSet<Estado>> S= new LinkedList(); //Pila de comprobación
        HashSet<HashSet<Estado>> utiles= new HashSet(); //Estados que servirán para el AFD
        
        //Calcular S0
        cEstados= cerraduraEpsilon(afn.estadoInicial());
        S.add(cEstados);
        utiles.add(cEstados);
        
        //Análisis del resto de estados
        int i=0;
        while(i<S.size()){
            cEstados= S.pop();
            for(char s: alfabeto){
                HashSet<Estado> ira= irA(cEstados, s);
                if(ira.size()>0){
                    S.add(ira);
                    utiles.add(ira);
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
