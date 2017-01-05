package main;


import analysis.SentiStrengthSentiment;
import computing.Grams;
import computing.TF_IDFComputer;
import model.CsvElementsTFIDF;
import printing.PrintingFile;
import printing.WriterCSV;
import reading.ReadingCSV;
import reading.ReadingFile;
import replacing.POSTagger;
import replacing.RemoveNotEnglishWords;
import replacing.ReplacerTextWithMarks;
import tokenizer.TokenizeCorpus;
import utility.Utility;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {

        Grams gr = new Grams();

        //PRE-PROCESSING, tokenizing, urlRemoving, Post_Tagging
        //Tokenizzatore
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf= new ReadingFile();

        Map<String, List<String>> inputCorpus = rd.read_AllColumn_CSV("res/inputCorpus.csv",';');
        pr.print("res/onlyText", inputCorpus.get("comment"));


        TokenizeCorpus tk = new TokenizeCorpus();
        tk.tokenizerByToken("res/onlyText", "res/onlyText_TOKENIZED");
        List<String> inputCorpusTknz = rdf.read("res/onlyText_TOKENIZED");
     /*extracting bigram or unigram lists*/
    /*   SortedMap<String, Integer> unigram= gr.getPositionWordMap(new File("res/onlyText_TOKENIZED"), 0, 1);
        Set<String> keysUni = unigram.keySet();
        for (String k : keysUni) {
            System.out.println(k + ": " + "\n");
            System.out.println(unigram.get(k) + " ");
        }
        System.out.println("\n");*/

      /* SortedMap<String, Integer> bigram = gr.getPositionWordMap(new File("res/onlyText"), 0, 2);
        Set<String> keysBi = bigram.keySet();
        for (String k : keysBi) {
            System.out.println(k + ": " + "\n");
            System.out.println(bigram.get(k) + " ");
        }
        System.out.println("\n");*/


        //Remove non-english words
        RemoveNotEnglishWords rmNotEnglishWords= new RemoveNotEnglishWords();
        List<String> paths = new ArrayList<>();
        paths.add("res/neutral_emotion.csv");
        paths.add("res/ambiguos-emotion.csv");
        paths.add("res/positive_emotion.csv");
        paths.add("res/negative_emotion.csv");

        List<String> docsWithoutNotEnglishWords= rmNotEnglishWords.removeNotEnglishWords(inputCorpusTknz);
        System.out.println("printing docs without not english word...");
        pr.print("res/docsWithoutNotEnglishWords",docsWithoutNotEnglishWords);

        //Remove URL
       /* RemoveURL rmurl= new RemoveURLOne();
        List<String> docsWithoutURLndNotEnglishWordsTknz= rmurl.removeUrlOne(docsPostTagged);
        pr.print("res/docsWithoutURLAndNotEnglishWords",docsWithoutURLAndNotEnglishWordsTknz);*/

       /* Post tagger*/
       /* POSTagger pt = new POSTagger();
        List<String> docsPostTagged =pt.posTag(inputCorpusTknz);
        pr.print("res/docsPostTagged",docsPostTagged);*/



          /*extracting bigram or unigram lists*/
      /* SortedMap<String, Integer> unigram= gr.getPositionWordMap(new File("res/docsWithoutURLAndNotEnglishWordsTokenized"), 0, 1);
        Set<String> keysUni = unigram.keySet();
        for (String k : keysUni) {
            System.out.println(k + ": " + "\n");
            System.out.println(unigram.get(k) + " ");
        }
        System.out.println("\n");

        SortedMap<String, Integer> bigram = gr.getPositionWordMap(new File("res/docsWithoutURLTokenized"), 0, 2);
        Set<String> keysBi = bigram.keySet();
        for (String k : keysBi) {
            System.out.println(k + ": " + "\n");
            System.out.println(bigram.get(k) + " ");
        }

        System.out.println("\n");
*/
        //FINE PREPROCESSING


        //tf-idf-bigrams
       /* TF_IDFComputer cl= new TF_IDFComputer();

        Utility u = new Utility();

        List<String> paths = new ArrayList<>();
        paths.add("res/neutral_emotion.csv");
        paths.add("res/ambiguos-emotion.csv");
        paths.add("res/positive_emotion.csv");
        paths.add("res/negative_emotion.csv");
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

        Map<String, List<String>> pos = rd.read_AllColumn_CSV("res/positive_emotion.csv",';');
        Map<String, List<String>> neg = rd.read_AllColumn_CSV("res/negative_emotion.csv",';');
        Map<String, List<String>> neu = rd.read_AllColumn_CSV("res/neutral_emotion.csv",';');
        Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("res/ambiguos-emotion.csv",';');


        List<String> replaced = replacer.replaceTermsWithMarks("res/onlyText_TOKENIZED",paths);
        List<Map<String,Double>> posTFIDF= cl.tf_idf(replaced,u.createMap(pos),1);
        List<Map<String,Double>> negTFIDF= cl.tf_idf(replaced,u.createMap(neg),1);
        List<Map<String,Double>> neuTFIDF= cl.tf_idf(replaced,u.createMap(neu),1);
        List<Map<String,Double>> ambiTFIDF= cl.tf_idf(replaced,u.createMap(ambiguos),1);

        //ora scrivo sul CSV

        //passo gli ID letti in ordine dal coso originale, con i vari documenti
        CsvElementsTFIDF csv= new CsvElementsTFIDF();
        csv.setDocuments(inputCorpus.get("comment"));
        csv.setUnigramTFIDF(unigramsTFIDF);
        csv.setBigramTFIDF(bigramsTFIDF);
        csv.setPositiveTFIDF(posTFIDF);
        csv.setNegativeTFIDF(negTFIDF);
        csv.setNeutralTFIDF(neuTFIDF);
        csv.setAmbiguosTFIDF(ambiTFIDF);

        WriterCSV writerCSV= new WriterCSV();
        writerCSV.writeCsvFile("res/TFIDF.csv",csv);*/

      //  SentiStrengthSentiment st = new SentiStrengthSentiment();
       // st.SentiStrengthgetScore("Excellent ! This is exactly what I needed . Thanks ! ");
        //aggiungilo nella stessa posizione messa da federico

        //politeness
        //fai prima post_tag


    }
}


