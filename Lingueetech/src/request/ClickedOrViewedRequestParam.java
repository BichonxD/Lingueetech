package request;

import json.JSONObject;
import exception.InvalidParamException;

public class ClickedOrViewedRequestParam extends AbstractRequestParam {


	private int id;
	private String event;
	public static final String EVENT_VIEWED = "Viewed" ; 
	public static final String EVENT_CLICKED = "Clicked"; 
	
	/**
	 * Instancie une requete avec ses parametres 
	 * @param jsonObject
	 * 				Objet contenant les parametres de la requete de recherche.
	 * @throws InvalidParamException 
	 */
	public ClickedOrViewedRequestParam (JSONObject jsonObject) throws InvalidParamException{
		initWith(jsonObject);
	}
	
	public int getId() {
		return id;
	}

	public String getEvent() {
		return event;
	}


	@Override
	public void initWith(JSONObject jsonObject) throws InvalidParamException {
		// recupere les parametres de la recherche de la requete
		JSONObject requestParam = jsonObject.getJSONObject("request")
				.getJSONObject("params");

		// Initialise l'objet SearchRequestParam
		id = requestParam.getInt("id"); 
		event = requestParam.getString("event");
	}

}
