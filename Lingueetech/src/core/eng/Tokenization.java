/**
 * 
 */
package eng;

import java.util.*;
import java.util.Map.Entry;

import core.KnowledgeGraph.KnowledgeDictionary;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;

/**
 * @author ET4INFO
 * 
 */
public class Tokenization {
	private StanfordCoreNLP core;
	// les structures de l'index
	private HashMap<String, Integer> indexLemmeToId;
	private ArrayList<String> indexIdToLemme; // probablement non nécessaire car la perte de place est surement trop grande pour le gain de temps trop petit
	private HashMap<Integer, ArrayList<Integer>> dictionnaireIdToDocs;
	private HashMap<Integer, String> indexIdToSentences;
	private ArrayList<Integer> idLemmeToFrequence;
	private HashMap<Integer, Integer> indexIdToIdf; // id token, son idf
		
	public Tokenization(){
		// initialiser les structures de l'index
		indexLemmeToId = new HashMap<String, Integer>();
		indexIdToLemme = new ArrayList<String>(); // probablement non nécessaire car la perte de place est surement trop grande pour le gain de temps trop petit
		dictionnaireIdToDocs = new HashMap<Integer, ArrayList<Integer>>();
		indexIdToSentences = new HashMap<Integer, String>();
		idLemmeToFrequence = new ArrayList<Integer>();
		indexIdToIdf = new HashMap<Integer, Integer>();
		
		// Préciser les attributs à reconnaitre pour chauqe phrase, on construit
		// l'index par les lemmes
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		// L'objet pour analyser les textes
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		this.core=pipeline;
	}
	
	/** Vérifier que le str en parametre est un nombre n'est ni un nombre ni un
	symbole
	 * @param str
	 */
	public static boolean isLemma(String str) {
		//symbole ou vide
		if (str.replaceAll("[\\pP\\pM\\pZ\\pS\\pC]+", "").isEmpty()) {
			return false;
		}
		
		//numerique
		if (str.matches("-?\\d+(\\.\\d+)?")) {
			return false;
		}
		return true;
	}
	
	public StanfordCoreNLP getCore(){
		return this.core;
	}
	
	/** Renvoie la lemme d'un mot */
	public String toLemma(String text){
		Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    core.annotate(document);
	    try{
	    	return document.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class).get(0).getString(LemmaAnnotation.class);
	    }catch(Exception e){
	    	System.err.println("Erreur de lemmalisation pour le mot \""+text+"\" : "+e.getMessage());
	    	return "";
	    }
	    
	}
	/**
	 * Analyser un fichier et enregistrer les éléments obtenus dans les structures
	 * @param path chemin du fichier
	 */
	public void tokenize(String path){
		// Construire une annotation a partir d'un fichier
		Annotation document = new Annotation(
				IOUtils.slurpFileNoExceptions(path));
		core.annotate(document);
		//variables temporaires
		String[] temp;
		Integer itemp = -1;
		String typeLemme;
		// variables à remplir
		int idToken = 0, idSentence = 0, idf = 0;
		// On traite chaque phrase dans le fichier separament
			// un CoreMap est un Map qui utilise les objest de classe comme les
			// clefs et les types des valeurs est un choix libre
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				temp = sentence.toString().split("\t");
				indexIdToSentences.put(idSentence, temp[2]);
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
					typeLemme = token.getString(PartOfSpeechAnnotation.class);
					// On ne traite pas les num�ros et les symboles
					if (isLemma(lemma)) {
						// Ajouter la lemma dans l'index si elle n'est pas presente dedans
						if (!indexIdToLemme.contains(lemma)) {
							indexLemmeToId.put(lemma, idToken);
							indexIdToIdf.put(idToken, 0);
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
				}
				idSentence++;

			}
			for(Integer i : indexIdToIdf.keySet()){
				indexIdToIdf.put(i, (int)(Math.log(dictionnaireIdToDocs.size()/getDocs(i).size())));
			}
		
	}
	
	public ArrayList<Integer> tokenizeSentence(String sentence, int lang){
		//TODO
		return tokenizeSentence(sentence); // temp
	}
	
	public ArrayList<Integer> tokenizeSentence(String sentence){
		ArrayList<Integer> ids=new ArrayList<>();
		String[] tabWords=sentence.split(" ");
		for(int i=0; i<tabWords.length; i++){
			String lemma = toLemma(tabWords[i]);
			ids.add(indexLemmeToId.get(lemma));
		}
		return ids;
	}
	
	public ArrayList<Integer> getDocs(Integer lemma){
		return dictionnaireIdToDocs.get(lemma);
	}
	
	public Integer getIDF(Integer lemma){
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

	public static void main(String[] args) {

		Tokenization tokenization = new Tokenization();
		tokenization.tokenize("Files/test_sentences.txt");
		// ligne debug
		System.out.println(tokenization.getIndexLemmeToId().toString());
		System.out.println(tokenization.getIndexIdToSentences().toString());
		System.out.println(tokenization.getDictionnaireIdToDocs().toString());
		System.out.println(tokenization.getIdLemmeToFrequence().toString());
		//Tester la lemme (go) pour le mot went
		System.out.println("La lemme pour le mot went est : "+tokenization.toLemma("went"));
		KnowledgeDictionary dico = new KnowledgeDictionary();
		
		Search search = new Search(tokenization, dico, int language

	}

}
