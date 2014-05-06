///*package eng;
///**
// * 
// * HashMap<String, Integer> indexLemmeToId = new HashMap<String, Integer>();
// * ArrayList<String> indexIdToLemme = new ArrayList<String>(); probablement non nécessaire car la perte de place est surement trop grande pour le gain de temps trop petit
// * 
//		HashMap<Integer, ArrayList<Integer>> dictionnaireIdToDocs = new HashMap<Integer, ArrayList<Integer>>();
//		
//		HashMap<Integer, String> indexIdToSentences = new HashMap<Integer, String> (); 
//		
//		ArrayList<Integer> IdLemmeToFrequence = new ArrayList<Integer>();
// */
//
//import java.util.*;
//import java.util.Map.Entry;
//
//import edu.stanford.nlp.io.*;
//import edu.stanford.nlp.ling.*;
//import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
//import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
//import edu.stanford.nlp.pipeline.*;
//import edu.stanford.nlp.util.*;
//
///**
// * @author ET4INFO
// * 
// */
//public class Main {
//
//	/**
//	 * V�rifier que le str en parametre est un nombre n'est ni un nombre ni un
//	 * symbole
//	 * 
//	 * @param args
//	 */
//	public static boolean isLemma(String str) {
//		// symbole ou vide
//		if (str.replaceAll("[\\pP\\pM\\pZ\\pS\\pC]+", "").isEmpty()) {
//			return false;
//		}
//
//		// numerique
//		if (str.matches("-?\\d+(\\.\\d+)?")) {
//			return false;
//		}
//		return true;
//	}
//
//	public static void main(String[] args) {
//		// les structures de l'index
//		HashMap<String, Integer> indexLemmeToId = new HashMap<String, Integer>();
//		ArrayList<String> indexIdToLemme = new ArrayList<String>(); // probablement non nécessaire car la perte de place est surement trop grande pour le gain de temps trop petit
//		HashMap<Integer, ArrayList<Integer>> dictionnaireIdToDocs = new HashMap<Integer, ArrayList<Integer>>();
//		HashMap<Integer, String> indexIdToSentences = new HashMap<Integer, String>();
//		ArrayList<Integer> idLemmeToFrequence = new ArrayList<Integer>();
//		
//		//variables temporaires
//		String[] temp;
//		Integer itemp = -1;
//		String typeLemme;
//		
//		// variables à remplir
//		int idToken = 0, idSentence = 0, idf = 0;
//		// Pr�ciser les attributs � reconnaitre pour chauqe phrase, on construit
//		// l'index par les lemmes
//		Properties props = new Properties();
//		props.put("annotators", "tokenize, ssplit, pos, lemma");
//		// L'objet pour analyser les textes
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		// Construire une annotation a partir d'un fichier
//		Annotation document = new Annotation(
//				IOUtils.slurpFileNoExceptions("Files/test_sentences.txt"));
//		pipeline.annotate(document);
//		// On traite chaque phrase dans le fichier separament
//		// un CoreMap est un Map qui utilise les objest de classe comme les
//		// clefs et les types des valeurs est un choix libre
//		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//		for (CoreMap sentence : sentences) {
//			temp = sentence.toString().split("\t");
//			indexIdToSentences.put(idSentence, temp[2]);
//			// Liste des tokens dans la phrase actuelle
//			List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
//			Iterator<CoreLabel> iterator = tokens.iterator();
//			CoreLabel token;
//			// On saute les deux permiers tokens de la phrase (Id et langue)
//			token = iterator.next();
//			token = iterator.next();
//			// Pour chaque mot de la phrase
//			while (iterator.hasNext()) {
//				token = iterator.next();
//				// On transforme tous en minuscules pour les lemmes
//				String lemma = token.getString(LemmaAnnotation.class).toLowerCase();
//				typeLemme = token.getString(PartOfSpeechAnnotation.class);
//				// On ne traite pas les num�ros et les symboles
//				if (isLemma(lemma)) {
//					// Ajouter la lemma dans l'index si elle n'est pas presente dedans
//					if (!indexIdToLemme.contains(lemma)) {
//						indexLemmeToId.put(lemma, idToken);
//						indexIdToLemme.add(lemma);
//						idLemmeToFrequence.add(1);
//						// Ajouter lemma dans le dictionnaire s'il n'est pas
//						// présent : créer l'arraylist de sentence correspondant
//						ArrayList<Integer> t = new ArrayList<Integer>();
//						t.add(idSentence);
//						dictionnaireIdToDocs.put(idToken, t);
//						idToken++;
//					} else {
//						itemp = -1;
//						// màj de l'arrayList du lemme existant avec la sentence actuelle
//						for (Entry<String, Integer> entry : indexLemmeToId.entrySet()) {
//							//Si le string correspond au lemme, on sauvegarde l'id dans itemp
//							if (entry.getKey().equals(lemma))
//								itemp = entry.getValue();
//						}
//						idLemmeToFrequence.set(itemp, idLemmeToFrequence.get(itemp) + 1);
//						//précaution que nous prenons : on s'assure que le lemme est bien dans notre hash, si ce n'est pas le cas problème !
//						if (itemp == -1)
//							System.out.println("c'est la merde les gars !");
//						//on peut ajouter notre id de phrase à la liste de phrases du lemme
//						dictionnaireIdToDocs.get(itemp).add(idSentence);
//					}
//				}
//			}
//			idSentence++;
//
//		}
//		// ligne debug
//		System.out.println(indexLemmeToId.toString());
//		System.out.println(indexIdToSentences.toString());
//		System.out.println(dictionnaireIdToDocs.toString());
//		System.out.println(idLemmeToFrequence.toString());
//
//	}
//
//}
//*/
//
///*
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Scanner;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import java.util.Set;
//
//class AfficheurFlux implements Runnable {
//
//    private final InputStream inputStream;
//
//    AfficheurFlux(InputStream inputStream) {
//        this.inputStream = inputStream;
//    }
//
//    private BufferedReader getBufferedReader(InputStream is) {
//        return new BufferedReader(new InputStreamReader(is));
//    }
//
//    @Override
//    public void run() {
//        BufferedReader br = getBufferedReader(inputStream);
//        String ligne = "";
//        try {
//            while ((ligne = br.readLine()) != null) {
//                System.out.println(ligne);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//public class Main {
//	public static void main(String[] args) {
//		String chemin = "Files/sentences.csv";
//		String phraseDecoupee[];
//		HashMap<Integer, String> index1 = new HashMap<Integer, String>();
//		String[] command = {"java","-Xmx1024M","-jar", "/home/florian/Bureau/ExtractionDonnees/Projet/talismane-fr-1.8.2b-allDeps.jar","command=analyse", "inFile=/home/florian/Bureau/ExtractionDonnees/RecupDonnees/Files/onlyFra.txt", "outFile=/home/florian/Bureau/ExtractionDonnees/RecupDonnees/Files/resultatApi.txt"};	  
//		Scanner scanner;
//		ArrayList<String> nosPhrasesEn = new ArrayList<String> ();
//		ArrayList<String> nosPhrasesFr = new ArrayList<String> ();
//		
//		try {
//			FileReader fin = new FileReader(chemin);
//			scanner = new Scanner(fin);
//			String phrase = null;
//			PrintWriter fluxSortieFr = null;
//			PrintWriter fluxSortieEn = null;
//			fluxSortieFr = new PrintWriter(new FileOutputStream("Files/onlyFra.txt",true));
//			fluxSortieEn = new PrintWriter(new FileOutputStream("Files/onlyEng.txt",true));
//			while (scanner.hasNextLine()) {
//			phrase = scanner.nextLine();
//			//Découpage de la phrase en mot pour connaître la langue etc et avoir une manipulation facile
//			phraseDecoupee = phrase.split("\t");
//			//Enregistrement de la phrase complète dans l'index 2 - l'associer à un entier unique
//			
//			/Respecter les règles sur les phrases à garder et celles à jeter (que les ENG et FRA mais FRA qui ont une traduction en ENG
//			if(phraseDecoupee[1].compareTo("fra") == 0){
//			  	fluxSortieFr.println(phrase);
//			  	nosPhrasesFr.add(phraseDecoupee[2]);
//			}
//			if(phraseDecoupee[1].compareTo("eng") == 0){
//			  	fluxSortieEn.println(phrase);
//			  	nosPhrasesEn.add(phraseDecoupee[2]);
//			}
//			
//			   
//			//Passage de la phrase dans l'API : Obtenir le lemme
//			
//			
//			    //Si c'est un déterminant on passe à la suite
//			    
//			    
//			    //Sinon on sauvegarde
//			    
//			    //Si le token (le lemme plutôt) n'est pas déjà dans la liste : on l'ajoute
//			    
//			    //Sinon on augmente le compteur du lemme déjà présent
//			    
//			    
//			}
//			fluxSortieFr.close();
//			fluxSortieEn.close();
//			
//			System.out.println("Récupération des phrases eng & fra terminée");
//		} catch (FileNotFoundException e) {
//			System.out.println("fichier non trouvé : " + chemin);
//			e.printStackTrace();
//		} catch (Exception e1){
//			System.out.println("Problème non identifié : " + e1);
//		}
//		    System.out.println("Début du programme");
//	        try {
//	        	Process p = Runtime.getRuntime().exec(command);
//	            AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream());
//	            AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream());
//
//	            new Thread(fluxSortie).start();
//	            new Thread(fluxErreur).start();
//
//	            p.waitFor();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	        System.out.println("Fin du programme");
//	    
//		System.out.println("Parsing par notre API terminé.");
//	}
//}*/