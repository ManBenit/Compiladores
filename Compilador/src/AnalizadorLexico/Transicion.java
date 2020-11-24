package AnalizadorLexico;

public class Transicion{
    private char simbolo;
    private Estado destino;
    
    public Transicion(char c, Estado e){
        simbolo=c;
        destino=e;
    }
    
    //Modelo de un rango de caracteres
    public Transicion(char ini, char fin, Estado e){
        //El ascii de fin debe ser mayor al de ini
        if( (int)ini < (int)fin){
            
        }
        else
            System.out.printf("ERROR AL CREAR TRANSICIÓN: [%c - %c]", ini, fin);
    }
    
    //Modelo de la transición épsilon
    public Transicion(Estado e){
        simbolo= '\0';
        destino= e;
    }
    
    public char simbolo(){
        return simbolo;
    }
    
    public Estado destino(){
        return destino;
    }
    
    @Override
    public String toString(){
        if(simbolo=='\0')
            return " ----"+"\u0190"+"----> "+destino;
        else
            return " ----"+simbolo+"----> "+destino;
    }
    
}
