package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClaseLexicaPrueba {
    //El ArrayList<Object> será de tamaño 3: String nombre, String exp reg, Integer token
    private static ArrayList<ArrayList<Object>> clases= new ArrayList<>();
    //Las posiciones a continuación se definen de acuerdo a donde se encuentre cada
    //elemento (nombre, expresión regular, token) dentrod el archivo a cargar
    private static final int POS_NOM=0, POS_EXPREG=1, POS_TOKEN=2;
    
    
    public static void cargarClasesLexicas(String ruta){
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(ruta)));
            
            String str="";
            while((str= br.readLine()) != null){
                String[] div= str.split("¬");
                ArrayList<Object> a= new ArrayList<>();
                a.add(div[POS_NOM]);
                a.add(div[POS_EXPREG]);
                a.add(Integer.parseInt(div[POS_TOKEN]));
                clases.add(a);
            }
                
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static int obtNumClases(){
        return clases.size();
    }
    
    public static int obtToken(String nomClase){
        int ret=0;
        for(int i=0; i<clases.size(); i++)
            if(clases.get(i).contains(nomClase)){
                ret= (Integer) clases.get(i).get(POS_TOKEN);
                break;
            }
        
        return ret;
    }
    
    public static String obtExpReg(String nomClase){
        String ret="";
        for(int i=0; i<clases.size(); i++)
            if(clases.get(i).contains(nomClase)){
                ret= (String) clases.get(i).get(POS_EXPREG);
                break;
            }
        
        return ret;
    }
    
    public static String[] obtNomClases(){
        String[] ret= new String[clases.size()];
        
        for(int i=0; i<clases.size(); i++)
            ret[i]= (String)clases.get(i).get(POS_NOM);
        
        return ret;
    }
    
    public static ArrayList<Character> obtAlfabeto(){
        ArrayList<Character> ret= new ArrayList<>();
        StringBuilder sb= new StringBuilder("");
        Pattern patron;
        Matcher matcher;
        
        for(ArrayList<Object> a: clases)
            sb.append(a.get(POS_EXPREG));
        
//        System.out.println(sb.toString());
        
        //Definir el patrón
        patron= Pattern.compile("[A-Z]|\\."); //Normalmente que sean mayúsculas

        //Definir un encontrador de ese patrón
        matcher= patron.matcher(sb.toString());
        
        while(matcher.find()){
            char c= matcher.group().charAt(0);
            if(!ret.contains(c))
                ret.add(c);
        }
        
        return ret;
    }
}
