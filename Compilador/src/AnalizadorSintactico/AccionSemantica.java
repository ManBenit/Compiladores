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
            File Nfile = new File("AccionSemantica2.java");
            if (Nfile.createNewFile()) {
                System.out.println("File created: " + Nfile.getName());
            } else {
                System.out.println("File already exists.");
            }

            reader = new BufferedReader(new FileReader(adaptarRuta("../grambienprueba.txt")));
            writer = new BufferedWriter(new FileWriter(adaptarRuta("./src/AnalizadorSintactico/AccionSemantica2.java")));

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
            "\nimport AnalizadorLexico.AFN;\n"+ 
            "\nimport AnalizadorLexico.AnalizadorLexico;\n"+
            "\npublic class AccionSemantica2{\n"+
            "\n\tAnalizadorLexico lexic;\n"+
            "\n\tpublic static final int OR=1, CONC=2, CERR_POS=3, CERR_KLEENE=4, OPC=5, PAR_I=6, PAR_D=7, CORCH_I=8, CORCH_D=9, GUION=10, SIMB=11, FIN=12, NUM=13;\n"+
            "\n\tpublic AccionSemantica2(AFD afd){\n"+
            "\t\tlexic=new AnalizadorLexico(afd);\n"+
            "\t}\n"+
            "\tpublic static void main(String[] args){\n}");
            //or    -> OR toUpperCase
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
                    "\n\tprivate boolean "+splitLine[0]+"(AFN f){"+
                    "\n\t\tif("+sep[0]+"(f))"+
                    "\n\t\t\tif("+splitLine[0]+"pr(f))"+
                    "\n\t\t\t\treturn true;"+
                    "\n\t\treturn false;");


                }else{	//bool de regla tipo E'					
                    writer.write("\n\t//"+line+
                    "\n\t private boolean "+nom[0]+"pr(AFN f){"+
                    "\n\t\tint tok;"+
                    "\n\t\ttok=lexic.yylex();");
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
                                //if(sim2.equals("or"))
                                String txt=casos(sim2);
                                writer.write("\n\t\tif(tok=="+txt+" ){"+
                                "\n\t\t\tif("+sim[Slen-4]+"(f))"+
                                "\n\t\t\t\tif("+sim[Slen-3]+"pr(f))"+
                                "\n\t\t\t\t\treturn true;"+
                                "\n\t\t\treturn false;"+
                                "\n\t\t}"+
                                "\n\t\tlexic.regresarToken();"+
                                "\n\t\treturn true;");
                            }
                    }else{ //cuando hay mas condiciones
                        writer.write("\n\t\tswitch(tok){");						
                        for(int i=2; i<splitLine.length-1; i=i+2){
                                String[] sim = splitLine[i].split("");
                                //writer.write("\n\t\t\tcase '"+sim[0]+"':");
                                String txt=casos(sim[0]);
                                writer.write("\n\n\t\t\tcase "+txt+":");
                                if(sim.length>3){
                                    writer.write("\n\t\t\t\tif(''"+sim[1]+"'(f))"+
                                    "\n\t\t\t\t\tif("+sim[2]+"pr(f))"+
                                    "\n\t\t\t\t\t\treturn true;"+
                                    "\n\t\t\t\treturn false;"/*+
                                    "\n\t\t\t\tbreak;"*/);
                                }else{									
                                    writer.write("\n\t\t\t\tif("+sim[1]+"pr(f))"+
                                    "\n\t\t\t\t\treturn true;"+
                                    "\n\t\t\t\treturn false;"/*+
                                    "\n\t\t\t\tbreak;"*/);
                                }
                        }
                        writer.write("\n\t\t\tdefault:"+
                        "\n\t\t\t\tlexic.regresarToken();"+
                        "\n\t\t\t\treturn true;"/*+
                        "\n\t\t\t\tbreak;"*/+
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
            "\n\t private boolean "+nom[0]+"(AFN f){"+
            "\n\t\tint tok;"+
            "\n\t\ttok=lexic.yylex();"+
            "\n\t\tswitch(tok){");

            /*se escriben los case*/
            for(int i=2; i<=splitLine.length-1; i=i+2){				
                String[] sim = splitLine[i].split("");
                /*si inicia con SIMB o NUM*/
                if(sim[0].equalsIgnoreCase("s") || sim[0].equals("n")){
                        if(splitLine[i].contains("SIMB") )
                                writer.write("\n\n\t\t\tcase SIMB:");					
                        if(splitLine[i].contains("num"))
                                writer.write("\n\n\t\t\tcase NUM:");
                }else{	// inicia "normal" 		|| sim[0].equalsIgnoreCase("s")
                    String txt=casos(sim[0]);
                    writer.write("\n\n\t\t\tcase "+txt+":");
                }
                int rep=4;
                String t = "\t";
                //cuando es num o simb solo
                if(sim.length==4 && splitLine[i].equalsIgnoreCase("simb")){
                        writer.write("\n\t\t\t\ttok=lexic.yylex();"+
                                        "\n\t\t\t\tif(tok == SIMB){"+
                                        "\n\t\t\t\t\treturn true;"+
                                        "\n\t\t\t\t}"+
                                        "\n\t\t\t\treturn false;"/*+
                                        "\n\t\t\t\tbreak;"*/);
                }else if(sim.length==3 && splitLine[i].equalsIgnoreCase("num")){
                        writer.write("\n\t\t\t\ttok=lexic.yylex();"+
                                        "\n\t\t\t\tif(tok == NUM){"+
                                        "\n\t\t\t\t\t\treturn true;"+
                                        "\n\t\t\t\t}"+
                                        "\n\t\t\t\treturn false;"/*+
                                        "\n\t\t\t\tbreak;"*/);
                //cuando son varios
                }else{
                int cont=0;

                    if(!sim[1].equals("S") && !sim[1].equals("n")){

                            writer.write("\n\t\t\t\tif("+sim[1]+"(f)){"+
                            "\n\t\t\t\t\ttok=lexic.yylex();");
                            cont++;
                    }
                    for(int j=2;j<sim.length-2; j++){
                        if(sim[j-1].equalsIgnoreCase("s") || sim[j-1].equals("n") || sim[j].equalsIgnoreCase("s") || sim[j].equals("n") ){
                            if(splitLine[i].contains("SIMB") ){
                                writer.write("\n\t\t\t\ttok=lexic.yylex();"+
                                "\n\t\t\t\tif(tok == SIMB){"+
                                "\n\t\t\t\t\ttok=lexic.yylex();");	
                                j=j+3;
                                cont++;
                            }if(splitLine[i].contains("num")){
                                writer.write("\n\t\t\t\ttok=lexic.yylex();"+
                                "\n\t\t\t\tif(tok == NUM){"+
                                "\n\t\t\t\t\ttok=lexic.yylex();");
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
                            String txt=casos(sim[j]);
                    //writer.write("\n\n\t\t\tcase "+txt+":");
                            writer.write("\n\t\t\t\tif(tok == "+txt+"){");	
                            if(!sim[1].equals("S") && !sim[1].equals("n"))
                                 writer.write("\n\t\t\t\t\ttok=lexic.yylex();");
                            cont++;
                        }




                    }
                            //rep++;
                    System.out.println("cont: "+cont);
                    String txt=casos(sim[sim.length-1]);
                    writer.write("\n\t\t\t\tif(tok== "+txt+")"+
                    "\n\t\t\t\t\t\treturn true;");
                    for(int j=1; j<=cont; j++)
                            writer.write("\n\t\t\t\t}");

                    writer.write("\n\t\t\t\treturn false;"/*+
                    "\n\t\t\t\tbreak;"*/);
                }
            }
            writer.write("\n\n\t\t\tdefault:"+
            "\n\t\t\t\tlexic.regresarToken();"+
            "\n\t\t\t\treturn true;"/*+
            "\n\t\t\t\tbreak;"*/+
            "\n\t\t}");


            writer.write("\n\t\t}"+
            "\n\t}");

            //writer.write("\n}");
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String casos(String ini){
        //OR=1, CONC=2, CERR_POS=3, CERR_KLEENE=4, OPC=5, PAR_I=6, PAR_D=7, CORCH_I=8, CORCH_D=9, GUION=10, SIMB=11, FIN=12, NUM=13, SUM=14, MULTI=15;

        if (ini.equalsIgnoreCase("("))
            return "PAR_D";//                                
        if (ini.equalsIgnoreCase(")"))
            return "PAR_I";
        if (ini.equalsIgnoreCase("["))
            return "CORCH_D";
        if (ini.equalsIgnoreCase("]"))
            return "CORCH_I";
        if (ini.equalsIgnoreCase("+"))
            return "CERR_POS";
        if (ini.equalsIgnoreCase("-"))
            return "GUION";
        if (ini.equalsIgnoreCase("*"))
            return "CERR_KLEENE";
        if (ini.equalsIgnoreCase("?"))
            return "OPC";
        if (ini.equalsIgnoreCase("/"))
            return "SUM";
        if (ini.equalsIgnoreCase("or"))
            return "OR";
        if (ini.equalsIgnoreCase("&"))
            return "CONC";
        else
            return ini;
    }
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }
}