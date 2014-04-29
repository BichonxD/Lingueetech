package request;

import exception.InvalidParamException;
import json.JSONObject;

public final class SearchRequestParam extends AbstractRequestParam{

	/**
	 * Expression saisie par l'utilisateur
	 */
	private String query;

	
	/**
	 * Instancie une requete avec ses parametres 
	 * @param jsonObject
	 * 				Objet contenant les parametres de la requete de recherche.
	 * @throws InvalidParamException 
	 */
	public SearchRequestParam (JSONObject jsonObject) throws InvalidParamException{
		initWith(jsonObject);
	}


	/**
	 * Recupere l'expression saisie par l'utilisateur
	 * @return String
	 */
	public String getQuery() {
		return query;
	}


	@Override
	public void initWith(JSONObject jsonObject) throws InvalidParamException{

		// recupere les parametres de la recherche de la requete
		JSONObject requestParam = jsonObject.getJSONObject("request")
				.getJSONObject("params");

		// Initialise l'objet SearchRequestParam
		query = requestParam.getString("query");

	}
}