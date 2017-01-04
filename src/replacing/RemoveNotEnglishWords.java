package replacing;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import reading.ReadingCSV;
import reading.ReadingFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by Francesco on 02/01/2017.
 * Remove all terms not contained in any wordnet synset.
 */
public class RemoveNotEnglishWords {



    public List<String> removeNotEnglishWords(List<String> docs){
        List<String> docsWithoutNonEnglishWords = new ArrayList<>();
        String wnhome ="C:/Program Files (x86)/WordNet/2.1";
        String path = wnhome + File.separator + "dict";
        URL url = null;
        try{
            url = new URL("file", null, path);
            // construct the dictionary object and open it
            IDictionary dict = new Dictionary(url);
            dict.open();

            for (String doc : docs) {
                //check if a single term into a text is find out into an list and replace it with the list's name.
                String[] textTerms = doc.split("\\s+");
                String finalString = "";
                for (String term : textTerms) {
                    // look up first sense of the word "dog"
                    if (findIndex(dict, term))
                        finalString = finalString + " " + term;
                }
                //I documenti vuoti li elimino, non li metto
                if (!finalString.isEmpty())
                    docsWithoutNonEnglishWords.add(finalString);
            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docsWithoutNonEnglishWords;
    }

    private boolean findIndex(IDictionary dict,String term){
        try {
            IIndexWord idxWord = dict.getIndexWord(term, POS.VERB);
            if(idxWord!=null){
                return true;
            }
            idxWord = dict.getIndexWord(term, POS.NOUN);
            if(idxWord!=null){
                return true;
            }
            idxWord = dict.getIndexWord(term, POS.ADJECTIVE);
            if(idxWord!=null) {
                return true;
            }
            idxWord = dict.getIndexWord(term, POS.ADVERB);
            return idxWord != null;
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
       return false;
    }




    public List<String> removeNonEnglishWord(List<String> docs,List<String> pathsMarks) {
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rf = new ReadingFile();
        ReplacerTextWithMarks rp = new ReplacerTextWithMarks();

        List<Map<String, List<String>>> allList = new ArrayList<>();
        //read all files , each of them is formed by : n list's name and n terms for each of them
        for (String path : pathsMarks) {
            Map<String, List<String>> listReaded = rd.read_AllColumn_CSV(path,';');
            allList.add(listReaded);
        }

        List<String> docsWithoutNonEnglishWords = new ArrayList<>();
        for (String doc : docs) {
            //check if a single term into a text is find out into an list and replace it with the list's name.
            String[] textTerms = doc.split("\\s+");
            String finalString = "";
            for (String term : textTerms) {
                String mark = null;
                for (Map<String, List<String>> allMarkTerms : allList) {
                    mark = rp.termToMark(term, allMarkTerms);
                    if (!mark.equals(" "))
                        finalString = finalString + " " + term;
                }
            }
            if (!finalString.isEmpty())
              docsWithoutNonEnglishWords.add(finalString);
        }
        return docsWithoutNonEnglishWords;
    }

}
