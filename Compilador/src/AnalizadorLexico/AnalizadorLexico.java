package AnalizadorLexico;

import java.util.ArrayList;
import java.util.LinkedList;

public class AnalizadorLexico {
    private int tokenRecordado;
    private String lexemaRecordado;
    private LinkedList<Integer> lexList;
    private LinkedList<String> texList;
    private LinkedList<Estado> estados;
    private ArrayList<Character> alfabeto;
    private int[][] tablaEstados;
    
    public AnalizadorLexico(AFD afd){
        lexList= new LinkedList<>();
        texList= new LinkedList<>();
        estados= afd.estados();
        alfabeto= afd.alfabeto();
        tablaEstados= afd.tablaDeEstados();
    }
    
    
    public boolean validarCadena(String cadena){
        boolean valida=true;
        ArrayList<Integer> idEdos= new ArrayList<>();
        for(Estado e: estados)
            idEdos.add(e.id());
        StringBuilder sb= new StringBuilder(""); //Evita el uso de índices de fin e inicio
        char[] cad= cadena.toCharArray();
        int indEstadoActual=0; //Recorre las filas
        boolean previoAceptado= false;
        int contRechazo=0;//Para evitar bucles infinitos en caso de error
        
        int token=0;
        String lexema= "";
        
        //Si el caracter actual es el fin de la cadena
        
        //Analizar cada caracter
        int inicio=0, fin=0, indiceCadena=0;
        while(indiceCadena<cad.length){
            //Se aumenta 1 la posición de la culumna debido a que la columna 0 es la de los estados de arranque
            int posible= tablaEstados[indEstadoActual][alfabeto.indexOf(cad[indiceCadena])+1]; 
            if(posible>-1){ //Si hay transicion de estadoActual con c a algún estado
                indEstadoActual=idEdos.indexOf(posible);
                
                int auxToken= tablaEstados[indEstadoActual][alfabeto.size()+1]; //tabla[indiceAct][ultima columna]
                //System.out.println("token: "+token);
                if(auxToken>0){ //Si es un estado de aceptación
                    //posRecordada=indiceCadena;
                    token=auxToken;
                    previoAceptado=true;
                    sb.append(cad[indiceCadena]);
                }
                else{
                    if(previoAceptado){
                        sb.append(cad[indiceCadena]);
//                        recToken=token;
                        
                        previoAceptado=false;
                    }
                }
                
                indiceCadena+=1; //Se pasa al siguiente caracter
            }
            else{ //No hubo transición
                if(!previoAceptado){ //Si no ha habido aceptación, hay error
//                    System.out.printf("Rechazado %c, posición %d\n", cad[indiceCadena], indiceCadena);
                    sb.delete(0, sb.length());
                    indEstadoActual=0;
//                    indiceCadena=0;
                    contRechazo+=1;
                }
                else{
                    //Regresar al último índice con aceptación
                    //indiceCadena=posRecordada;
                    lexema= sb.toString();
                    //System.out.printf("[ Lexema: %s , Token: %d ]\n", lexema, token);
                    //yyList.put(lexema, token);
                    texList.push(lexema); //Aquí avisar que hay un nuevo lexema encontrado****************************
                    lexList.push(token);
                    sb.delete(0, sb.length());
                    previoAceptado=false;
                }
            }
            
            //En el peor caso, todos los símbolos de la cadena representan un lexema de un solo caracter,
            //por lo que el número de rechazos en el análisis es menor a la longitud de la cadena,
            //por lo tanto, en cualquier otro caso, el número es siempre menor, en caso contrario,
            //se está empezando un ciclo infinito, ahí la cadena se invalida.
            //(Esto es sobretodo para controlar el punto, poque dos puntos fuera de lugar hacen cciclo infinito)
            //(Esta condición debe desaparecer, ya que no debe ser necesaria)
            if(contRechazo>=cadena.length()){
                valida=false;
                break;
            }
                
            
            
        }
        
        //Cuando se acaba la cadena, ver el último lexema
        if( !(lexema=sb.toString()).equals("") && valida){
            //System.out.printf("[ Lexema: %s , Token: %d ]\n", lexema, token);
            texList.push(lexema);//Aquí avisar que hay un nuevo lexema encontrado****************************
            lexList.push(token);
        }
        
        
        return valida;
    }
    
    public int yylex(){ //Al detectar que se llama este o yytex, verificar si hay algo disponible, si no, analizar hasta que haya algo
        tokenRecordado= lexList.pop();
        return tokenRecordado;
    }
    
    public String yytex(){
        lexemaRecordado= texList.pop();
        return lexemaRecordado;
    }
    
    public void regresarToken(){
        lexList.push(tokenRecordado);
        if(!lexemaRecordado.equals(""))
            texList.push(lexemaRecordado);
    }
}
