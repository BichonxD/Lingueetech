package response;

import json.JSONString;

/**
 * 
 * @author Serge Martial NGUETTA
 *
 */
public abstract class AbstractResponse implements JSONString{
	
	
	/**
	 * Convertit l'objet reponse vers une chaine de caracteres au format JSON
	 * @return String
	 */
	public abstract String toJSONString();
}
