package action;

import json.JSONArray;
import json.JSONObject;
import request.ClickedOrViewedRequestParam;
import request.SearchRequestParam;
import response.AbstractResponse;
import response.SuccessResponse;
import exception.ActionHandlerException;
import exception.InvalidParamException;
import exception.InvalidRequestException;

public class ClikedOrViewedAction extends AbstractAction {

	/**
	 * Objet contenant les parametres de recherche de l'utilisateur
	 */
	private ClickedOrViewedRequestParam cOvParam; 

	@Override
	public AbstractResponse handleRequest() throws ActionHandlerException {
		 
		
		if ( cOvParam.getEvent().equals(ClickedOrViewedRequestParam.EVENT_VIEWED) ){
			
		}
		else if ( cOvParam.getEvent().equals(ClickedOrViewedRequestParam.EVENT_CLICKED) ) {
			
		}
 
		// Retour de la reponse au client 
		SuccessResponse successResponse = new SuccessResponse(new JSONObject());
		return successResponse;	
	}

	@Override
	public void setRequestFromJSONObject(JSONObject jsonObject)
			throws InvalidRequestException, InvalidParamException {

		// Si les parametres de la recherches n'existent pas
		if ( cOvParam == null )
			cOvParam = new ClickedOrViewedRequestParam(jsonObject);

		else // Sinon on lmes initialise.
			cOvParam.initWith(jsonObject);

	}

}
