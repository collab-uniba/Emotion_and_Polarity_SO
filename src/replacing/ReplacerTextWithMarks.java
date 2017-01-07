package replacing;

import reading.ReadingCSV;
import reading.ReadingFile;

import java.io.IOException;
import java.util.*;


public class ReplacerTextWithMarks {


    /**
     * Per ogni documento, per ogni suo termine controlla sotto quale symset si trova. Sostituisce il termine con l'affective label
     * @param pathDocuments
     * @param pathsMarks
     * @return
     */
    public List<String> replaceTermsWithMarks(String pathDocuments,List<String> pathsMarks) throws IOException {
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rf= new ReadingFile();

        List<Map<String,List<String>>> allList = new ArrayList<>();
        //read all files , each of them is formed by : n list's name and n terms for each of them
        for(String path: pathsMarks){
            Map<String, List<String>> listReaded = rd.read_AllColumn_CSV(path,';');
            allList.add(listReaded);
        }
        List<String> texts = rf.read(pathDocuments);

        List<String> textMark = new ArrayList<>();
        if (texts != null) {
            for (String text : texts) {
                //check if a single term into a text is find out into an list and replace it with the list's name.
                String[] textTerms = text.split("\\s+");
                String finalString = "";
                for (String term : textTerms) {
                    String mark = null;
                    for (Map<String, List<String>> allMarkTerms : allList) {
                        mark = termToMark(term, allMarkTerms);
                        finalString = finalString + " " + mark;
                    }

                }
                 textMark.add(finalString);
            }
            //print
            texts= textMark;
          /*  for(String text : texts){
                System.out.println(text + " ");
            }*/
        }
        else
            System.err.println("key not found!");
         return textMark;
    }




    String termToMark(String term, Map<String,List<String>> allMarksTerms){
        Set<String> keys= allMarksTerms.keySet();
        for(String mark : keys){

            if(mark.equals(term)){
                return mark;
            }
            else {
                //takes all mark's termns
                List<String> markTerms = allMarksTerms.get(mark);
                if(markTerms.contains(term)){
                    return mark;
                }
            }
        }
        return " ";
    }
}
