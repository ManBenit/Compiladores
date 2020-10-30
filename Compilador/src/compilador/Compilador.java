package compilador;

import AnalizadorLexico.AFN;
import AnalizadorLexico.ClaseLexica;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compilador {

    public static void main(String[] args) {
        //Algún operador, paréntesis o algún símbolo específico (se representa con una letra)
        Pattern patron= Pattern.compile("\\*|\\+|\\?|[|]|\\(|\\)|[A-Z]");
        
        ClaseLexica.cargarClasesLexicas(adaptarRuta("../DescriptorClases/claslex.txt"));
        String[] clases= ClaseLexica.obtNomClases();
        
        //Crear AFN a partir de una expresión regular
        ArrayList<String> regexs= new ArrayList();
        for(String c: clases)
            regexs.add(ClaseLexica.obtExpReg(c));
        
        
//        for(String s: regexs)
//            System.out.println(s);
        
        
        //Creación de autómatas
//        ArrayList<Character> alfa= new ArrayList();
//        for(String expreg: regexs){ //Para cada expresión regular,
//            System.out.println("Regex: "+expreg);
//            char[] partesRegex= expreg.toCharArray();
//            Matcher matcher;
//            
//            for(int i=0; i<partesRegex.length; i++){
//                
//                matcher= patron.matcher(String.valueOf(partesRegex[i]));
//                if(matcher.find()){ //mientras coincida
//                    char encontrado= matcher.group().charAt(0);
//                    if((int)encontrado>=65 && (int)encontrado<=90)
//                        
//                    System.out.println(matcher.group().charAt(0));
//                }
//                else{
//                    alfa.add(partesRegex[i]);
//                }
//                
//            }
//                
//        }
//        System.out.println("****");
//        for(char c: alfa)
//            System.out.print(c+"- ");
    }
    
    
    private static boolean coincide(char c){
        
        
        
        
        return false;
    }
    
    
    private static void escribir(AFN afn, String ruta){
        try{
            FileOutputStream f= new FileOutputStream(new File(ruta));
            f.write(afn.toString().getBytes());
        }catch(IOException ex){
            System.out.println("Fallo al escribir\n"+ex);
        }
    }
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
    
}
