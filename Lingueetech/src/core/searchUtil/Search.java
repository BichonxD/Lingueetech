package core.searchUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import core.KnowledgeGraph.KnowledgeDictionary;
import core.eng.Index;

public class Search{

	public static final String RELEVANCE = "Relevance"; 
	public static final String EDUCATIVE = "Educative"; 
	
	/* ATTRIBUTES */
	private Index index;
	private KnowledgeDictionary dico;
	private HashMap<Integer, Float> scoreDoc = null;

	private final Comparator<Integer> relevComparator = new Comparator<Integer>() {
		public int compare(Integer d1, Integer d2) {
			int res = scoreDoc.get(d2).compareTo(scoreDoc.get(d1));
			if (res != 0)
				return res;
			else {
				res=dico.get(d2).getKnowledge().compareTo(dico.get(d1).getKnowledge()) ;
				return (res==0)?1:res;
			}
		}
	};
	
	private final Comparator<Integer> eduComparator = new Comparator<Integer>() {
		public int compare(Integer d1, Integer d2) {
			int res=getKnowledge(d2).compareTo(getKnowledge(d1));
			return (res == 0) ? 1 : res; /*Egalite interdite pour le treeset*/
		}
	};


	/* CONSTRUCTOR */
	public Search(Index index, KnowledgeDictionary dico) {
		this.index=index;
		this.dico = dico;
	}

	/* METHODS */
	public TreeSet<Integer> searchRelev(String keywords) {
		ArrayList<Integer> tokens = index.tokenizeSentence(keywords);
		ArrayList<Integer> listDocs = new ArrayList<>();
		scoreDoc = new HashMap<>();

		for (Integer t : tokens) {
			ArrayList<Integer> listDocsToken = index.getDocs(t);
			if(listDocsToken != null) {
				listDocs.addAll(listDocsToken);
				for (Integer d : listDocsToken) {
					float previousScore = (scoreDoc.containsKey(d)) ? scoreDoc.get(d) : 0; // Indexes should not create more than one instance of a document.
					scoreDoc.put(d, previousScore + index.getIDF(t));
				}
			}
		}

		TreeSet<Integer> tree = new TreeSet<>(relevComparator);
		tree.addAll(listDocs);
		
		System.out.println(tree.size());
		return tree;
	}
	
	public TreeSet<Integer> searchEdu(String keywords) {
		ArrayList<Integer> tokens = index.tokenizeSentence(keywords);
		TreeSet<Integer> sentences = new TreeSet<>(eduComparator);

		for(Integer t : tokens)
			sentences.addAll(index.getDocs(t));
		
		return sentences;
	}

	private Float getKnowledge(Integer doc){
		float s=0;
		ArrayList<Integer> lemmas=index.getSentenceIdToLemmaIds(doc);
		for(Integer lm:lemmas){
			s += dico.get(lm).getKnowledge();
		}
		return s/lemmas.size();
	}
}
