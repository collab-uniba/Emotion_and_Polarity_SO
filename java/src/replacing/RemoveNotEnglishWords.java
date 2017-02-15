package replacing;


import printing.PrintingFile;
import reading.ReadingFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


/**
 * Created by Francesco on 02/01/2017.
 * Remove all terms not contained in any wordnet synset.
 */
public class RemoveNotEnglishWords {

    private final String wnhome= "C:/Program Files (x86)/WordNet/2.1";
    private final String path = wnhome + File.separator + "dict";
    private final String negEmoticon= "res/Senti4SDLists/NegativeEmoticon";
    private final String posEmoticon= "res/Senti4SDLists/PositiveEmoticon";


    public List<String> removeNotEnglishWords(List<String> docs){
        List<String> positiveEmoticon= null;
        List<String> negativeEmoticon= null;

        List<String> docsWithoutNotEnglishWords = new ArrayList<>();


        Set<String> wordsRejectedOrdering= new TreeSet<>();//elimina i doppioni e mette in ordine crescente
        List<String> wordsRejected= new ArrayList<>();

        PrintingFile pr= new PrintingFile();
        ReadingFile rd= new ReadingFile();

        URL url = null;
        try{
            url = new URL("file", null, path);
            // construct the dictionary object and open it
            //IDictionary dict = new Dictionary(url);
            //dict.open();

            positiveEmoticon= rd.read(posEmoticon);
            negativeEmoticon= rd.read(negEmoticon);



            for (String doc : docs) {
                //check if a single term into a text is find out into an list and replace it with the list's name.
                String[] textTerms = doc.split("\\s+");
                String finalString = "";
                for (String term : textTerms) {
                  /*  if (existInWordnet(dict, term) || isInPositiveNegativeList(positiveEmoticon,negativeEmoticon,term))
                        finalString = finalString + " " + term;
                    else{
                        wordsRejectedOrdering.add(term);
                    }*/
                }
                //I documenti vuoti li elimino, non li metto
                if (!finalString.isEmpty())
                    docsWithoutNotEnglishWords.add(finalString);
            }
            wordsRejected.addAll(wordsRejectedOrdering);
            pr.print("res/wordsRejected.txt",wordsRejected);
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docsWithoutNotEnglishWords;
    }

    /**
     * Check if the term exists in Wordnet
     * @param dict wordnet directory
     * @param term term taken in exam
     * @return true if the term exists else false
     */
  /*  private boolean existInWordnet(IDictionary dict,String term){
        try{
            WordnetStemmer wd = new WordnetStemmer(dict);
            for (POS pos : POS.values()) {
               List<String> stemms= wd.findStems(term,pos);
                   for (Iterator<IIndexWord> i = dict.getIndexWordIterator(pos); i.hasNext();)
                       for (IWordID wid : i.next().getWordIDs()) {
                           if (dict.getWord(wid).getLemma().equals(term)) {
                               return true;
                           }
                           List<IWord> words = dict.getWord(wid).getSynset().getWords();
                           for (IWord word : words) {
                               //Collection<Pointer> pt = Pointer.values();
                               //for (Pointer pointer : pt) {
                                //   List<IWordID> lm = word.getRelatedWords(pointer);
                                  // for (IWordID wl : lm) {
                                   //    IWord wmm = dict.getWord(wl);
                                       String wordy = word.getLemma();
                                       for(String stemm: stemms) {
                                           if (wordy.equals(stemm)) {
                                               return true;
                                           }
                                       }
                                   }
                               }
                           }
            }
        catch(IllegalArgumentException e){
           // e.printStackTrace();
        }
        return false;
    }
*/

    private boolean isInPositiveNegativeList(List<String> positiveEmoticon, List<String> negativeEmoticon,String term){
            return (positiveEmoticon.contains(term) || negativeEmoticon.contains(term));
    }
}
