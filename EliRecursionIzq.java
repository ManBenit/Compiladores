import java.io.*;


public class EliRecursionIzq {

	public static void main(String[] args) {
		BufferedReader reader;
		BufferedWriter writer;
		try {
			/*crea un archivo*/
			File Nfile = new File("grambienprueba.txt");
			if (Nfile.createNewFile()) {
				System.out.println("File created: " + Nfile.getName());
			} else {
				System.out.println("File already exists.");
			}
				
			reader = new BufferedReader(new FileReader("/Users/Jessy/Desktop/gramprueba.txt"));
			writer = new BufferedWriter(new FileWriter("/Users/Jessy/Desktop/grambienprueba.txt"));
			
			/*para obtener el numero de filas*/
			
			String line = reader.readLine();
			int contline = 0;
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
				contline++;
			}
			//System.out.println("contline: "+contline);
			
			reader = new BufferedReader(new FileReader("/Users/Jessy/Desktop/gramprueba.txt"));
			line = reader.readLine();
			int c=1;
			while(c<contline){
				boolean ban=false;
				boolean prim=false;
				String[] splitLine = line.trim().split("\\s+");
				int sizeSL = splitLine.length;
				
				
				
				for(int i=2; i<sizeSL; i=i+2){
					String[] sepPriR = splitLine[i].split("");
					if(splitLine[0].equals(sepPriR[0])){
						ban=true;
						if(i==2)
							prim=true;
					}
				}
				
				
				/*cuando necesita recursion*/
				if(ban){
					
				
					//System.out.println("count: "+sizeSL);
					writer.write(splitLine[0]+" "+splitLine[1]+" "); // E -> 
					String simb = "";
					if(prim){
						simb = splitLine[sizeSL-1]+splitLine[0]+"'";
							System.out.println("simb: "+simb);
						  writer.write(simb+"\n"+splitLine[0]+"' "+splitLine[1]+" ");
					}else{
						String[] sep = splitLine[4].split("");
						String pa="";
						for(int j=1; j<sep.length; j++){
							pa=pa+sep[j];
						}
						//simb = pa+splitLine[0]+"'";
						simb= sep[sep.length-1]+sep[0]+"'";
						writer.write(simb+"\n"+splitLine[0]+"' "+splitLine[1]+" ");
					}	 
				
					for(int i=2; i<sizeSL; i=i+2){
								System.out.println("\ti:"+i);
						String[] sim = splitLine[i].split("");
						int len = sim.length;										
							System.out.println("\tsplitline: "+splitLine[i]+" , len: "+len);
						
						 if(len==2) //EJ. E+
							writer.write(sim[1]+sim[0]+"' | ");
						else if(len==1)
							writer.write("");
						else{		//EJ. EorT
							String sim2="";
							for(int j=1; j<len-1; j++){
								sim2=sim2+sim[j];
							}
					
							writer.write(sim2+sim[len-1]+sim[0]+"' | ");
						
							
						}
					}
					writer.write("ep");
				}else{
					writer.write(line);
				}
				
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
}