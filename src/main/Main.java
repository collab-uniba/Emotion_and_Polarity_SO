package main;


import computing.Grams;
import computing.TF_IDFComputer;
import replacing.ReplacerTextWithMarks;
import tokenizer.TokenizeCorpus;
import utility.Utility;
import model.CsvElements;
import printing.PrintingFile;
import printing.WriterCSV;
import reading.ReadingCSV;
import reading.ReadingFile;

import java.io.File;
import java.io.IOException;
import java.util.*;



public class Main {

    public static void main(String[] args) throws IOException {
        Grams gr = new Grams();

        /*extracting bigram or unigram lists*/
     /*   SortedMap<String, Integer> unigram= gr.getPositionWordMap(new File("res/onlyText"), 0, 1);
        Set<String> keysUni = unigram.keySet();
        for (String k : keysUni) {
            System.out.println(k + ": " + "\n");
            System.out.println(unigram.get(k) + " ");
        }
        System.out.println("\n");

        SortedMap<String, Integer> bigram = gr.getPositionWordMap(new File("res/onlyText"), 0, 2);
        Set<String> keysBi = bigram.keySet();
        for (String k : keysBi) {
            System.out.println(k + ": " + "\n");
            System.out.println(bigram.get(k) + " ");
        }
        System.out.println("\n");*/


       //Tokenizzatore

      PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf= new ReadingFile();
        Map<String, List<String>> inputCorpus = rd.read_AllColumn_CSV("res/inputCorpus.csv");
        pr.print("res/onlyText", inputCorpus.get("comment"));


        TokenizeCorpus tk = new TokenizeCorpus();
        tk.tokenizer("res/onlyText", "res/onlyText_TOKENIZED");
        List<String> inputCorpusTknz = rdf.read("res/onlyText_TOKENIZED");



       //tf-idf-bigrams
        TF_IDFComputer cl= new TF_IDFComputer();
        Utility u = new Utility();
        SortedMap<String, String> unigrams = gr.importNgrams(Main.class.getClassLoader().getResourceAsStream("UnigramsList"));
        System.out.println("unigrams loaded");
        SortedMap<String, String> bigrams = gr.importNgrams(Main.class.getClassLoader().getResourceAsStream("BigramsList"));
        System.out.println("bigrams loaded");
        List<Map<String,Double>> unigramsTFIDF= cl.tf_idf(inputCorpusTknz,unigrams,1);
        System.out.println("Tf-idf for unigrams , computed");
       List<Map<String,Double>> bigramsTFIDF= cl.tf_idf(inputCorpusTknz,bigrams,2);
        System.out.println("Tf-idf for bigrams , computed");


       //
      ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();
        List<String> paths = new ArrayList<>();
        paths.add("res/neutral_emotion.csv");
        paths.add("res/ambiguos-emotion.csv");
        paths.add("res/positive_emotion.csv");
        paths.add("res/negative_emotion.csv");

        Map<String, List<String>> pos = rd.read_AllColumn_CSV("res/positive_emotion.csv");
        Map<String, List<String>> neg = rd.read_AllColumn_CSV("res/negative_emotion.csv");
        Map<String, List<String>> neu = rd.read_AllColumn_CSV("res/neutral_emotion.csv");
        Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("res/ambiguos-emotion.csv");


        List<String> replaced = replacer.replaceTermsWithMarks("res/onlyText_TOKENIZED",paths);
        List<Map<String,Double>> posTFIDF= cl.tf_idf(replaced,u.createMap(pos),1);
        List<Map<String,Double>> negTFIDF= cl.tf_idf(replaced,u.createMap(neg),1);
        List<Map<String,Double>> neuTFIDF= cl.tf_idf(replaced,u.createMap(neu),1);
        List<Map<String,Double>> ambiTFIDF= cl.tf_idf(replaced,u.createMap(ambiguos),1);

        //ora scrivo sul CSV

        //passo gli ID letti in ordine dal coso originale, con i vari documenti
        CsvElements csv= new CsvElements();
        csv.setDocuments(inputCorpus.get("comment"));
        csv.setUnigramTFIDF(unigramsTFIDF);
        csv.setBigramTFIDF(bigramsTFIDF);
        csv.setPositiveTFIDF(posTFIDF);
        csv.setNegativeTFIDF(negTFIDF);
        csv.setNeutralTFIDF(neuTFIDF);
        csv.setAmbiguosTFIDF(ambiTFIDF);

        WriterCSV writerCSV= new WriterCSV();
        writerCSV.writeCsvFile("output.csv",csv);

    }
}


