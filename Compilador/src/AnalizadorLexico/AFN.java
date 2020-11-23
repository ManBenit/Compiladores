package AnalizadorLexico;

//Utilizando método de Thompson
import java.util.ArrayList;
import java.util.HashSet;

public class AFN {
    private HashSet<Estado> estados;
    private String claseLexica;
    
    public AFN(String claseLexica){
        estados= new HashSet();
        this.claseLexica= claseLexica;
    }
    
    public void crearBasico(char c){
        Estado inicio= new Estado(true, false, 0);
        Estado fin= new Estado(false, true, ClaseLexica.obtToken(claseLexica));
        inicio.agregarTransicion(c, fin);
        estados.add(inicio);
        estados.add(fin);
    }
    
    public HashSet<Estado> estados(){
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
        
    public ArrayList<Estado> estadosAceptacion(){
        ArrayList<Estado> acept= new ArrayList<>();
        for(Estado e: this.estados)
            if(e.esAceptacion())
                acept.add(e);
        return acept;    
    } 
    
    
    // T H O M P S O N /////////////////////////////////////////////////////////
    public void unir(AFN afn){
        Estado nuevoIni= new Estado(true, false, 0);
        Estado nuevoFin= new Estado(false, true, ClaseLexica.obtToken(claseLexica));
        
        //Estados del AFN this
        nuevoIni.agregarTransicion(this.estadoInicial()); //Transiciones épsilon
        this.estadoInicial().cambiarInicial();

        for(Estado e: estadosAceptacion()){
            e.agregarTransicion(nuevoFin);
            e.cambiarAceptacion();
        }
        
        //A cada elemento del afn parámetro,
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
        Estado nuevoFin= new Estado(false, true, ClaseLexica.obtToken(claseLexica));
        
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
        Estado nuevoFin= new Estado(false, true, ClaseLexica.obtToken(claseLexica));
        
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
        Estado nuevoFin= new Estado(false, true, ClaseLexica.obtToken(claseLexica));
        
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
    
    
    public static AFN unificarInicial(ArrayList<AFN> afns){
        AFN defAfn= new AFN("AFNTOTAL");
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
        ArrayList<Estado> ctrlImp= new ArrayList<>();
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
