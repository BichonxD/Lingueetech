package action;


import java.util.TreeSet;

import core.KnowledgeGraph.KnowledgeDictionary;
import core.eng.Index;
import core.searchUtil.Search;
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

		// Calcul demandé par la requete ... 
		Index index = new Index();
		index.tokenize("Files/test_sentences.txt");
		KnowledgeDictionary dico = new KnowledgeDictionary(index);
		Search srch = new Search(index, dico, 0); //TODO language
		TreeSet<Integer> tree= srch.search(searchParam.getQuery());
		// Création de la reponse 
		JSONObject jsonResponse = new JSONObject();
		
		// Initialiser l'objet JSON
		for(Integer doc:tree){
			jsonResponse.accumulate("sentences", index.getTxtDoc(doc));
		}
		
 		
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
