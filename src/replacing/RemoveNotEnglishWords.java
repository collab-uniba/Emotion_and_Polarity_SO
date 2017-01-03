package replacing;

import reading.ReadingCSV;
import reading.ReadingFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Francesco on 02/01/2017.
 * Remove all terms not contained in any wordnet synset.
 */
public class RemoveNotEnglishWords {

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
