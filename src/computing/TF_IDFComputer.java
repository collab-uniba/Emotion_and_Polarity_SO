package computing;

import model.Document;
import printing.PrintingFile;
import utility.Utility;

import java.io.IOException;
import java.util.*;

/**
 * Created by Francesco on 30/12/2016.
 */
public class TF_IDFComputer {
    private Grams gr= new Grams();
    private Map<String, Double> wordTF=new LinkedHashMap<>();
    private Map<String, Double> termsIDF = new LinkedHashMap<>();
    /**
     * Calculate tf_idf
     * @param grams eg : unigrams -> !! uni1 , or bigrams -> zoom_out?	bi76368 ecc..
     * @param n
     * @return
     * @throws IOException
     */
    public Map<Integer,Document> tf_idf( Map<Integer, Document> documents,Map<String, String> grams,int n,String type) throws IOException {

        invertedDocumentFrequency(documents, grams.keySet(),n,type);
        PrintingFile pr = new PrintingFile();
        Set<String> k = grams.keySet();
        for (String ke : k) {
            Utility l = new Utility();
            l.directoryCreator("res/IDF");
            pr.printIDF(termsIDF, "res/IDF/IDF_" + grams.get(ke) + "_" + n);
            break;
        }
        String text="";
        for (Integer id : documents.keySet()) {
            text=getText(documents,id,type);

            termFrequency(text, n);

            LinkedHashMap<String, Double> gramsAndTFIDF = new LinkedHashMap<>();
            //prendo la lista di unigrammi adesso  e controllo se sta nel documento per ogni termine
            for (String s : grams.keySet()) {
                if (wordTF.get(s) != null) {
                    //se è presente in questo documento allora
                    double idf = termsIDF.get(s);
                    gramsAndTFIDF.put(grams.get(s), wordTF.get(s) * idf);
                } else {
                    gramsAndTFIDF.put(grams.get(s), 0.0);
                }
            }
            //aggiunta al documento
            Document d = documents.get(id);
            switch (type) {
                case "unigrams":
                    d.setUnigramTFIDF(gramsAndTFIDF);
                case "bigrams":
                    d.setBigramTFIDF(gramsAndTFIDF);
                case "positives":
                    d.setPositiveTFIDF(gramsAndTFIDF);
                case "negatives":
                    d.setNegativeTFIDF(gramsAndTFIDF);
                case "neutral":
                    d.setNeutralTFIDF(gramsAndTFIDF);
                case "ambiguos":
                    d.setAmbiguosTFIDF(gramsAndTFIDF);
            }
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
        return documents;
    }



    /**
     * Tf is : term's occourrences in doc / num doc's terms
     * @param input document
     * @param n indicates the N gram : n= 1 -> unigram, n=2 -> bigram ecc..
     * @return
     */
   private Map<String,Double> termFrequency(String input, int n){
        double occurrences=0;
        wordTF.clear();
        Collection<String> ss = gr.getNgrams(input, n);
        double numTermsInDoc = ss.size();
        //se ci sono 0 termini nel documento il ciclo non inizia proprio
        for (String string : ss) {
            if (wordTF.keySet().contains(string)) {
                occurrences = wordTF.get(string);
                occurrences++;
                double tf= occurrences/numTermsInDoc;
                wordTF.put(string, tf);
            } else {
                double tf= 1.0/numTermsInDoc;
                wordTF.put(string,tf);
            }
        }
        return wordTF;
    }

    /**
     * idf is : log _ 2 (totdocs / docsContainingTheTerm)
     * @param docs all tokenized docs
     * @param terms terms who idf will be calculate
     * @param n  indicates if it is a unigrams, bigrams ecc..
     * @return map of term-idf
     */
    private Map<String,Double> invertedDocumentFrequency(Map<Integer,Document> docs, Set<String> terms, int n,String type) {
        termsIDF.clear();
        double numDocs = docs.size();
        double totdocsContainingTerm = 0;
        for (String t : terms){
            //cerco il termine in tutti i documenti
            String text="";
            for (Integer id: docs.keySet()) {

                text= getText(docs,id,type);

                Collection<String> ss = gr.getNgrams(text, n);
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


    private String getText(Map<Integer,Document> docs,int id,String type){
        if(type.equals("unigrams") || type.equals("bigrams"))
           return  docs.get(id).getText();
        else
           return docs.get(id).getTextReplaced();
    }


}
