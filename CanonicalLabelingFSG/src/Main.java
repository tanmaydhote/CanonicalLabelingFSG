

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		//	String path = System.getProperty("user.dir")+"/src/gSpanSample/aido99_all.txt";
			String path = "/home/tanmay/workspace/CanonicalLabelingFSG/data/aido99_all.txt";
			BufferedReader fileReader = new BufferedReader(new FileReader(path));
			PrintWriter writer = new PrintWriter("gSpanInput.txt", "UTF-8");
			@SuppressWarnings("unused")
			String graphID;
			int total = 0;
			while((graphID = fileReader.readLine()) != null){
				total++;
				writer.println("t # "+total);
				int n = Integer.parseInt(fileReader.readLine());
				for(int i = 0; i < n; i++){
					String l = fileReader.readLine();
					writer.println("v "+i+" "+ l);
				}
				int e = Integer.parseInt(fileReader.readLine());
				for(int i = 0; i < e; i++) {
					String l = fileReader.readLine();
					StringTokenizer stt = new StringTokenizer(l, " ");
					int s = Integer.parseInt(stt.nextToken().toString());
					int d = Integer.parseInt(stt.nextToken().toString());
					int el = Integer.parseInt(stt.nextToken().toString());
					writer.println("e "+s + " " + d +" "+ el);
				}
			}
		}
		catch(Exception e){
			System.out.println("Error: "+e);
		}
	}

}
