package AnalizadorLexico;

import java.util.HashSet;

public class Estado {
    private static int idCuenta=0;
    private int id;
    private boolean aceptacion;
    private boolean inicial;
    private HashSet<Transicion> transiciones;
    private int token;
        
    public Estado(boolean ini, boolean acept, Integer token){
        transiciones= new HashSet();
        inicial=ini;
        aceptacion= acept;
        if(acept)
            if(token==null)
                this.token=0;
            else
                this.token=token;
        else
            this.token=0;
        id= idCuenta;
        idCuenta+=1;
    }
    
    public void agregarTransicion(char c, Estado destino){
        this.transiciones.add(new Transicion(c, destino));
    }
    
    //Modelado de nueva transición épsilon
    public void agregarTransicion(Estado destino){
        this.transiciones.add(new Transicion('\0', destino));
    }
    
    //Puede haber una transición existente que solo hay que asignar
    public void agregarTransicion(Transicion t){
        this.transiciones.add(t);
    }
    
    public int obtToken(){
        return token;
    }
    
    public int id(){
        return id;
    }
    
    public String obtNombre(){
        return "S"+id;
    }
    
    public void nuevoId(int id){
        this.id= id;
    }
    
    public boolean esAceptacion(){
        return aceptacion;
    }
    
    public void cambiarAceptacion(){
        aceptacion= !aceptacion;  
        token=0;
    }
    public void cambiarAceptacion(Integer token){
        aceptacion= !aceptacion;        
        this.token=token;     
    }
    
    public boolean esInicial(){
        return inicial;
    }
    
    public void cambiarInicial(){
        inicial= !inicial;
    }
    
    public HashSet<Transicion> obtTransiciones(){
        return transiciones;
    }
    
    public int numTransiciones(){
        return transiciones.size();
    }
    
    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        
        sb.append("S").append(id).append(": ");
        
        if(aceptacion)
            sb.append("[ACEPTACIÓN], ");
        if(inicial)
            sb.append("[INICIAL], ");
        if(token>0)
            sb.append("[token ").append(token).append("], ");
        
        return sb.toString();
    }
    
}
