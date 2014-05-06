package action;

import exception.ActionHandlerException;
import exception.InvalidParamException;
import exception.InvalidRequestException;
import json.JSONArray;
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
		/*
		Index index = new Index();
		index.tokenize("Files/test_sentences.txt");
		KnowledgeDictionary dico = new KnowledgeDictionary(index);
		Search srch = new Search(index, dico);
		
		
		TreeSet<Integer> tree = srch.search(searchParam.getQuery());
		*/
		// Création de la reponse 
		JSONObject jsonResponse = new JSONObject();
		
		// Initialiser l'objet JSON
		/*
		sentences = [ {
			id = 
			text = 
			tokens = [{ id, lemma, text, score} , { } , { } , { }]
			score = 
			favorite = bool
		}]
		*/
		
 		
		// Parcourir les phrases
		// Parcourir les tokens de la phrase
		JSONArray tokens = new JSONArray();
			JSONObject token = new JSONObject();
			token.put("id", 123123);
			token.put("lemma", "Hello");
			token.put("text", "Hello Token");
			token.put("score", 123123);
		tokens.put(token);
				
		JSONObject sentence = new JSONObject();
			sentence.put("id", 123);
			sentence.put("text", "text");
			sentence.put("tokens", tokens);
			sentence.put("score", 123);
			sentence.put("favorite", false);
			jsonResponse.accumulate("sentences", sentence);
			jsonResponse.accumulate("sentences", sentence);
		
 		
 		
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
