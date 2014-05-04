package core.KnowledgeGraph;

public class LexicalInfo {
	float knowledge;
	
	public LexicalInfo() {
		knowledge = 0;
	}

	public void increase(float value) {
		knowledge = Math.min(1, knowledge + value);
	}
	
	public void setKnowledge(float knowledge) {
		this.knowledge = knowledge;
	}
	
	public Float getKnowledge() {
		return knowledge;
	}
}
