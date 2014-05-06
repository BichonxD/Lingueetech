package core.KnowledgeGraph;

public class LexicalInfo {
	float knowledge;
	
	public LexicalInfo() {
		knowledge = 0;
	}

	public void increase(float progress) {
		knowledge += (1-knowledge)*progress;
	}
	
	public void setKnowledge(float knowledge) {
		this.knowledge = knowledge;
	}
	
	public Float getKnowledge() {
		return knowledge;
	}
}
