package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ClaseLexica {
    private ArrayList<ArrayList> diccionario;
    
    public ClaseLexica(){
        diccionario= new ArrayList<>();
    }
    
    public void cargarGramatica(String ruta){
        try{
            BufferedReader br= new BufferedReader(new FileReader(new File(ruta)));
            
            String str="";
            while((str= br.readLine()) != null){
                String[] div= str.split(String.valueOf((char)9)); //Separar con tabulador
                ArrayList lista= new ArrayList();
                lista.add(div[0]); //Debe ser el nombre de la clase lexica
                lista.add(div[1]); //Debe ser la expresión regular
                lista.add(div[2]); //Debe ser el token
                diccionario.add(lista);
            }
                
            br.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
//        System.out.println("\nClase léxica");
//        int i=0;
//        for(ArrayList al: diccionario){
//            for(Object obj: al){
//                if(i<2)
//                    System.out.print((String)obj+", ");
//                else
//                    System.out.print(Integer.parseInt((String)obj)+", ");
//                i+=1;
//            }
//            System.out.println("");
//            i=0;
//        }
    }
}
