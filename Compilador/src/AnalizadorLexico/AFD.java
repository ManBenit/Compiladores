package AnalizadorLexico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class AFD{
    private LinkedList<Estado> estados;
    private ArrayList<Character> alfabeto;
    private int[][] tablaEstados;
    private final String nombre;
    private LinkedList<HashSet<Estado>> conjEstados;
    
    public AFD(String nombre){
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
    
    public boolean validarCadena(String cadena){
        ArrayList<Integer> idEdos= new ArrayList<>();
        for(Estado e: estados)
            idEdos.add(e.id());
        StringBuilder sb= new StringBuilder("");
        char[] cad= cadena.toCharArray();
        int estadoActual=0; //Recorre las filas
        boolean previoAceptado= false;
        int posRecordada=0;
        int token=0;
        
        //Si el caracter actual es el fin de la cadena
        
        //Analizar cada caracter
        int inicio=0, fin=0, indiceCadena=0;
        while(indiceCadena<cad.length){
            int posible= tablaEstados[estadoActual][alfabeto.indexOf(cad[indiceCadena])];
            if(posible>-1){ //Si hay transicion de estadoActual con c a algún estado
                estadoActual=idEdos.indexOf(posible);
                
                System.out.println("Posible: "+posible);
                token= tablaEstados[estadoActual][alfabeto.size()+1];
                if(token>0){ //Si es un estado de aceptación
                    posRecordada=indiceCadena;
                    
                    previoAceptado=true;
                    
                }
                sb.append(cad[indiceCadena]);
                indiceCadena+=1; //Se apsa al siguiente caracter
            }
            else{ //No hubo transición
                if(!previoAceptado){ //Si no ha habido aceptación, hay error
                    System.out.printf("Rechazado %c, posición %d\n", cad[indiceCadena], indiceCadena);
                    sb.delete(0, sb.length());
                    estadoActual=0;
                    indiceCadena+=1;
                }
                else{
                    //Regresar al último índice con aceptación
                    //indiceCadena=posRecordada;
                    System.out.printf("Último lex aceptado: %s, token %d\n", sb.toString(), token);
                    sb.delete(0, sb.length());
                    previoAceptado=false;
                    
                }
            }
        }
        
        
        return true;
    }
    
    public void convertir(AFN afn){
        conjEstados= obtConjEdos(afn);
        
        //Crear estado para cada subconjunto
        int idDisc=conjEstados.size()-1; //Discriminante del ID
        int id=0;
        for(HashSet<Estado> conjunto: conjEstados){
            boolean ini= false, acep=false;
            int token=0;
            Estado nEdo;
            
            for(Estado e: conjunto){
                id= e.token()+idDisc; //ID: token + el conjunto que repreesenta (mismo índice que idDisc)
                if(e.esAceptacion()){
                    acep=true;
                    token= e.token();
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
            idDisc-=1;
        }
        
        HashSet<Estado> conjAux;
        int i=1; //Sea Sn= irA(Sk, s), i representa k
        while(i<conjEstados.size()){
            for(char s: alfabeto){
                conjAux= irA(conjEstados.get(i), s);
                if(conjAux.size()>0)
                    representante(conjEstados.get(i)).agregarTransicion(s, representante(conjAux));
            }
            i+=1;
        }
        
        crearTabla();
        //renombrarEstadosEnTabla(); //Método para que sean números consecutivos en la tabla
    }
    
    //Estado creado que representa un conjunto de estados
    private Estado representante(HashSet<Estado> conjunto){
        int idDisc=conjEstados.size()-1;
        Estado ret=null;
        for(Estado edoConj: conjunto){
            for(Estado e: estados){
                if(conjEstados.get(idDisc).equals(conjunto)){
                    ret=e;
                    break;
                }
                idDisc-=1;
            }
            idDisc=conjEstados.size()-1;
        }
                    
        return ret;
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
            for(Transicion t: e.obtTransiciones())
                tablaEstados[indice][alfabeto.indexOf(t.simbolo())+1]= t.destino().id();
            
            indice+=1;
        }
    }
    
    //Este método renombra lso estados ÚNICAMENTE EN LA TABLA DE ESTADOS para
    //verlos como números consecutivos
    private void renombrarEstadosEnTabla(){
        ArrayList<Integer> idEdos= new ArrayList<>();
        for(Estado e: estados)
            idEdos.add(e.id());
        for(int i=0; i<tablaEstados.length; i++)
            for(int j=0; j<tablaEstados[i].length-1; j++)
                if(tablaEstados[i][j]>-1)
                    tablaEstados[i][j]=idEdos.indexOf(tablaEstados[i][j]);
    }
    
    public int[][] tablaDeEstados(){
        return tablaEstados;
    }
    
    public ArrayList<Character> alfabeto(){
        return alfabeto;
    }
    
    private LinkedList<HashSet<Estado>> obtConjEdos(AFN afn){
        HashSet<Estado> cEstados; //Conjuntos de estados a analizar
        LinkedList<HashSet<Estado>> S= new LinkedList(); //Pila de comprobación
        HashSet<HashSet<Estado>> utiles= new HashSet(); //Estados que servirán para el AFD, HashSet garantiza que no se repitan
        LinkedList<HashSet<Estado>> utilRet= new LinkedList(); //Se regresará el conjunto en forma de lista apra manejarlo más fácilmente
        
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
        
        //Meter conjunto a lista
        for(HashSet<Estado> hs: utiles)
            utilRet.add(hs);
        
        return utilRet;
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
                        sb.append("S").append(e.id()).append(t).append("\n");
                    }
                    
            ctrlImp.add(e);
        }
        
        sb.append("**************************************************");
        
        return sb.toString();
    }
}
