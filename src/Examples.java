import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/** A class to demonstrate the functionality of the JWNL package. */
public class Examples {
    private static final String USAGE = "java Examples <properties file>";

    public static void main(String[] args) throws IOException {
        // construct the URL to the Wordnet dictionary directory
             String wnhome ="C:/Program Files (x86)/WordNet/2.1";
             String path = wnhome + File.separator + "dict";
             URL url = null;
             try{ url = new URL("file", null, path); }
             catch(MalformedURLException e){ e.printStackTrace(); }
             if(url == null) return;

            // construct the dictionary object and open it
            IDictionary dict = new Dictionary(url);
            dict.open();

            // look up first sense of the word "dog"
            IIndexWord idxWord = dict.getIndexWord("be", POS.VERB);
            IWordID wordID = idxWord.getWordIDs().get(0);
            IWord word = dict.getWord(wordID);
            System.out.println("Id = " + wordID);
            System.out.println("Lemma = " + word.getLemma());
            System.out.println("Gloss = " + word.getSynset().getGloss());
        ISynset synset = word.getSynset();
        String LexFileName = synset.getLexicalFile().getName();
        System.out.println("Lexical Name : "+ LexFileName);
    }

}