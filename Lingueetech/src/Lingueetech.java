import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import action.AbstractAction;
import action.ClikedOrViewedAction;
import action.SearchAction;
import json.JSONObject;
import json.JSONTokener;
import response.AbstractResponse;
import response.ErrorResponse;
import exception.ActionNotFoundException;
import exception.LingueetechException;


/**
 * Classe principale de l'application JAVA 
 * 
 *  
 *
 */
public class Lingueetech {

	/**
	 * Table  de correspondance entre le nom des actions et des  objets qui en 
	 * sont responsable.Ce champ contient des paires, <nomAction, ObjetAction> 
	 * où ObjetAction est une sous classe de AbstractAction capable de gérer
	 * l'action dont le nom est nomAction. 
	 * 
	 * Ce champ pourra contenir par exemple : <"Search" , new SearchAction()>
	 */
	private static HashMap<String, AbstractAction> actionMatchingTable;




	public static void main(String[] args) {

		// Initialise L'application Lingueetech
		Lingueetech.initialize();

		// Traite toutes les requetes entrantes des utilisateurs 
		Lingueetech.handleAllRequest();
	}


	/**
	 * Traite une requete renvoi une reponse et se met en attente d'une nouvelle 
	 * requete utilisateur.
	 * 
	 * TO DO : Pour le moment cette fonction ne traite qu'une requete, il 
	 * 		   faudrait faire en sorte qu'elle se mette attente des suivantes.
	 */
	private static void handleAllRequest() {
		ServerSocket serverSocket  ;
		Socket phpSocket ;
		while ( true )
		try {
			serverSocket = new ServerSocket(1603);
			System.out.println("L'application JAVA est à l'écoute du port : " + serverSocket.getLocalPort());
			phpSocket = serverSocket.accept(); 
			BufferedReader phpRequestBuffer = new BufferedReader(new InputStreamReader(phpSocket.getInputStream()));

			PrintWriter phpSocketMsg = 	new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(phpSocket.getOutputStream())), 
							true);

			// Lecture du message transmit par PHP
			String request = phpRequestBuffer.readLine(); 

			// Traitement de la requete utilisateur
			AbstractResponse response = Lingueetech.handleRequest(request); 

			// Renvoi de la reponse au client
			phpSocketMsg.println(response.toJSONString());                     

			// Fermeture des conexions
			phpRequestBuffer.close();
			phpSocketMsg.close();
			serverSocket.close();

		}
		catch (IOException e) {

			e.printStackTrace();
		}

	}


	/**
	 * Initialize l'application Lingueetech
	 */
	private static void initialize() {

		// Initialise la table de correspondance des action et des objets qui 
		// en sont responsable.
		actionMatchingTable = new HashMap<>();
		actionMatchingTable.put("Search", new SearchAction());	
		actionMatchingTable.put("ClickedOrViewed", new ClikedOrViewedAction());	
		
		
		
		// Autres Initialisations .... 
	}


	/**
	 * Recupere l'objet Action sachant gérer l'action dont le nom est passé en
	 * parametre.
	 * 
	 * @param String actionName
	 * 			Nom de l'action dont on recherche l'objet 
	 * @return AbstractAction si un objet correspondant a été trouvé et null sinon
	 * 		
	 */
	private static AbstractAction getActionFor(String actionName) throws ActionNotFoundException {

		AbstractAction matchedAction; 
		if ( !actionMatchingTable.containsKey(actionName) )
			throw new ActionNotFoundException("L'action : " + actionName + " n'est pas enregistrée sur le serveur") ; 

		matchedAction = actionMatchingTable.get(actionName) ; 
		return matchedAction; 
	}

	
	private static AbstractResponse handleRequest(String stringRequest) {
		AbstractResponse javaResponse = null;
		try {
			// On recupere l'objet JSON contenant la requete utilisateur
			JSONObject jsonRequest = new JSONObject(
					new JSONTokener(stringRequest)
					);
			// Log
			System.out.println("Requete Utilisateur : " + jsonRequest.toString()); 

			// On recupere l'action voulue par l'utilisateur
			String requestedAction = jsonRequest.getJSONObject("request")
					.getString("action");

			// Log
			System.out.println("RequestedAction : " + requestedAction); 

			// On recupere l'objet Action sachant gérer la requete de l'utilisation 
			AbstractAction matchedAction = Lingueetech.getActionFor(requestedAction);

			// Log
			System.out.println("ObjectAction Correspondant : " + matchedAction.getName()); 

			// Initialise l'AobjetAction trouvé avec la requete utilisateur
			matchedAction.setRequestFromJSONObject(jsonRequest);

			// Gere la requete et renvoi une reponse au client
			javaResponse = matchedAction.handleRequest();
		}
		catch (Exception e){
			//e.printStackTrace();

			// Initialise une reponse d'erreur à partir le l'exception : invReqException
			javaResponse = new ErrorResponse(e);
		}
		
		// Log
		System.out.println("Reponse  : " + javaResponse.toJSONString()); 
		System.out.println(); 
		
		return javaResponse;
	
	}
}