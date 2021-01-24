package AnalizadorLexico;

//Utilizando método de Thompson
import java.util.LinkedList;
import java.util.LinkedHashSet;

public class AFN {
    private LinkedHashSet<Estado> estados;
    private String claseLexica;
    private int token;
    
    public AFN(String claseLexica, int token){
        estados= new LinkedHashSet();
        this.claseLexica= claseLexica;
        this.token= token;
    }
    
    public void crearBasico(char c){
        Estado inicio= new Estado(true, false, 0);
        Estado fin= new Estado(false, true, token);
        inicio.agregarTransicion(c, fin);
        estados.add(inicio);
        estados.add(fin);
    }
    
    public void crearBasico(char charIni, char charFin){
        Estado inicio= new Estado(true, false, 0);
        Estado fin= new Estado(false, true, token);
        inicio.agregarTransicion(charIni, charFin, fin);
        estados.add(inicio);
        estados.add(fin);
    }
    
    public LinkedHashSet<Estado> estados(){
        return estados;
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
        
    public LinkedList<Estado> estadosAceptacion(){
        LinkedList<Estado> acept= new LinkedList<>();
        for(Estado e: this.estados)
            if(e.esAceptacion())
                acept.add(e);
        return acept;    
    } 
    
    public int token(){
        return token;
    }
    
    
    // T H O M P S O N /////////////////////////////////////////////////////////
    //Cuando se use solo un básico más
    public void unir(AFN afn){
        unir(new AFN[]{afn});
    }
    
    //Cualquier número mayor a 2 de AFNs a unir
    public void unir(AFN[] afns){
        Estado nuevoIni= new Estado(true, false, 0);
        Estado nuevoFin= new Estado(false, true, token);
        
        //Nuevo inical al estado inicial del AFN this
        nuevoIni.agregarTransicion(this.estadoInicial()); //Transiciones épsilon
        this.estadoInicial().cambiarInicial();
        
        //Nuevo inical al estado inicial de cada afn del arreglo afns
        for(AFN afn: afns){
            nuevoIni.agregarTransicion(afn.estadoInicial()); //Transiciones épsilon
            afn.estadoInicial().cambiarInicial();
        }
        
        //Estado final de this al nuevo final
        for(Estado e: estadosAceptacion()){
            e.agregarTransicion(nuevoFin);
            e.cambiarAceptacion();
        }
        
        //Estado final de cada anf de afns al nuevo final
        for(AFN afn: afns){
            for(Estado e: afn.estadosAceptacion()){
                e.agregarTransicion(nuevoFin);
                e.cambiarAceptacion();
            }
        }
        
        //A cada elemento de cada afn de afns
        for(AFN afn: afns){
            for(Estado e: afn.estados){
                //verifica si es inicial para enlazarlo con épsilon al nuevo inicial,
                if(e.esInicial()){
                    nuevoIni.agregarTransicion(e);
                    e.cambiarInicial();
                }

                //verifica si es aceptación para enlazarlo con épsilon al nuevo final
                if(e.esAceptacion()){
                    e.agregarTransicion(nuevoFin);
                    e.cambiarAceptacion();
                }

                //y agregarlo a la lista de estados del AFN this, así se fusionan en 
                //lugar de crear uno nuevo
                this.estados.add(e);
            }
        }
        
        
        this.estados.add(nuevoIni);
        this.estados.add(nuevoFin);
    }
    
    public void concatenar(AFN afn){
        Estado finThis=this.estadosAceptacion().get(0);
        finThis.cambiarAceptacion();
        
        for(Estado e: afn.estados)
            if(e.esInicial())
                for(Transicion t: e.obtTransiciones())
                    finThis.agregarTransicion(t);
            else
                this.estados.add(e);
    }
    
    public void cTransitiva(){
        Estado nuevoIni= new Estado(true, false, 0);
        Estado nuevoFin= new Estado(false, true, token);
        
        nuevoIni.agregarTransicion(estadoInicial());
        
        //Debe haber un solo estado de aceptación en este punto
        estadosAceptacion().get(0).agregarTransicion(nuevoFin);
        estadosAceptacion().get(0).agregarTransicion(estadoInicial());
        estadosAceptacion().get(0).cambiarAceptacion();
        
        estadoInicial().cambiarInicial();
        
        estados.add(nuevoIni);
        estados.add(nuevoFin);
    }
    
    public void cEstrella(){
        Estado nuevoIni= new Estado(true, false, 0);
        Estado nuevoFin= new Estado(false, true, token);
        
        nuevoIni.agregarTransicion(estadoInicial());
        
        //Debe haber un solo estado de aceptación en este punto
        estadosAceptacion().get(0).agregarTransicion(nuevoFin);
        estadosAceptacion().get(0).agregarTransicion(estadoInicial());
        estadosAceptacion().get(0).cambiarAceptacion();
        
        estadoInicial().cambiarInicial();
        
        nuevoIni.agregarTransicion(nuevoFin);
        
        estados.add(nuevoIni);
        estados.add(nuevoFin);
    }
    
    public void opcional(){
        Estado nuevoIni= new Estado(true, false, 0);
        Estado nuevoFin= new Estado(false, true, token);
        
        nuevoIni.agregarTransicion(estadoInicial());
        
        //Debe haber un solo estado de aceptación en este punto
        estadosAceptacion().get(0).agregarTransicion(nuevoFin);
        estadosAceptacion().get(0).cambiarAceptacion();
        
        estadoInicial().cambiarInicial();
        
        nuevoIni.agregarTransicion(nuevoFin);
        
        estados.add(nuevoIni);
        estados.add(nuevoFin);
    }
    //////////////////////////////////////////////////////////////////////////////
    
    
    public static AFN unificarInicial(LinkedList<AFN> afns){
        AFN defAfn= new AFN("AFNTOTAL", -1); //el -1 es provisional
        Estado inicialTotal= new Estado(true, false, 0);
        int nuevosId=1;
        
        inicialTotal.nuevoId(0);
        defAfn.estados.add(inicialTotal);
        
        for(AFN afn: afns){
            inicialTotal.agregarTransicion(afn.estadoInicial());
            afn.estadoInicial().cambiarInicial();
            for(Estado e: afn.estados()){
                e.nuevoId(nuevosId);
                defAfn.estados.add(e);
                nuevosId+=1;
            }
        }
        
        return defAfn;
    }
    
    
    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        if(claseLexica.equals("AFNTOTAL"))
            sb.append("***************** AFN total ********************");
        else
            sb.append("***************** AFN de ").append(claseLexica).append(" *************************");
        
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
