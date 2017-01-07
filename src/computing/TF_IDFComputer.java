package computing;

import printing.PrintingFile;

import java.io.IOException;
import java.util.*;

/**
 * Created by Francesco on 30/12/2016.
 */
public class TF_IDFComputer {
    private Grams gr= new Grams();

    /**
     * Calculate tf_idf
     * @param docs all tokenized docs
     * @param grams eg : unigrams -> !! uni1 , or bigrams -> zoom_out?	bi76368 ecc..
     * @param n
     * @return
     * @throws IOException
     */
    public List<Map<String,Double>> tf_idf(List<String> docs, Map<String, String> grams ,int n) throws IOException {

        Map<String, Double> wordTF;
        List<Map<String, Double>> allTFIDF = new ArrayList<>();

        Map<String, Double> keyIDF = invertedDocumentFrequency(docs, grams.keySet(), n);
        PrintingFile pr= new PrintingFile();
        Set<String> k= grams.keySet();
        for(String ke:k) {
            pr.printIDF(keyIDF, "res/IDF_" + grams.get(ke)+"_"+n);
            break;
        }
        int i=0;
        for (String doc : docs) {
            wordTF = termFrequency(doc, n);
            for(String ke:k) {
             pr.printIDF(wordTF, "res/TF_"+i+grams.get(ke)+"_"+n);
             break;
            }
            LinkedHashMap<String, Double> gramsAndTFIDF = new LinkedHashMap<>();
            //prendo la lista di unigrammi adesso  e controllo se sta nel documento per ogni termine
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
            i++;
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



    /**
     * Tf is : term's occourrences in doc / num doc's terms
     * @param input document
     * @param n indicates the N gram : n= 1 -> unigram, n=2 -> bigram ecc..
     * @return
     */
   private Map<String,Double> termFrequency(String input, int n){

        double occurrences=0;
        Map<String, Double> mapTF = new LinkedHashMap<>();
        Collection<String> ss = gr.getNgrams(input, n);
        double numTermsInDoc = ss.size();
        //se ci sono 0 termini nel documento il ciclo non inizia proprio
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

    /**
     * idf is : log _ 2 (totdocs / docsContainingTheTerm)
     * @param docs all tokenized docs
     * @param terms terms who idf will be calculate
     * @param n  indicates if it is a unigrams, bigrams ecc..
     * @return map of term-idf
     */
    private Map<String,Double> invertedDocumentFrequency(List<String> docs, Set<String> terms, int n) {
        LinkedHashMap<String, Double> termsIDF = new LinkedHashMap<>();
        double numDocs = docs.size();
        double totdocsContainingTerm = 0;
        for (String t : terms){
            //cerco il termine in tutti i documenti
            for (String doc : docs) {
                Collection<String> ss = gr.getNgrams(doc, n);
                for (String string : ss) {
                    if (string.equals(t)) {
                        totdocsContainingTerm++;
                        break;
                    }
                }
            }
            double part=0.0;
            double idf=0.0;
            if(totdocsContainingTerm>0){
                part = numDocs / totdocsContainingTerm;
                idf= Math.round(Logarithm.logb(part, 2));
            }
            termsIDF.put(t,idf);
            totdocsContainingTerm = 0;
        }
        return termsIDF;
    }



}
