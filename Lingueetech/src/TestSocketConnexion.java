import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class TestSocketConnexion {

	public static void main(String[] args) {

		// On lit un fichier contenant un exemple de requete depuis un fichier.
		String phpStringRequest = readFile("data/pipe/php-request-example.json");

		try {
			Socket phpSocket = new Socket(InetAddress.getLocalHost(), 1603);

			BufferedReader phpJavaMsg = new BufferedReader(
					new InputStreamReader(phpSocket.getInputStream())
					);

			PrintWriter phpMsg = 	new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(phpSocket.getOutputStream())),
							true);


			phpMsg.println(phpStringRequest);          // envoi d'un message
			String javaResponse = phpJavaMsg.readLine();
			System.out.println("Reponse Recu : " + javaResponse);

			phpMsg.close();
			phpJavaMsg.close();
			phpSocket.close();
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      

	}


	private static String readFile(String filePath){
		Scanner scanner;
		StringBuilder sb =  new StringBuilder();
		try {
			scanner = new Scanner(new FileReader(filePath));
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine());
			}
			scanner.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}


}
