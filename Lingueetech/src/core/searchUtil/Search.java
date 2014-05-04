package core.searchUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import core.KnowledgeGraph.KnowledgeDictionary;
import core.eng.Index;

public class Search {

	/* ATTRIBUTES */
	private Index index;
	private KnowledgeDictionary dico;
	private HashMap<Integer, Float> scoreDoc = null;
	private int language;

	private final Comparator<Integer> relevComparator = new Comparator<Integer>() {
		public int compare(Integer d1, Integer d2) {
			int res = scoreDoc.get(d2).compareTo(scoreDoc.get(d1));
			if (res != 0)
				return res;
			else {
				res=dico.get(d2).getKnowledge().compareTo(dico.get(d1).getKnowledge()) ; // knowledge should return the average of the knowledge of the tokens of the document.
				return (res==0)?1:res;
			}
		}
	};

	/* CONSTRUCTOR */
	public Search(Index index, KnowledgeDictionary dico, int language) {
		this.index = index;
		this.dico = dico;
		this.language = language;
	}

	/* METHODS */
	public TreeSet<Integer> search(String keywords) {
		ArrayList<Integer> tokens = index.tokenizeSentence(keywords, language);
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
}
