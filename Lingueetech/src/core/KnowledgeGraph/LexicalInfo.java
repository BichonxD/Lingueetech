package KnowledgeGraph;

import java.util.ArrayList;

public class LexicalInfo {
	float knowledge;
	ArrayList<Integer> docs;
	
	public LexicalInfo() {
		docs = new ArrayList<>();
		knowledge = 0;
	}

	public void increase(float value) {
		knowledge = Math.min(1, knowledge + value);
	}
	
	public void setKnowledge(float knowledge) {
		this.knowledge = knowledge;
	}
	
	public boolean addDoc(int doc) {
		return docs.add(doc);
	}
}
