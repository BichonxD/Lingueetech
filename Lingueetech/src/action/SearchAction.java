package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import main_package.Lingueetech;
import core.searchUtil.Search;
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
		
		 
		Search srch = new Search(Lingueetech.index, Lingueetech.dico);
		TreeSet<Integer> tree;
		if( searchParam.getSearchType().equals(Search.RELEVANCE) ){
			tree = srch.searchRelev(searchParam.getQuery());
		}
		else if ( searchParam.getSearchType().equals(Search.EDUCATIVE) ){
			tree =  srch.searchEdu(searchParam.getQuery());
		}
		else 
			throw new ActionHandlerException("Le type de recherche " + searchParam.getSearchType() + "est inconnue");
		 
		
		// Création de la reponse 
		JSONObject jsonResponse = new JSONObject();
		
		// Initialiser l'objet JSON
		/*
		sentences = [ {
			// id = 
			text = 
			tokens = [{ id, lemma, text, score} , { } , { } , { }]
			score = 
			favorite = bool
		}]
		*/
		
		for( Integer sentenceId : tree){
			// Parcourir les phrases
			// Parcourir les tokens de la phrase

			HashMap<Integer, String> associatedLemmeToken = new HashMap<Integer, String>();
			HashMap<Integer, Float> associatedLemmeScore = new HashMap<Integer, Float>();
			for (String tokenText : Lingueetech.index.getTxtDoc(sentenceId).split(" ") ) {
				
				String lemma = Lingueetech.index.toLemma(tokenText) ;
				if ( lemma != null ){
					int temp  = Lingueetech.index.getIndexLemmeToId().get(lemma);
					associatedLemmeToken.put(temp, tokenText);
					associatedLemmeScore.put(temp, Lingueetech.dico.get(temp).getKnowledge());
				}
			}
			
			JSONArray tokens = new JSONArray();
			for ( Integer tokenId : associatedLemmeToken.keySet()){
				
				JSONObject token = new JSONObject();
				token.put("id", tokenId );
				token.put("lemma", Lingueetech.index.getIndexIdToLemme().get(tokenId));
				token.put("text", associatedLemmeToken.get(tokenId));
				token.put("score", Lingueetech.dico.get(tokenId).getKnowledge());
				tokens.put(token);
			}

			JSONObject sentence = new JSONObject();
			sentence.put("id", sentenceId);
			sentence.put("text", Lingueetech.index.getTxtDoc(sentenceId));
			sentence.put("tokens", tokens);
			float score = 0f;
			for( Integer tokenId : Lingueetech.index.getSentenceIdToLemmaIds(sentenceId)){
				score += associatedLemmeScore.get(tokenId);
			}
			sentence.put("score", score);
			sentence.put("favorite", false);
			jsonResponse.accumulate("sentences", sentence);
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
