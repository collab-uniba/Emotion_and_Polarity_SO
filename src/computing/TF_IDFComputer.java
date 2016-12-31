package computing;

import java.io.IOException;
import java.util.*;

/**
 * Created by Francesco on 30/12/2016.
 */
public class TF_IDFComputer {
    private Grams gr= new Grams();
    public List<LinkedHashMap<String,Double>> tf_idf(List<String> docs, Map<String, String> grams ,int n) throws IOException {

        Map<String, Double> wordTF;
        List<LinkedHashMap<String, Double>> allTFIDF = new ArrayList<>();

        LinkedHashMap<String, Double> keyIDF = invertedDocumentFrequency(docs, grams.keySet(), n);


        for (String doc : docs) {
            wordTF = termFrequency(doc, n);
            LinkedHashMap<String, Double> gramsAndTFIDF = new LinkedHashMap<>();
            //prendo la lista di unigrammi adesso  e controllo se sta nel documento
            for (String s : grams.keySet()) {
                if (wordTF.get(s) != null) {
                    //se è presente in questo documento allora
                    double idf = keyIDF.get(s);
                    gramsAndTFIDF.put(grams.get(s), wordTF.get(s) * idf);
                } else {
                    gramsAndTFIDF.put(grams.get(s), 0.0);
                }
            }
            allTFIDF.add(gramsAndTFIDF);
        }

        //stampa
     /*   int i=1;
        for (LinkedHashMap<String, Double> l : allTFIDF){
            System.out.println("Documento: " + i +"\n" );
            for(String s: l.keySet()){
                if(l.get(s)>0.0)
                  System.out.println("grams: " + s + "tf-idf "+ new DecimalFormat("#.##").format(l.get(s))+"\n");
            }
            i++;+

    }*/
        return allTFIDF;
    }


   private Map<String,Double> termFrequency(String input, int n){

        double occurrences=0;
        Map<String, Double> mapTF = new LinkedHashMap<>();
        Collection<String> ss = gr.getNgrams(input, n);
        double numTermsInDoc = ss.size();
        for (String string : ss) {
            if (mapTF.keySet().contains(string)) {
                occurrences = mapTF.get(string);
                occurrences++;
                double tf= occurrences/numTermsInDoc;
                mapTF.put(string, tf);
            } else {
                double tf= 1.0/numTermsInDoc;
                mapTF.put(string,tf);
            }
        }
        return mapTF;
    }

    //n=1: unigrammi , n=2 : bigrammi
    private LinkedHashMap<String,Double> invertedDocumentFrequency(List<String> docs, Set<String> terms, int n) {
        LinkedHashMap<String, Double> termsIDF = new LinkedHashMap<>();
        double numDocs = docs.size();
        double totdocsContainingTerm = 0;
        for (String t : terms){
            for (String doc : docs) {
                Collection<String> ss = gr.getNgrams(doc, n);
                for (String string : ss) {
                    if (string.equals(t)) {
                        totdocsContainingTerm++;
                        break;
                    }
                }
            }
            double part = numDocs / totdocsContainingTerm;
            double idf= Math.round(Logarithm.logb(part, 2));
            termsIDF.put(t,idf);
        }
        return termsIDF;
    }



}
