package AnalizadorSintactico;

import java.io.*;


public class EliRecursionIzq {

    public static void main(String[] args) {
        BufferedReader reader;
        BufferedWriter writer;
        try {
            /*crea un archivo*/
            File Nfile = new File("gramnorec.txt");
            if (Nfile.createNewFile()) {
                System.out.println("File created: " + Nfile.getName());
            } else {
                System.out.println("File already exists.");
            }

            reader = new BufferedReader(new FileReader(adaptarRuta("../gramrec.txt")));
            writer = new BufferedWriter(new FileWriter(adaptarRuta("../gramnorec.txt")));

            /*para obtener el numero de filas*/

            String line = reader.readLine();
            int contline = 0;
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
                contline++;
            }
            //System.out.println("contline: "+contline);

            reader = new BufferedReader(new FileReader(adaptarRuta("../gramrec.txt")));
            line = reader.readLine();
            int c=1;
            while(c<contline){
                String[] splitLine = line.trim().split("\\s+");
                int sizeSL = splitLine.length;
                //System.out.println("count: "+sizeSL);
                writer.write(splitLine[0]+" "+splitLine[1]+" ");

                String simb = splitLine[sizeSL-1]+splitLine[0]+"'";
                //System.out.println("simb: "+simb);
                writer.write(simb+"\n"+splitLine[0]+"' "+splitLine[1]+" ");

                for(int i=2; i<sizeSL-1; i=i+2){	
                    String[] sim = splitLine[i].split("");
                    int len = sim.length;										
                    //System.out.println("sim: "+sim[1]+" , len: "+len);

                    if(len==3)	// EJ. E+T
                        writer.write(sim[1]+simb+"| ");
                    else if(len==2) //EJ. E+
                        writer.write(sim[1]+sim[0]+"| ");
                    else{		//EJ. EorT
                        String sim2="";
                        for(int j=1; j<len-1; j++){
                            sim2=sim2+sim[j];
                        }
                        writer.write(sim2+simb+"| ");
                    }
                }
                writer.write("ep");


                // read next line
                line = reader.readLine();
                writer.write("\n");
                c++;
            }
            writer.write(line);


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