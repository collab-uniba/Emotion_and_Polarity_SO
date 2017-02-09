package replacing;

import model.Document;
import printing.PrintingFile;
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
    public Map<String,Document> replaceTermsWithMarks(Map<String, Document> docs, List<String> pathsMarks,String pathDir) throws IOException {
        ReadingCSV rd = new ReadingCSV();
        PrintingFile pr = new PrintingFile();
        List<String> txt = new ArrayList<>();

        List<Map<String,List<String>>> allList = new ArrayList<>();
        //read all files , each of them is formed by : n list's name and n terms for each of them
        for(String path: pathsMarks){
            Map<String, List<String>> listReaded = rd.read_AllColumn_CSV(path,';');
            allList.add(listReaded);
        }

        if (docs != null) {
            for (String id:docs.keySet()) {
                Document d= docs.get(id);
                //check if a single term into a text is find out into an list and replace it with the list's name.
                String[] textTerms = d.getText().split("\\s+");
                String finalString = "";
                for (String term : textTerms) {
                    String mark = null;
                    //in alcuni csv ci sono valori come "" quindi farebbe matching , restituendo una cosa errata..
                    if(!term.equals("")) {
                        //cerco nella lista dei positivi, negativi, neutrals e ambigui. Appena lo trovo blocco la ricerca e passo al prossimo termine del documento.
                        for (Map<String, List<String>> allMarkTerms : allList) {
                            mark = termToMark(term, allMarkTerms);
                            if (!mark.equals(" ")) {
                                finalString = finalString + " " + mark;
                                break;
                            }
                        }
                    }
                }
                 docs.get(id).setTextReplaced(finalString);
                finalString = id + "\t" + finalString;
                 txt.add(finalString);
            }

            pr.print(pathDir+"/ElaboratedFiles/TextsReplacedWithWordnetCategories.txt",txt);
        }
        else
            System.err.println("key not found!");
         return docs;
    }




    private String termToMark(String term, Map<String,List<String>> allMarksTerms){
        Set<String> keys= allMarksTerms.keySet();
        for(String mark : keys){

            if(mark.toLowerCase().equals(term)){
                return mark.toLowerCase();
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
