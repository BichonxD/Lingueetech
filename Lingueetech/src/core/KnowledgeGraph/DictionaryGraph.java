package core.KnowledgeGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import core.eng.Index;
import core.searchUtil.Word2Vec;

// Tel quel, les aretes sont en double...
public class DictionaryGraph extends HashMap<Integer, HashMap<Integer, Float>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Word2Vec w2v;
	private KnowledgeDictionary dictionary;
	private Index index;
	private HashMap<Integer, Float> scores;
	private float thresholdSimilarity=0.6f, thresholdZone = 0.8f;
	
	public DictionaryGraph(Index index, KnowledgeDictionary dictionary, Word2Vec w2v, int nb, float pond){
		this.index=index;
		this.dictionary=dictionary;
		this.w2v = w2v;
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
		ArrayList<String> idToLemma = index.getIndexIdToLemme();
		
		return (float) w2v.distance(idToLemma.get(lm1), idToLemma.get(lm2));
	}
	
	/* On renvoie les nb mots les plus proches des lemmes stockes dans la liste 'lemmas' */
	public HashSet<Integer> getSimilar(HashSet<Integer> lemmas, int nb) {
		HashSet<Integer> results = new HashSet<>();
		ArrayList<String> slemmas = new ArrayList<>(), sim;
		
		for(int i : lemmas)
			slemmas.add(index.getIndexIdToLemme().get(i));
		
		//Obtenir des mots similaires a la fois a tous ceux dans 'lemmas'
		sim = w2v.close(slemmas);
		
		for(String s : sim) {
			if(results.size() < nb)
				results.add(index.getLemmeToId(index.toLemma(s)));
			else
				break;
		}
		
		return results;
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
	
	/* Obtenir une HashSet contenant nb mots proches les uns des autres dans le graphe */
	public HashSet<Integer> getRdmZone(int nb) {
		int ind = (int) Math.random() * (this.size() + 1);
		HashMap<Integer, Float> start = this.get(ind), pstart = null;
		ArrayList<Integer> lemmas = new ArrayList<>();
		
		lemmas.add(ind);
		while(lemmas.size() < nb) {
			if(pstart == start)
				break;
			
			for(int i : start.keySet()) {
				if(start.get(i) >= thresholdZone && !lemmas.contains(i)) {
					lemmas.add(i);
				}
			}
			pstart = start;
			start = this.get(lemmas.get(lemmas.size()-1));
		}
		
		return new HashSet<Integer>(lemmas);
	}
}
