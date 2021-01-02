package AnalizadorSintactico;

import java.io.*;
import java.util.*;
public class AccionSemantica{
    public static void main(String[] args) {
        BufferedReader reader;
        BufferedWriter writer;
        String SIMB = "simb";
        String num = "num";
        try {
            /*crea un archivo*/
            File Nfile = new File("AccionSemantica.java");
            if (Nfile.createNewFile()) {
                System.out.println("File created: " + Nfile.getName());
            } else {
                System.out.println("File already exists.");
            }

            reader = new BufferedReader(new FileReader(adaptarRuta("../grambienprueba.txt")));
            writer = new BufferedWriter(new FileWriter(adaptarRuta("../AccionSemantica.java")));

            /*para obtener el numero de filas*/

            String line = reader.readLine();
            int contline = 0;
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
                contline++;
            }
            System.out.println("contline: "+contline);

            reader = new BufferedReader(new FileReader(adaptarRuta("../grambienprueba.txt")));
            line = reader.readLine();
            int c=1;

            //comienza a escribir el nuevo programa
            //esta parte es fija
            writer.write("package AnalizadorSintactico; \n"+
            "\nimport AnalizadorLexico.AFD;\n"+
            "\npublic class AccionSemantica{\n"+
            "\n\tpublic AccionSemantica(AFD afd){\n"+
            "\t\tLexic=afd;\n"+
            "\t}\n");

            while(c<contline){
                String[] splitLine = line.trim().split("\\s+");
                //verifica que tipo de regla es
                String[] nom = splitLine[0].split("");
                int len = nom.length;
                                //System.out.println("nom: "+splitLine[0] +" len: "+len);
                //comienza a escribir los bool
                if(len==1){	//bool de regla tipo E
                    String[] sep = splitLine[2].split("");
                    writer.write("\n\t//"+line+
                    "\n\tprivate boolean "+splitLine[0]+"(){"+
                    "\n\t\tif("+sep[0]+"())"+
                    "\n\t\t\tif("+splitLine[0]+"pr())"+
                    "\n\t\t\t\treturn true;"+
                    "\n\t\treturn false;");


                }else{	//bool de regla tipo E'					
                    writer.write("\n\t//"+line+
                    "\n\t private boolean "+nom[0]+"pr(){"+
                    "\n\t\tint tok;"+
                    "\n\t\ttok=Lexic.yylex();");
                    System.out.println("splitLine len: "+splitLine.length);
                    if(splitLine.length <=5){ //cuando sean solo 2 condiciones 
                            String[] sim = splitLine[2].split(""); 
                            int Slen = sim.length;
                            System.out.println("lenSim: "+Slen);
                            if(Slen>3){	//del tipo orTE'
                                String sim2="";
                                for(int j=0; j<Slen-4; j++){
                                        sim2=sim2+sim[j];
                                }
                                writer.write("\n\t\tif(Tok== '"+sim2+"'){"+
                                "\n\t\t\tif("+sim[Slen-4]+"())"+
                                "\n\t\t\t\tif("+sim[Slen-3]+"pr())"+
                                "\n\t\t\t\t\treturn true;"+
                                "\n\t\t\treturn false;"+
                                "\n\t\t}"+
                                "\n\t\tLexic.RegresarToken();"+
                                "\n\t\treturn true;");
                            }
                    }else{ //cuando hay mas condiciones
                        writer.write("\n\t\tswitch(tok){");						
                        for(int i=2; i<splitLine.length-1; i=i+2){
                                String[] sim = splitLine[i].split("");
                                writer.write("\n\t\t\tcase '"+sim[0]+"':");

                                if(sim.length>3){
                                    writer.write("\n\t\t\t\tif("+sim[1]+"())"+
                                    "\n\t\t\t\t\tif("+sim[2]+"pr())"+
                                    "\n\t\t\t\t\t\treturn true;"+
                                    "\n\t\t\t\treturn false;"+
                                    "\n\t\t\t\tbreak;");
                                }else{									
                                    writer.write("\n\t\t\t\tif("+sim[1]+"pr())"+
                                    "\n\t\t\t\t\treturn true;"+
                                    "\n\t\t\t\treturn false;"+
                                    "\n\t\t\t\tbreak;");
                                }
                        }
                        writer.write("\n\t\t\tdefault:"+
                        "\n\t\t\t\tLexic.RegresarToken();"+
                        "\n\t\t\t\treturn true;"+
                        "\n\t\t\t\tbreak;"+
                        "\n\t\t}");
                    }										
                }				
                writer.write("\n\t}\n");
                line = reader.readLine();
                c++;
            }

            //se escribe el ultimo boolean
            String[] splitLine = line.trim().split("\\s+");
                    //verifica que tipo de regla es
            String[] nom = splitLine[0].split("");
            int len = nom.length;

            writer.write("\n\t//"+line+
            "\n\t private boolean "+nom[0]+"(){"+
            "\n\t\tint tok;"+
            "\n\t\ttok=Lexic.yylex();"+
            "\n\t\tswitch(tok){");

            /*se escriben los case*/
            for(int i=2; i<=splitLine.length-1; i=i+2){				
                String[] sim = splitLine[i].split("");
                /*si inicia con SIMB o NUM*/
                if(sim[0].equalsIgnoreCase("s") || sim[0].equals("n")){
                        if(splitLine[i].contains("SIMB") )
                                writer.write("\n\n\t\t\tcase 'SIMB':");					
                        if(splitLine[i].contains("num"))
                                writer.write("\n\n\t\t\tcase 'num':");
                }else	// inicia "normal" 		|| sim[0].equalsIgnoreCase("s")
                        writer.write("\n\n\t\t\tcase '"+sim[0]+"':");
                int rep=4;
                String t = "\t";
                //cuando es num o simb solo
                if(sim.length==4 && splitLine[i].equalsIgnoreCase("simb")){
                        writer.write("\n\t\t\t\ttok=Lexic.yylex();"+
                                        "\n\t\t\t\tif(tok == 'SIMB'){"+
                                        "\n\t\t\t\t\treturn true;"+
                                        "\n\t\t\t\t}"+
                                        "\n\t\t\t\treturn false;"+
                                        "\n\t\t\t\tbreak;");
                }else if(sim.length==3 && splitLine[i].equalsIgnoreCase("num")){
                        writer.write("\n\t\t\t\ttok=Lexic.yylex();"+
                                        "\n\t\t\t\tif(tok == 'num'){"+
                                        "\n\t\t\t\t\t\treturn true;"+
                                        "\n\t\t\t\t}"+
                                        "\n\t\t\t\treturn false;"+
                                        "\n\t\t\t\tbreak;");
                //cuando son varios
                }else{
                int cont=0;

                    if(!sim[1].equals("S") && !sim[1].equals("n")){

                            writer.write("\n\t\t\t\tif("+sim[1]+"()){"+
                            "\n\t\t\t\t\ttok=Lexic.yylex();");
                            cont++;
                    }
                    for(int j=2;j<sim.length-2; j++){
                        if(sim[j-1].equalsIgnoreCase("s") || sim[j-1].equals("n") || sim[j].equalsIgnoreCase("s") || sim[j].equals("n") ){
                            if(splitLine[i].contains("SIMB") ){
                                writer.write("\n\t\t\t\ttok=Lexic.yylex();"+
                                "\n\t\t\t\tif(tok == 'SIMB'){"+
                                "\n\t\t\t\t\ttok=Lexic.yylex();");	
                                j=j+3;
                                cont++;
                            }if(splitLine[i].contains("num")){
                                writer.write("\n\t\t\t\ttok=Lexic.yylex();"+
                                "\n\t\t\t\tif(tok == 'num'){"+
                                "\n\t\t\t\t\ttok=Lexic.yylex();");
                                j=j+2;
                                cont++;
                            }
                        }

                        if(sim[j].equalsIgnoreCase("B") || sim[j].equalsIgnoreCase("n")){
                            System.out.println("entro aqui");
                            j++;
                        }

                        if(j!=sim.length-1){
                            System.out.println("sim["+j+"]: "+sim[j]);
                            writer.write("\n\t\t\t\tif(tok == '"+sim[j]+"'){");	
                            if(!sim[1].equals("S") && !sim[1].equals("n"))
                                 writer.write("\n\t\t\t\t\ttok=Lexic.yylex();");
                            cont++;
                        }




                    }
                            //rep++;
                    System.out.println("cont: "+cont);
                    writer.write("\n\t\t\t\tif(tok== '"+sim[sim.length-1]+"')"+
                    "\n\t\t\t\t\t\treturn true;");
                    for(int j=1; j<=cont; j++)
                            writer.write("\n\t\t\t\t}");

                    writer.write("\n\t\t\t\treturn false;"+
                    "\n\t\t\t\tbreak;");
                }
            }
            writer.write("\n\n\t\t\tdefault:"+
            "\n\t\t\t\tLexic.RegresarToken(tok);"+
            "\n\t\t\t\treturn true;"+
            "\n\t\t\t\tbreak;"+
            "\n\t\t}");


            writer.write("\n\t\t}"+
            "\n\t}");

            writer.write("\n}");
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}