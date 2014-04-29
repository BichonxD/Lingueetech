package request;

import exception.InvalidParamException;
import json.JSONObject;

/**
 * 
 *
 */
public abstract class AbstractRequestParam {
	
	/**
	 * Initialise l'objet contenant les paramètres d'une requete à partir d'un
	 * Objet JSON.
	 * 
	 * @param JSONObject
	 * @throws InvalidParamException 
	 */
	public abstract void initWith(JSONObject jsonObject) throws InvalidParamException;
	
}
