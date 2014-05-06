package core.KnowledgeGraph;

public class LexicalInfo {
	float knowledge;
	float progress=0.5f;
	
	public LexicalInfo() {
		knowledge = 0;
	}

	public void increase(float value) {
		knowledge += (1-knowledge)*progress;
	}
	
	public void setKnowledge(float knowledge) {
		this.knowledge = knowledge;
	}
	
	public Float getKnowledge() {
		return knowledge;
	}
}
