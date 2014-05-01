package action;


import exception.ActionHandlerException;

import exception.InvalidParamException;
import exception.InvalidRequestException;
import json.JSONObject;
import request.SearchRequestParam;
import response.AbstractResponse;
import response.SuccessResponse;

/**
 * 
 *
 */
public class SearchAction extends AbstractAction {

	/**
	 * Objet contenant les parametres de recherche de l'utilisateur
	 */
	private SearchRequestParam searchParam; 


	@Override
	public AbstractResponse handleRequest() throws ActionHandlerException{
		int l ;
		// Calcul demandé par la requete ... 
	
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Création de la reponse 
		JSONObject jsonResponse = new JSONObject();

		// Initialiser l'objet JSON
		jsonResponse.accumulate("sentences", "Le Jambon pour quoi pas ?");
		jsonResponse.accumulate("sentences", "J'aime le Jambon-beurre.");
		
		// Retour de la reponse au client 
		SuccessResponse successResponse = new SuccessResponse(jsonResponse);
		return successResponse;	

	}

	@Override
	public void setRequestFromJSONObject(JSONObject jsonObject) throws InvalidRequestException, InvalidParamException{

		// Si les parametres de la recherches n'existent pas
		if ( searchParam == null )
			searchParam = new SearchRequestParam(jsonObject);

		else // Sinon on lmes initialise.
			searchParam.initWith(jsonObject);
	}
}
