package replacing;

import model.Document;
import reading.ReadingCSV;
import reading.ReadingFile;

import java.io.IOException;
import java.util.*;


public class ReplacerTextWithMarks {


    /**
     * Per ogni documento, per ogni suo termine controlla sotto quale symset si trova. Sostituisce il termine con l'affective label
     * @param pathsMarks
     * @return
     */
    public Map<Integer,Document> replaceTermsWithMarks(Map<Integer, Document> docs, List<String> pathsMarks) throws IOException {
        ReadingCSV rd = new ReadingCSV();
       // ReadingFile rf= new ReadingFile();

        List<Map<String,List<String>>> allList = new ArrayList<>();
        //read all files , each of them is formed by : n list's name and n terms for each of them
        for(String path: pathsMarks){
            Map<String, List<String>> listReaded = rd.read_AllColumn_CSV(path,';');
            allList.add(listReaded);
        }

        if (docs != null) {
            for (Integer id:docs.keySet()) {
                Document d= docs.get(id);
                //check if a single term into a text is find out into an list and replace it with the list's name.
                String[] textTerms = d.getText().split("\\s+");
                String finalString = "";
                for (String term : textTerms) {
                    String mark = null;
                    for (Map<String, List<String>> allMarkTerms : allList) {
                        mark = termToMark(term, allMarkTerms);
                        finalString = finalString + " " + mark;
                    }

                }
                 docs.get(id).setTextReplaced(finalString);
            }
        }
        else
            System.err.println("key not found!");
         return docs;
    }




    private String termToMark(String term, Map<String,List<String>> allMarksTerms){
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
