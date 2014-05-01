package KnowledgeGraph;

import java.util.ArrayList;
import java.util.HashMap;

import eng.Tokenization;

public class KnowledgeDictionary extends HashMap<Integer, LexicalInfo> {
	private static final long serialVersionUID = 4491795902364114584L;

	private Tokenization index;
	
	public static final float updateDocClicked = (float) 0.1,
								updateKnown = (float) 0.35,
								updateSeen = (float) 0.01,
								updateInit = (float) 0.2,
								freqThreshold = (float) 0.1;
	
	public KnowledgeDictionary(Tokenization index) {
		super();
		this.index = index;
		ArrayList<Integer> id = new ArrayList<>();
		ArrayList<Integer> freq = index.getIdLemmetoFrequence();
		
		for(int t : freq)
			if(t > freqThreshold)
				id.add(freq.indexOf(t));
		
		update(updateInit, id);
	}
	
	public void addTextKnowledge(String text) {
		ArrayList<Integer> id = index.tokenizeSentence(text);
		
		update(updateKnown, id);
	}
	
	private void update(float value, ArrayList<Integer> tokens) {
		for(int t : tokens)
			if(this.containsKey(t)) {
				this.get(t).increase(value);
			}
			else {
				LexicalInfo li = new LexicalInfo();
				li.setKnowledge(value);
				this.put(t, li);
			}
	}
	
	public void updateDocClicked(int doc) {
		ArrayList<Integer> id = index.getDictionnaireIdToDocs().get(doc);
		
		update(updateDocClicked, id);
	}
	
	public void updateDocSeen(int doc) {
		ArrayList<Integer> id = index.getDictionnaireIdToDocs().get(doc);
		
		update(updateSeen, id);
	}
	
	public void updateToken(int token) {
		if(this.containsKey(token))
			this.get(token).setKnowledge(1);
		else {
			LexicalInfo li = new LexicalInfo();
			li.setKnowledge(1);
			this.put(token, li);
		}
	}
}