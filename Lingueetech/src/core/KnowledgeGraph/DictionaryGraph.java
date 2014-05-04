package core.KnowledgeGraph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import core.eng.Index;

// Tel quel, les arêtes sont en double...
public class DictionaryGraph extends HashMap<Integer, HashMap<Integer, Float>> {
	private HashMap<Integer, LexicalInfo> nodes;
	private KnowledgeDictionary dictionary;
	private Index index;
	private HashMap<Integer, Float> scores;
	
	public DictionaryGraph(Index index, KnowledgeDictionary dictionary, int nb){
		this.index=index;
		this.dictionary=dictionary;	
		nodes=new HashMap<>();
		scores=new HashMap<>();
		fillGraph(nb);
	}
	
	private void fillGraph(int nb){
		HashSet<Integer> relevantLemma=getMoreRelevant(nb);
		for(Integer lemma:relevantLemma)
			put(lemma,getSimilar(lemma));
	}
	
	private HashMap<Integer, Float> getSimilar(Integer lemma) {
		// TODO with word2vec
		return null;
	}

	private final Comparator<Integer> relevComparator = new Comparator<Integer>() {
		public int compare(Integer l1, Integer l2) {
			int res = scores.get(l2).compareTo(scores.get(l1));
			return (res != 0) ? res : 1;
		}
	};

	private HashSet<Integer> getMoreRelevant(int nb) {
		TreeSet<Integer> tree=new TreeSet<>();
		HashSet<Integer> relevants=new HashSet<>();
		for(Integer lemma: dictionary.keySet()){
			scores.put(lemma, dictionary.get(lemma).getKnowledge()/index.getIDF(lemma));
			tree.add(lemma);
		}
		relevants.addAll(tree.subSet(0, nb));
		return relevants;
	}
	
	
	
}
