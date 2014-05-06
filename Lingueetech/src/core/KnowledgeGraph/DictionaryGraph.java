package core.KnowledgeGraph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import core.eng.Index;

// Tel quel, les aretes sont en double...
public class DictionaryGraph extends HashMap<Integer, HashMap<Integer, Float>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KnowledgeDictionary dictionary;
	private Index index;
	private HashMap<Integer, Float> scores;
	private float thresholdSimilarity=0.6f;
	
	public DictionaryGraph(Index index, KnowledgeDictionary dictionary, int nb, float pond){
		this.index=index;
		this.dictionary=dictionary;
		scores=new HashMap<>();
		fillGraph(nb, pond);
	}
	
	private void fillGraph(int nb, float pond){
		HashSet<Integer> relevantLemma=getMoreRelevant(nb, pond);
		for(Integer lemma:relevantLemma){
			put(lemma,new HashMap<Integer,Float>());
			if(!isEmpty()){
				for(Integer lemmaAlready:this.keySet()){
					float sim=getSimilarity(lemma,lemmaAlready);
					if(sim>thresholdSimilarity){
						get(lemmaAlready).put(lemma, sim);
						get(lemma).put(lemmaAlready, sim);
					}
				}
			}
		}
	}

	public float getSimilarity(Integer lm1, Integer lm2){
		//TODO word2vec
	}

	private final Comparator<Integer> relevComparator = new Comparator<Integer>() {
		public int compare(Integer l1, Integer l2) {
			int res = scores.get(l2).compareTo(scores.get(l1));
			return (res != 0) ? res : 1;
		}
	};

	private HashSet<Integer> getMoreRelevant(int nb, float ponderation) {
		TreeSet<Integer> tree=new TreeSet<>(relevComparator);
		HashSet<Integer> relevants=new HashSet<>();
		for(Integer lemma: dictionary.keySet()){
			scores.put(lemma, ponderation*dictionary.get(lemma).getKnowledge()+(1-ponderation)*index.getIDF(lemma));
			tree.add(lemma);
		}
		relevants.addAll(tree.subSet(0, nb));
		return relevants;
	}
	
}
