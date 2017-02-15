package computing;

import model.Document;
import printing.PrintingFile;
import reading.ReadingFile;
import utility.Utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Francesco on 30/12/2016.
 */
public class TermFrequency_InverseDocumentFrequency {
    private Grams gr = new Grams();
    private Map<String, Double> wordTF = new LinkedHashMap<>();
    private Map<String, Double> termsIDF = new LinkedHashMap<>();
    private Utility l = new Utility();
    private ReadingFile rd = new ReadingFile();
    private PrintingFile pr = new PrintingFile();
    private String path="";
    /**
     * Calculate tf_idf
     * @param grams eg : unigrams -> !! uni1 , or bigrams -> zoom_out?	bi76368 ecc..
     * @param n
     * @return
     * @throws IOException
     */
    public void tf_idf(Map<String, Document> documents, Map<String, String> grams, int n, String gramsType, String path,String taskType) throws InterruptedException, FileNotFoundException{

        this.path=path;

        if(taskType.equals("classification")){
            termsIDF = rd.readIDF(gramsType,path+"/idfs/");
        }
        else if(taskType.equals("training"))
            invertedDocumentFrequency(documents, grams.keySet(), n, gramsType);

        String text = "";

        for (String id : documents.keySet()) {
            System.out.println("Computing tf_idf for the doc " + id + "\n");
            Thread.sleep(5);

            //recover the text
            text = getText(documents, id, gramsType);
            //calculate TF
            termFrequency(text, n);

            LinkedHashMap<String, Double> gramsAndTFIDF = new LinkedHashMap<>();
            //Check into the type list [unigrams, bigrams or wornet categories(pos, neg , ambiguos and neutrals)] if the document contains the term.
            for (String s : grams.keySet()) {
                if (wordTF.get(s) != null) {
                    //if the list's term is present in the document , recovers the IDF and put the tf*idf into the map
                    double idf = termsIDF.get(s);
                    double tf_idf = wordTF.get(s) * idf;
                    gramsAndTFIDF.put(grams.get(s), tf_idf);
                } else {
                    gramsAndTFIDF.put(grams.get(s), 0.0);
                }
            }
            //adding the map (term, tf-idf) to the document
            Document d = documents.get(id);
            switch (gramsType) {
                case "unigrams_1": {
                    d.setUnigramTFIDF(gramsAndTFIDF);
                }
                case "bigrams_1": {
                    d.setBigramTFIDF(gramsAndTFIDF);
                }
                case "unigrams_2": {
                    d.setUnigramTFIDF(gramsAndTFIDF);
                }
                case "bigrams_2": {
                    d.setBigramTFIDF(gramsAndTFIDF);
                }
                case "positives": {
                    d.setPositiveTFIDF(gramsAndTFIDF);
                }
                case "negatives": {
                    d.setNegativeTFIDF(gramsAndTFIDF);
                }
                case "neutrals": {
                    d.setNeutralTFIDF(gramsAndTFIDF);
                }
                case "ambiguos": {
                    d.setAmbiguosTFIDF(gramsAndTFIDF);
                }
            }
        }
    }




    /**
     * Tf is : term's occourrences in doc / num doc's terms
     * @param input document
     * @param n indicates the N gram : n= 1 -> unigram, n=2 -> bigram ecc..
     * @return
     */
   private Map<String,Double> termFrequency(String input, int n){
        double occurrences=0;
        double tf=0;
        wordTF.clear();
        Collection<String> ss = gr.getNgrams(input, n);
        double numTermsInDoc = ss.size();
        //se ci sono 0 termini nel documento il ciclo non inizia proprio
        for (String string : ss) {
            if (wordTF.keySet().contains(string)) {
                occurrences = wordTF.get(string);
                occurrences++;
                wordTF.put(string, occurrences);
            } else {
                wordTF.put(string,1.0);
            }
        }
        //Change the occourrences in tf.
        for(String k: wordTF.keySet()){
           occurrences=  wordTF.get(k);
           tf = occurrences/numTermsInDoc;
           wordTF.put(k, tf);
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
    private Map<String,Double> invertedDocumentFrequency(Map<String,Document> docs, Set<String> terms, int n,String type) throws InterruptedException {
        System.out.println("Computing idf for :  " + type + "\n");
        Thread.sleep(100);
        termsIDF.clear();
        double numDocs = docs.size();
        double totdocsContainingTerm = 0;
        Collection<String> ss=null;
        int i=0;
        for (String t : terms){
            System.out.println("term num : "+ i+"\n");
            Thread.sleep(3);
            //finding terms into all documents
            String text="";
            for (String id: docs.keySet()) {

                text= getText(docs,id,type);

                ss = gr.getNgrams(text, n);

                if (ss.contains(t)) {
                    totdocsContainingTerm++;
                }
            }
            double part=0.0;
            double idf=0.0;
            if(totdocsContainingTerm>0){
                part = numDocs / totdocsContainingTerm;
                idf= Logarithm.logb(part, 10);
            }
            termsIDF.put(t,idf);
            totdocsContainingTerm = 0;
            i++;
        }
        System.out.println("Idf computer for " + type + "\n");
        Thread.sleep(100);


        //PRINTING

        System.out.println("Printing idf for " + type + "\n");
        Thread.sleep(100);

        l.directoryCreator(path + "/idfs");
        pr.printIDF(termsIDF, path + "/idfs/" + type + ".txt");

        System.out.println("Printed idf for " + type + "\n");
        Thread.sleep(100);


        return termsIDF;
    }


    private String getText(Map<String,Document> docs,String id,String type){
        if(type.equals("unigrams_1") || type.equals("bigrams_1") || type.equals("unigrams_2") || type.equals("bigrams_2"))
           return  docs.get(id).getText();
        else
           return docs.get(id).getTextReplaced();
    }



}
