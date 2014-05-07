package Word2Vec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public final class Word2Vec {

    private static volatile Word2Vec instance = null;
    private static final String URL_GENSIM = "http://localhost:8080/";
    private URL url;
    @SuppressWarnings("unused")
	private URLConnection connection;
    
    private Word2Vec() {
    	super();
    }

    /**
     * MŽthode permettant de renvoyer une instance de la classe Word2Vec
     * @return Retourne l'instance du singleton.
     */
    public final static Word2Vec getInstance() {
        if (Word2Vec.instance == null) {
           synchronized(Word2Vec.class) {
             if (Word2Vec.instance == null) {
            	 Word2Vec.instance = new Word2Vec();
             }
           }
        }
        return Word2Vec.instance;
    }

    /**
     * MŽthode permettant de renvoyer la distance entre deux mots
     * @return Retourne la distance entre deux mots
     */
    public double distance(String word1, String word2) {
    	String inputLine,dist="";
    	
    	try {
    		url = new URL(URL_GENSIM+"distance?w="+word1+"&x="+word2);

        	BufferedReader in = new BufferedReader(
        				new InputStreamReader(
        				url.openStream()));
         
        	while ((inputLine = in.readLine()) != null)
        	    dist=inputLine;
        	in.close();
        	
        	return Double.parseDouble(dist);
		
    	} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return -1;
    }
    
    public static void main(String[] args) {
    	System.out.println(Word2Vec.getInstance().distance("dog", "cat"));
    }

}