package AnalizadorLexico;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

public class AFD{
    private LinkedList<Estado> estados;
    //private Map<String, Integer> yyList;
    
    private LinkedList<Character> alfabeto;
    private int[][] tablaEstados;
    private final String nombre;
    private LinkedList<LinkedHashSet<Estado>> conjEstados;
    
    public AFD(String nombre, LinkedList<Character> alfabeto){
        this.alfabeto= alfabeto;
        estados= new LinkedList<>();
        //yyList= new HashMap();
        
//        conjEstados= new LinkedList<>();
        this.nombre=nombre;
    }
    
    public void convertir(AFN afn){
        conjEstados= obtConjEdos(afn);
        LinkedList<Integer> ids= new LinkedList();
        //System.out.println("Aquí");
        //Crear estado para cada subconjunto
        int idDisc=conjEstados.size()-1; //Discriminante del ID
        int id=0;
        
        //Crear un nuevo estado por cada subconjunto de estados
        for(int i=0; i<conjEstados.size(); i++){
            boolean ini= false, acep=false;
            int token=0;
            Estado nEdo;
            
            //Si contiene estado de aceptación o es el inicial
            for(Estado e: conjEstados.get(i)){
                //id= e.token()+idDisc; //ID: token + el conjunto que representa
                
                if(e.esAceptacion()){
                    acep=true;
                    token= e.token();
                    break; //Ver si se puede quitar el brak aquí y en la de abajo
                }
                if(e.esInicial()){
                    ini=true;
                    break;
                }
            }
            
            nEdo= new Estado(ini, acep, token);
            
            nEdo.nuevoId(i);
            estados.add(nEdo);
            idDisc-=1;
        }
//        for(Estado e: estados)
//            System.out.println(e);
        LinkedHashSet<Estado> conjAux;
        int i=0; //Sea Sn= irA(Sk, s), i representa k
        while(i<conjEstados.size()){ //1<11
            for(char s: alfabeto){
                conjAux= irA(conjEstados.get(i), s);
                if(conjAux.size()>0)
                    //representante(conjEstados.get(i)).agregarTransicion(s, representante(conjAux));
                    estados.get(i).agregarTransicion(s, estados.get( conjEstados.indexOf(conjAux) ) );
            }
            i+=1;
        }
        
        crearTabla();
        //renombrarEstadosEnTabla(); //Método para que sean números consecutivos en la tabla
    }
    
    
    private void crearTabla(){
        int filas= conjEstados.size();
        int columnas= alfabeto.size()+2;
        tablaEstados= new int[filas][columnas];
        int indice=0; //Este índice recorrerá las filas
        
        //Iniciar tabla en -1
        for(int i=0; i<filas; i++)
            for(int j=1; j<columnas-1; j++)
                tablaEstados[i][j]= -1;
        
        //Asignar estados de arranque, tokens y transiciones
        for(Estado e: estados){
            tablaEstados[indice][0]= e.id();
            tablaEstados[indice][columnas-1]= e.token();
            for(Transicion t: e.obtTransiciones()){
                for(int i=(int)t.simInicial(); i<=(int)t.simFinal(); i++){
//                    if(t.destino()!=null){
                        tablaEstados[indice][alfabeto.indexOf((char)i)+1]= t.destino().id();//******problemou
//                    }
                }
            }
               
            indice+=1;
        }
    }
    
    public int[][] tablaDeEstados(){
        return tablaEstados;
    }
    
    public LinkedList<Character> alfabeto(){
        return alfabeto;
    }
    
    public LinkedList<Estado> estados(){
        return estados;
    }
    
    private LinkedList<LinkedHashSet<Estado>> obtConjEdos(AFN afn){
        LinkedHashSet<Estado> cEstados; //Conjuntos de estados a analizar
        LinkedList<LinkedHashSet<Estado>> S= new LinkedList(); //Pila de comprobación
        LinkedHashSet<LinkedHashSet<Estado>> utiles= new LinkedHashSet(); //Estados que servirán para el AFD, LinkedHashSet garantiza que no se repitan
        LinkedList<LinkedHashSet<Estado>> utilRet= new LinkedList(); //Se regresará el conjunto en forma de lista para manejarlo más fácilmente
        
        //Calcular S0
        cEstados= cerraduraEpsilon(afn.estadoInicial());
        S.add(cEstados);
        utiles.add(cEstados);
        
        //Análisis del resto de estados
        int i=0, cont=0;
        while(i<S.size()){ //RECUERDA QUITAR EL +1
            cEstados= S.pop();
            for(char s: alfabeto){
                LinkedHashSet<Estado> ira= irA(cEstados, s);
                if(ira.size()>0){  
                    if(!S.isEmpty()){
                        //Se comprueba solo el tope porque en el rango peude salir el mismo conjunto y siempre es un paso antes
                        if(!S.getLast().equals(ira)){

                            cont++;   
                            S.add(ira);
                            utiles.add(ira);

                        }
                    }
                    else{
                        cont++;   
                        S.add(ira);
                        utiles.add(ira);
                    }
                    
                }
            }
            i+=1;
        }
        
        //Pasar el conjunto a lista
        for(LinkedHashSet<Estado> hs: utiles)
            utilRet.add(hs);
        
        for(LinkedHashSet<Estado> hs: utilRet){
            System.out.println("******Conjunto*******");
            for(Estado e: hs){
                System.out.print(e.id()+", ");
                if(e.esAceptacion())
                    System.out.print("Aceptación "+e.id()+"->"+e.token()+", ");
            }
            System.out.println("");
        }
        
        return utilRet;
    }
    
    private LinkedHashSet<Estado> cerraduraEpsilon(Estado e){
        Estado p;
        
        LinkedList<Estado> S= new LinkedList();
        LinkedHashSet<Estado> R= new LinkedHashSet(); //Estados ya analizados
        S.push(e);
        
        //Se ve cada estado si hay transiciones
        while(!S.isEmpty()){
            p= S.pop(); //Sacar el propio elemento,
            R.add(p); //agregarlo a R
            
            //Analizar si p tiene transiciones épsilon
            for(Transicion t: p.obtTransiciones())
                if(t.simFinal()=='\0') //Da igual comparar inicial o final porque se inició con el mismo (\0)
                    S.push(t.destino());    
        }
        
        return R;
    }
    
    private LinkedHashSet<Estado> cerraduraEpsilon(LinkedHashSet<Estado> E){
        LinkedHashSet R= new LinkedHashSet();
        
        for(Estado e: E)
            R.addAll(cerraduraEpsilon(e));
        
        return R;            
    }
    
    private LinkedHashSet<Estado> mover(Estado e, char s){
        LinkedHashSet<Estado> R= new LinkedHashSet();
        for(Transicion t: e.obtTransiciones())
            if((int)s>=(int)t.simInicial() && (int)s<=(int)t.simFinal()) //Si se encuentra dentro del rango
                R.add(t.destino());
        
                
        return R;
    }
    
    private LinkedHashSet<Estado> mover(LinkedHashSet<Estado> E, char s){
        LinkedHashSet<Estado> R= new LinkedHashSet();
        for(Estado e: E)
            R.addAll(mover(e, s));
        
        return R;
    }
    
    private LinkedHashSet<Estado> irA(LinkedHashSet<Estado> E, char s){
        return cerraduraEpsilon(mover(E, s));
    }
    
    public Estado estadoInicial(){
        Estado ret=null;
        for(Estado e: estados)
            if(e.esInicial()){
                ret=e;
                break;
            }
        return ret;        
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        sb.append("***************** AFD ").append(nombre).append(" ***********************");
        
        sb.append("\n");
        sb.append(estados.size()).append(" estados.").append("\n");
        
        for(Estado e: estados){
            if(e.esInicial())
                sb.append(e).append("\n");
            if(e.numTransiciones()>0){
                sb.append("S").append(e.id()).append("\n");
                for(Transicion t: e.obtTransiciones())
                    sb.append(t).append("\n");
            }
        }
        
        sb.append("**************************************************");
        
        return sb.toString();
    }
    
}
