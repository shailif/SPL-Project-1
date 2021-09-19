package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		try {
			String input = args[0];
			String output = args[1];
			Diary diary=Diary.getInstance();

			Input json = JsonInputReader.getInputFromJson(input);

			Ewoks Evector =  Ewoks.getInstance(); // ewok i will be at i-1
			for (int i = 1; i <= json.getEwoks(); i++) 
				Evector.getVectorE().add(new Ewok(i));
			

			Thread Leia=new Thread(new LeiaMicroservice(json.getAttacks()));
			Thread  HanSoloMicroservice= new Thread(new HanSoloMicroservice());
			Thread  C3POMicroservice= new Thread(new C3POMicroservice());
			Thread R2D2=new Thread(new R2D2Microservice(json.getR2D2()));
			Thread Lando=new Thread(new LandoMicroservice(json.getLando()));

			Leia.start();
			HanSoloMicroservice.start();
			C3POMicroservice.start();
			R2D2.start();
			Lando.start();

			try{ Lando.join();
			}catch (InterruptedException e){}

			 Gson gson=new GsonBuilder().setPrettyPrinting().create();
			 FileWriter writer=new FileWriter(output);
			 gson.toJson(diary,writer);
			 writer.flush();
			 writer.close();

		}catch (IOException e){
			e.printStackTrace();
		}


	}
}

