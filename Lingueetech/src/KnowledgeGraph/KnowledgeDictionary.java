package KnowledgeGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class KnowledgeDictionary extends HashMap<Integer, LexicalInfo> {
	private static final long serialVersionUID = 4491795902364114584L;

	private Index index;
	
	public static final float updateDocClicked = (float) 0.1,
								updateKnown = (float) 0.35,
								updateSeen = (float) 0.01,
								updateInit = (float) 0.2,
								freqThreshold = (float) 0.1;
	
	public KnowledgeDictionary() {
		super();
		ArrayList<Integer> tokens = new ArrayList<>();
		
		for(int t : index.getTokens())
			if(index.getFrequency(t) > freqThreshold)
				tokens.add(t);
		
		update(updateInit, tokens);
	}
	
	public void addTextKnowledge(String text) {
		ArrayList<Integer> tokens = tokenize(text);
		
		update(updateKnown, tokens);
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
		ArrayList<Integer> tokens = index.getDoc(doc).getTokens();
		
		update(updateDocClicked, tokens);
		
		for(int t : tokens)
			this.get(t).addDoc(doc);
	}
	
	public void updateDocSeen(int doc) {
		ArrayList<Integer> tokens = index.getDoc(doc).getTokens();
		
		update(updateSeen, tokens);
		
		for(int t : tokens)
			this.get(t).addDoc(doc);
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