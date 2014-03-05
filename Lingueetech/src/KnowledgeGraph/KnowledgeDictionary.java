package KnowledgeGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class KnowledgeDictionary {
	private HashMap<Integer, LexicalInfo> dictionary; // Identifiant lemme / niv. de connaissance
	private Index index;
	
	public static final float updateDocClicked = (float) 0.1,
								updateSeen = (float) 0.01;
	
	public KnowledgeDictionary() {
		this.dictionary = new HashMap<>();
	}
	
	private void update(float value, ArrayList<Integer> tokens) {
		for(int t : tokens)
			if(dictionary.containsKey(t))
				dictionary.get(t).increase(value);
			else {
				LexicalInfo li = new LexicalInfo();
				li.setKnowledge(value);
				dictionary.put(t, li);
			}
	}
	
	public void updateDocClicked(int doc) {
		update(updateDocClicked, index.getDoc(doc).getTokens());
	}
	
	public void updateDocSeen(int doc) {
		update(updateSeen, index.getDoc(doc).getTokens());
	}
	
	public void updateToken(int token) {
		if(dictionary.containsKey(token))
			dictionary.get(token).setKnowledge(1);
		else {
			LexicalInfo li = new LexicalInfo();
			li.setKnowledge(1);
			dictionary.put(token, li);
		}
	}
}