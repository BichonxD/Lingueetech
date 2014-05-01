package response;

import json.JSONObject;

/**
 * 
 *
 */
public class ErrorResponse extends AbstractResponse {

	private JSONObject jsonResponse;


	public ErrorResponse(Exception e){

		JSONObject errorObject = new JSONObject();
		errorObject.put("message", e.getMessage());
		errorObject.put("type", e.getClass().getName().replace("exception.", ""));
		
		this.jsonResponse = new JSONObject();
		this.jsonResponse.put("response", new JSONObject());
		this.jsonResponse.getJSONObject("response").put("error", errorObject );
	}

	@Override 
	public String toJSONString(){
		return jsonResponse.toString();
	}
}
