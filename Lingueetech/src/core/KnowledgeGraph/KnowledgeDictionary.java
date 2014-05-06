package core.KnowledgeGraph;

import java.util.ArrayList;
import java.util.HashMap;

import core.eng.Index;

public class KnowledgeDictionary extends HashMap<Integer, LexicalInfo> {
	private static final long serialVersionUID = 4491795902364114584L;

	private Index index;
	
	public static final float updateDocClicked = (float) 0.5,
								updateKnown = (float) 1,
								updateSeen = (float) 0.25,
								freqThreshold = (float) 0.1,
								initValue = (float) 0;
	
	public KnowledgeDictionary(Index index){
		super();
		this.index = index;
		ArrayList<Integer> id = new ArrayList<>();
		ArrayList<Integer> freq = index.getIdLemmeToFrequence();
		
		for(int i=0; i<freq.size();i++){
			if(freq.get(i) > freqThreshold)
				id.add(i);
		}
		init(id);
	}
	
	public void init(ArrayList<Integer> ids){
		for(int t : ids){
			LexicalInfo li = new LexicalInfo();
			li.setKnowledge(initValue);
			this.put(t, li);
		}
	}
	
	public void addTextKnowledge(String text) {
		ArrayList<Integer> id = index.tokenizeSentence(text);
		
		update(updateKnown, id);
	}
	
	private void update(float value, ArrayList<Integer> tokens) {
		for(int t : tokens) this.get(t).increase(value);
	}
	
	public void updateDocClicked(int doc) {
		ArrayList<Integer> id = index.getDictionnaireIdToDocs().get(doc);
		
		update(updateDocClicked, id);
	}
	
	public void updateDocSeen(int doc) {
		ArrayList<Integer> id = index.getDictionnaireIdToDocs().get(doc);
		
		update(updateSeen, id);
	}
	
	public void known(int token) {
		if(this.containsKey(token))
			this.get(token).setKnowledge(1);
		else {
			LexicalInfo li = new LexicalInfo();
			li.setKnowledge(1);
			this.put(token, li);
		}
	}
}