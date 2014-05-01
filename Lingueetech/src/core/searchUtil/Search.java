package searchUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Search {

	/* ATTRIBUTES */
	private Index index;
	private KnowledgeDictionary dico;
	private HashMap<Integer, Integer> scoreDoc = null;
	private int language;

	private final Comparator<Integer> relevComparator = new Comparator<Integer>() {
		public int compare(Integer d1, Integer d2) {
			int res = scoreDoc.get(d2).compareTo(scoreDoc.get(d1));
			if (res != 0)
				return res;
			else {
				return dico.knowledge(d2).compareTo(dico.knowledge(d1)); // knowledge should return the average of the knowledge of the tokens of the document.
				// what happens if equality ?
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
		ArrayList<Integer> tokens = index.tokenize(keywords, language);
		ArrayList<Integer> listDocs = new ArrayList<>();
		scoreDoc = new HashMap<>();

		for (Integer t : tokens) {
			ArrayList<Integer> listDocsToken = index.getDocs(t); // TODO: lit
			listDocs.addAll(listDocsToken);

			for (Integer d : listDocsToken) {
				int previousScore = (scoreDoc.containsKey(d)) ? scoreDoc.get(d) : 0; // Indexes should not create more than one instance of a document.
				scoreDoc.put(d, previousScore + index.getIDF(t));
			}
		}

		TreeSet<Integer> tree = new TreeSet<>(relevComparator);
		tree.addAll(listDocs);
		return tree;
	}
}
