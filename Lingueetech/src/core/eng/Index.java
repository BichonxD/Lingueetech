/**
 * 
 */
package core.eng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import exception.LingueetechException;

/**
 * @author ET4INFO
 * 
 */
public class Index {
	private StanfordCoreNLP core;
	// les structures de l'index
	private HashMap<String, Integer> indexLemmeToId;
	private ArrayList<String> indexIdToLemme; // probablement non nécessaire car
												// la perte de place est
												// surement trop grande pour le
												// gain de temps trop petit
	private HashMap<Integer, ArrayList<Integer>> dictionnaireIdToDocs;
	private HashMap<Integer, String> indexIdToSentences;
	private ArrayList<Integer> idLemmeToFrequence;
	private HashMap<Integer, Float> indexIdToIdf; // id token, son idf
	private ArrayList<SuffixArray> indexIdtoSuffixArray; // tableaux de suffixes

	public Index() {
		// initialiser les structures de l'index
		indexLemmeToId = new HashMap<String, Integer>();
		indexIdToLemme = new ArrayList<String>(); // probablement non nécessaire
													// car la perte de place est
													// surement trop grande pour
													// le gain de temps trop
													// petit
		dictionnaireIdToDocs = new HashMap<Integer, ArrayList<Integer>>();
		indexIdToSentences = new HashMap<Integer, String>();
		idLemmeToFrequence = new ArrayList<Integer>();
		indexIdToIdf = new HashMap<Integer, Float>();
		indexIdtoSuffixArray = new ArrayList<SuffixArray>();

		// Préciser les attributs à reconnaitre pour chauqe phrase, on construit
		// l'index par les lemmes
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		// L'objet pour analyser les textes
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		this.core = pipeline;
	}

	/**
	 * Vérifier que le str en parametre est un nombre n'est ni un nombre ni un
	 * symbole
	 * 
	 * @param str
	 */
	public static boolean isLemma(String str) {
		// symbole ou vide
		if (str.replaceAll("[\\pP\\pM\\pZ\\pS\\pC]+", "").isEmpty()) {
			return false;
		}

		// numerique
		if (str.matches("-?\\d+(\\.\\d+)?")) {
			return false;
		}
		return true;
	}

	public StanfordCoreNLP getCore() {
		return this.core;
	}

	/** Renvoie la lemme d'un mot */
	public String toLemma(String text) {
		Annotation document = new Annotation(text);
		// run all Annotators on this text
		core.annotate(document);
		try {
			return document.get(SentencesAnnotation.class).get(0)
					.get(TokensAnnotation.class).get(0)
					.getString(LemmaAnnotation.class);
		} catch (Exception e) {
			System.err.println("Erreur de lemmalisation pour le mot \"" + text
					+ "\" : " + e.getMessage());
			return "";
		}

	}

	/**
	 * Analyser un fichier et enregistrer les éléments obtenus dans les
	 * structures
	 * 
	 * @param path
	 *            chemin du fichier
	 */
	public void tokenize(String path) {
		// Construire une annotation a partir d'un fichier
		Annotation document = new Annotation(
				IOUtils.slurpFileNoExceptions(path));
		core.annotate(document);
		// variables temporaires
		String[] temp;
		Integer itemp = -1;
		// variables à remplir
		int idToken = 0, idSentence = 0;
		// On traite chaque phrase dans le fichier separament
		// un CoreMap est un Map qui utilise les objest de classe comme les
		// clefs et les types des valeurs est un choix libre
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			temp = sentence.toString().split("\t");
			indexIdToSentences.put(idSentence, temp[2]);
			indexIdtoSuffixArray.add(new SuffixArray(temp[2]));
			// Liste des tokens dans la phrase actuelle
			List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
			Iterator<CoreLabel> iterator = tokens.iterator();
			CoreLabel token;
			// On saute les deux permiers tokens de la phrase (Id et langue)
			token = iterator.next();
			token = iterator.next();
			// Pour chaque mot de la phrase
			while (iterator.hasNext()) {
				token = iterator.next();
				// On transforme tous en minuscules pour les lemmes
				String lemma = token.getString(LemmaAnnotation.class).toLowerCase();
				// On ne traite pas les num�ros et les symboles
				if (isLemma(lemma)) {
					// Ajouter la lemma dans l'index si elle n'est pas presente dedans
					if (!indexIdToLemme.contains(lemma)) {
						indexLemmeToId.put(lemma, idToken);
						indexIdToIdf.put(idToken, (float) 0);
						indexIdToLemme.add(lemma);
						idLemmeToFrequence.add(1);
						// Ajouter lemma dans le dictionnaire s'il n'est pas
						// présent : créer l'arraylist de sentence correspondant
						ArrayList<Integer> t = new ArrayList<Integer>();
						t.add(idSentence);
						dictionnaireIdToDocs.put(idToken, t);
						idToken++;
					} else {
						itemp = -1;
						// màj de l'arrayList du lemme existant avec la sentence actuelle
						for (Entry<String, Integer> entry : indexLemmeToId.entrySet()) {
							//Si le string correspond au lemme, on sauvegarde l'id dans itemp
							if (entry.getKey().equals(lemma))
								itemp = entry.getValue();
						}
						idLemmeToFrequence.set(itemp, idLemmeToFrequence.get(itemp) + 1);
						//précaution que nous prenons : on s'assure que le lemme est bien dans notre hash, si ce n'est pas le cas problème !
						if (itemp == -1)
							System.out.println("c'est la merde les gars !");
						//on peut ajouter notre id de phrase à la liste de phrases du lemme
						dictionnaireIdToDocs.get(itemp).add(idSentence);
					}
				}
				idSentence++;

			}
			
			for(Integer i : indexIdToIdf.keySet())
				indexIdToIdf.put(i, (float) Math.log(dictionnaireIdToDocs.size()/getDocs(i).size()));
		}
	}

	public ArrayList<Integer> tokenizeSentence(String sentence, int lang) {
		return tokenizeSentence(sentence); // TODO
	}

	public ArrayList<Integer> tokenizeSentence(String sentence) {
		ArrayList<Integer> ids = new ArrayList<>();
		String[] tabWords = sentence.split(" ");
		for (int i = 0; i < tabWords.length; i++) {
			String lemma = toLemma(tabWords[i]);
			ids.add(indexLemmeToId.get(lemma));
		}
		return ids;
	}

	/**
	 * Recherche binaire avec lcp
	 * 
	 * @param expr
	 *            l'expression à cherche dans le corpus
	 * @return liste des idSentence des pharses qui contient l'expression,null
	 *         si expr n'est pas présent dans le corpus
	 * @throws LingueetechException
	 */
	public ArrayList<Integer> sentenceIdForPhrase(String expr) throws LingueetechException {
		/*Vérifier si la recherche a une sense*/
		String s = expr.replaceAll("\\s+", " ").trim();
		String[] words = s.split("\\s+");
		String lemma;
		boolean notLemma = true;
		for (int i = 0; i < words.length; i++) {
			lemma = this.toLemma(words[i]);
			if (lemma != "")
				notLemma = false;
		}
		if (notLemma == true)
			return null;
		/* Vérifier si expr est présent */
		ArrayList<Integer> sentenceIdArray = new ArrayList<Integer>();
		int size = expr.length();
		for (int j = 0; j < indexIdtoSuffixArray.size(); j++) {
			SuffixArray sa = this.indexIdtoSuffixArray.get(j);
			int saSize = sa.length();
			if (size <= saSize) {
				Suffix[] suffixes = sa.getSuffixes();
				int left = 0;
				int right = saSize - 1;
				int middle = saSize / 2;
				int compare = 0;
				while (left <= right) {
					compare = SuffixArray.compare(expr, suffixes[middle]);
					if (compare == 1) {
						left = middle + 1;
					} else if (compare == -1) {
						right = middle - 1;
					} else {
						if(expr.length()>suffixes[middle].length()){
							left = middle + 1;
						}else {
							sentenceIdArray.add(j);
							break;
						}
					}
					middle = (right + left) / 2;
				}
			}
		}
		if (sentenceIdArray.isEmpty()) {
			return null;
		} else {
			// return sentenceIdArray;
			return sentenceIdArray;
		}

	}

	public String getTxtDoc(Integer id) {
		return indexIdToSentences.get(id);
	}

	public ArrayList<Integer> getDocs(Integer lemma) {
		return dictionnaireIdToDocs.get(lemma);
	}
	
	public Float getIDF(Integer lemma){
		return indexIdToIdf.get(lemma);
	}

	/**
	 * @return the indexLemmeToId
	 */
	public HashMap<String, Integer> getIndexLemmeToId() {
		return indexLemmeToId;
	}

	/**
	 * @return the indexIdToLemme
	 */
	public ArrayList<String> getIndexIdToLemme() {
		return indexIdToLemme;
	}

	/**
	 * @return the dictionnaireIdToDocs
	 */
	public HashMap<Integer, ArrayList<Integer>> getDictionnaireIdToDocs() {
		return dictionnaireIdToDocs;
	}

	/**
	 * @return the indexIdToSentences
	 */
	public HashMap<Integer, String> getIndexIdToSentences() {
		return indexIdToSentences;
	}

	/**
	 * @return the idLemmeToFrequence
	 */
	public ArrayList<Integer> getIdLemmeToFrequence() {
		return idLemmeToFrequence;
	}

	/**
	 * @return the indexIdtoSuffixArray
	 */
	public ArrayList<SuffixArray> getIndexIdtoSuffixArray() {
		return indexIdtoSuffixArray;
	}
	/*
	public static void main(String[] args) {

		Index index = new Index();
		index.tokenize("Files/test_sentences.txt");
		// // ligne debug
		// System.out.println(tokenization.getIndexLemmeToId().toString());
		// System.out.println(tokenization.getIndexIdToSentences().toString());
		// System.out.println(tokenization.getDictionnaireIdToDocs().toString());
		// System.out.println(tokenization.getIdLemmeToFrequence().toString());
		// //Tester la lemme (go) pour le mot went
		// System.out.println("La lemme pour le mot went est : "+tokenization.toLemma("went"));
		KnowledgeDictionary dico = new KnowledgeDictionary(index);

		Search srch = new Search(index, dico, 0); // TODO language
		TreeSet<Integer> tree = srch.search("is");
		for (Integer doc : tree) {
			System.out.println(index.getTxtDoc(doc));
		}
		try {
			ArrayList<Integer> a = index.sentenceIdForPhrase("password is");
			if(a!=null){
			System.out.println("result" + a.toString());
			 }
		} catch (LingueetechException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	*/
}
