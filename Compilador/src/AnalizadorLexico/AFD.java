package AnalizadorLexico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class AFD {
    private HashSet<Estado> estados;
    private ArrayList<Character> alfabeto;
    private ArrayList<int[]> tablaEstados;
    private final String nombre;
    private int id=0;
    
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
        
        
        estados= new HashSet();
        this.nombre=nombre;
    }
    
    public void convertir(AFN afn){
        LinkedList<HashSet<Estado>> conjEstados= obtConjEdos(afn);
        Object[] arrayConjEstados= conjEstados.toArray();
        
        //Iniciar tabla con todos los vlaores en -1, excepto el vector de tokens
//        tablaEstados= new int[arrayConjEstados.length][alfabeto.size()+1+1]; //uno extra de tokens(N) y uno de inicial(0)
//        for(int i=0; i<arrayConjEstados.length; i++)
//            for(int j=1; j<alfabeto.size()+1; j++)
//                tablaEstados[i][j]=-1;
        
        //---
        for(HashSet<Estado> he: conjEstados){
            System.out.print("S"+conjEstados.indexOf(he)+"={");
            for(Estado e: he)
                System.out.print(e.id()+", ");
            System.out.println("}");
        }
        //----
             
        int indFil=0; //Se llena de abajo a arriba para que el estado inicial quede siempre en la primera fila
        int conjContenedor;
        int tokenRelacionado;
        //int[] nvosIndices= new int[conjEstados.size()]; //Arreglo cuyo tamaño es el número de conjuntos de estados menos el inicial
        ArrayList<Integer> nvosIndices= new ArrayList<>();
        LinkedList<Estado> edosLeidos= new LinkedList<>();
        LinkedList<HashSet<Estado>> moverConjunto= new LinkedList(); //Mover de cada conjunto de estados (para tabla)
//        LinkedList<Integer> edosLeidos= new LinkedList<>();
        //for(int i=arrayConjEstados.length-1; i>=0; i--){//Recorrer cada conjunto de estados
        for(HashSet<Estado> conjunto: conjEstados){//Recorrer cada conjunto de estados
            //HashSet<Estado> conjunto= (HashSet<Estado>)arrayConjEstados[i];
            
            
            
            //Obtener token para el vector de tokens (última columna)
            tokenRelacionado=0; //si no hay, será 0
            for(Estado e: conjunto)
                if(e.esAceptacion()){
                    tokenRelacionado=e.obtToken();
                    break;
                }
            tablaEstados.get(indFil)[alfabeto.size()+1]= tokenRelacionado;
            
            
                    
            
            //Posición en la tabla de estados
            HashSet<Estado> conjaux;//Para el resultado del mover.
            tablaEstados.get(indFil)[0]=conjEstados.indexOf(conjunto);
            for(char c: alfabeto){
                conjaux= mover(conjunto, c); //Conjunto actual
//                moverConjunto.add(conjaux);
                
                if(conjaux.size()>0){
                    for(Estado e: conjaux){
                        System.out.printf("[%d: %s, %c]\n", indFil, e, c);
                        if(!edosLeidos.contains(e)){
                            edosLeidos.add(e);
                        }
//                        if(!edosLeidos.contains(e.id()))
//                            edosLeidos.add(e.id());
                        tablaEstados.get(indFil)[alfabeto.indexOf(c)+1]= e.id(); //En este moemento se agregan los idsTemporales
                    }
                }
            }
            
            indFil+=1;
        }
        
        
        System.out.println("----------leidos");
        for(Estado e: edosLeidos)
            System.out.println(e+"- ");
        System.out.println("---------------------");
        
        //Id de los estados en cuerpo de tabla
//        for(int i=0; i<edosLeidos.size(); i++){
//            for(int j=1; j<alfabeto.size()+1; j++){
//                int idDentro=tablaEstados.get(i)[j];
//                if(idDentro>0)
//                    if(!nvosIndices.contains(idDentro))
//                        nvosIndices.add(idDentro);
//            }
//        }
        
        //Renombrar estados: sustituir los índices originales por los nuevos en la tabla de estados
//        for(int i=0; i<edosLeidos.size(); i++){
//            for(int j=1; j<alfabeto.size()+1; j++){
//                if(tablaEstados.get(i)[j]>0)
//                    tablaEstados.get(i)[j]=nvosIndices.indexOf(tablaEstados.get(i)[j])+1;
//            }
//        }
        
//        for(int t: nvosIndices)
//            System.out.print(t+"_");*/
        
        //Id de los estados de arranque
//        indFil=edosLeidos.size()-1;
//        for(int j=0; j<edosLeidos.size(); j++){
//            //tablaEstados[indFil][0]= edosLeidos.get(j).id(); //Nuevos id de estado *****
//            tablaEstados.get(indFil)[0]= indFil;
//            //System.out.printf("(%d, %d)", indFil, j);
//            indFil-=1;
//        }
        
        
        
        
        
        
        
        
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
        LinkedList<HashSet<Estado>> utilRet= new LinkedList(); //Estados utiles a regresar
        
        //Calcular S0
        cEstados= cerraduraEpsilon(afn.estadoInicial());
        S.add(cEstados);
        utiles.add(cEstados);
        
        //Análisis del resto de estados
        int i=0;
        int iAux=1;
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
                    iAux+=1;
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
