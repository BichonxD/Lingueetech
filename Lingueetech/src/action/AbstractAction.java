package action;

import exception.ActionHandlerException;
import exception.InvalidParamException;
import exception.InvalidRequestException;
import response.AbstractResponse;
import json.JSONObject;


/**
 * 
 *
 */
public abstract class AbstractAction {
	
	private String name ;
	
	/**
	 * Traite une requete, et renvoi la reponse de celle ci au client.
	 * @throws ActionHandlerException : si une exception survient êndant le traitement de la requete
	 */
	public abstract AbstractResponse handleRequest() throws ActionHandlerException;
	
	
	/**
	 * Initialise les parametres de l'action à partir d'un Objet JSON.
	 *  
	 * @param jsonObject 
	 * 				L'objet à partir duquel initialiser les parametres de l'action.
	 * @throws InvalidRequestException : Si un erreur survient lors de la lecture du fichier JSON
	 * @throws InvalidParamException : Si un paramere est manquant ( alors qu'il est recquis )
	 * 								    ou n'a pas le bon type attendu.
	 */
	public abstract void setRequestFromJSONObject(JSONObject jsonObject) throws InvalidRequestException, InvalidParamException;


	
	public String getName() {
		if ( name == null )
		{
			name = this.getClass().getName().replace("action.", "");
		}
		return name;
	}

}
