package AnalizadorLexico;

public class Transicion{
    private char simboloInicial;
    private char simboloFinal;
    private Estado destino;
    
    public Transicion(char c, Estado e){
        simboloInicial=c;
        simboloFinal=c;
        destino=e;
    }
    
    //Modelo de un rango de caracteres
    public Transicion(char ini, char fin, Estado e){
        //El ascii de fin debe ser mayor al de ini
        if( (int)ini < (int)fin){
            simboloInicial=ini;
            simboloFinal=fin;
            destino=e;
        }
        else{
            System.out.printf("ERROR AL CREAR TRANSICIÓN: [%c - %c]\n", ini, fin);
            System.exit(1);
        }
    }
    
    //Modelo de la transición épsilon
    public Transicion(Estado e){
        simboloInicial= '\0';
        simboloFinal= '\0';
        destino= e;
    }
    
    public char simInicial(){
        return simboloInicial;
    }
    
    public char simFinal(){
        return simboloFinal;
    }
    
    public Estado destino(){
        return destino;
    }
    
    @Override
    public String toString(){
        if(simboloInicial==simboloFinal){
            if(simboloInicial=='\0')
                return " ----"+"\u0190"+"----> "+destino;
            else
                return " ----"+simboloInicial+"----> "+destino;
        }
        else{
            return " ----["+simboloInicial+"-"+simboloFinal+"]----> "+destino;
        }
        
            
    }
    
}
