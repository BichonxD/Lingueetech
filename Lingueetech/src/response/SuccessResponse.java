/**
 * 
 */
package response;

import json.JSONArray;
import json.JSONObject;

/**
 *
 */
public class SuccessResponse  extends AbstractResponse{

	private JSONObject jsonResponse;
	

	public SuccessResponse(JSONObject jsonResponse){
		this.jsonResponse = new JSONObject();
		this.jsonResponse.put("response", jsonResponse);
	}

	public SuccessResponse(JSONArray jsonResponse){
		this.jsonResponse = new JSONObject();
		this.jsonResponse.put("response", jsonResponse);
	}


	@Override
	public String toJSONString() {
		return jsonResponse.toString();
	}
}
