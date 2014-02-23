package searchUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Search {
	
	/* ATTRIBUTES */
	private Index index;
	private IndexSuffix indexSuffix;
	private HashMap<Document, Integer> scoreDoc=null;
	
	private static final Comparator<Document> relevComparator = new Comparator<Token>() {
	    public int compare(Document d1, Document d2){
	    	int res=scoreDoc.get(d2).compareTo(scoreDoc.get(d1));
	    	if(res!=0)
	    		return res;
	    	else{
	    		return d2.knowledge().compareTo(d1.knowledge()); // knowledge should return the average of the knowledge of the tokens of the document.
	    		// what happens if equality ?
	    	}
	    }
	};
	
	/* CONSTRUCTOR */
	public Search(Index index, IndexSuffix indexSuffix){
		this.index=index;
		this.indexSuffix=indexSuffix;
	}
	
	/* METHODS */
	public TreeSet<Document> search(String keywords){
		HashSet<Token> tokens = tokenize(keywords);
		ArrayList<Document> listDocs = new ArrayList<>();
		scoreDoc = new HashMap<>();
		
		for(Token t : tokens){
			ArrayList<Document> listDocsToken = (t.isExpression()) ? indexSuffix.getDocs(t) : index.getDocs(t);
			listDocs.addAll(listDocsToken);
			
			for(Document d : listDocsToken){
				int previousScore = (scoreDoc.get(d) == null) ? 0 : scoreDoc.get(d); // Indexes should not create more than one instance of a document.
				scoreDoc.put(d, previousScore + t.getIDF());
			}
		}
		
		TreeSet<Document> tree = new TreeSet<>(relevComparator);
		tree.addAll(listDocs);
		return tree;
	}
}
