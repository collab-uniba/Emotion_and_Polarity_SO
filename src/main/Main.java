package main;


import analysis.Politeness;
import analysis.SentiStrengthSentiment;
import computing.Grams;
import computing.TF_IDFComputer;
import model.CsvElementsTFIDF;
import printing.PrintingFile;
import printing.WriterCSV;
import reading.ReadingCSV;
import reading.ReadingFile;
import replacing.RemoveURL;
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

       Map<String, List<String>> inputCorpus = rd.read_AllColumn_CSV(args[0],';');
        pr.print("res/onlyText", inputCorpus.get("comment"));


        TokenizeCorpus tk = new TokenizeCorpus();
        tk.tokenizerByToken("res/onlyText", "res/onlyText_TOKENIZED");
        List<String> inputCorpusTknz = rdf.read("res/onlyText_TOKENIZED");

        //Remove non-english words
      /*  RemoveNotEnglishWords rmNotEnglishWords= new RemoveNotEnglishWords();
        List<String> paths = new ArrayList<>();
        paths.add("res/neutral_emotion.csv");
        paths.add("res/ambiguos-emotion.csv");
        paths.add("res/positive_emotion.csv");
        paths.add("res/negative_emotion.csv");

        List<String> docsWithoutNotEnglishWords= rmNotEnglishWords.removeNotEnglishWords(inputCorpusTknz);
        System.out.println("printing docs without not english word...");
        pr.print("res/docsWithoutNotEnglishWords",docsWithoutNotEnglishWords);*/

        //Remove URL
       RemoveURL rmurl= new RemoveURL();
        List<String> docsWithoutURLtknz= rmurl.removeUrlOne(inputCorpusTknz);
        pr.print("res/docsWithoutURLTknz",docsWithoutURLtknz);
        //remove user mention
        List<String> docsWithoutURLUserMentionTknz=gr.removeUserMention(docsWithoutURLtknz);
        pr.print("res/docsWithoutURLUserMention",docsWithoutURLUserMentionTknz);


       /* Post tagger*/
       /* POSTagger pt = new POSTagger();
        List<String> docsPostTagged =pt.posTag(inputCorpusTknz);
        pr.print("res/docsPostTagged",docsPostTagged);*/



          /*extracting bigram or unigram lists*/
      /* SortedMap<String, Integer> unigram= gr.getPositionWordMap(new File("res/docsWithoutURLUserMention"), 0, 1);
        Set<String> keysUni = unigram.keySet();
        for (String k : keysUni) {
            System.out.println(k + ": " + "\n");
            System.out.println(unigram.get(k) + " ");
        }
        System.out.println("\n");

        SortedMap<String, Integer> bigram = gr.getPositionWordMap(new File("res/docsWithoutURLUserMention"), 0, 2);
        Set<String> keysBi = bigram.keySet();
        for (String k : keysBi) {
            System.out.println(k + ": " + "\n");
            System.out.println(bigram.get(k) + " ");
        }

        System.out.println("\n");*/

        //FINE PREPROCESSING


        //tf-idf-bigrams
      /*  TF_IDFComputer cl= new TF_IDFComputer();

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

        List<Map<String,Double>> unigramsTFIDF= cl.tf_idf(docsWithoutURLUserMentionTknz,unigrams,1);
        System.out.println("Tf-idf for unigrams , computed");
       List<Map<String,Double>> bigramsTFIDF= cl.tf_idf(docsWithoutURLUserMentionTknz,bigrams,2);
        System.out.println("Tf-idf for bigrams , computed");


       //
      ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();

        Map<String, List<String>> pos = rd.read_AllColumn_CSV(args[1],';');
        Map<String, List<String>> neg = rd.read_AllColumn_CSV(args[2],';');
        Map<String, List<String>> neu = rd.read_AllColumn_CSV(args[3],';');
        Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV(args[4],';');


        List<String> replaced = replacer.replaceTermsWithMarks("res/docsWithoutURLUserMention",paths);
        List<Map<String,Double>> posTFIDF= cl.tf_idf(replaced,u.createMap(pos),1);
        List<Map<String,Double>> negTFIDF= cl.tf_idf(replaced,u.createMap(neg),1);
        List<Map<String,Double>> neuTFIDF= cl.tf_idf(replaced,u.createMap(neu),1);
        List<Map<String,Double>> ambiTFIDF= cl.tf_idf(replaced,u.createMap(ambiguos),1);

        Map<String, List<String>> joy = rd.read_AllColumn_CSV("res/EmotionsCSV/joy.csv",';');
        Map<String, List<String>> love = rd.read_AllColumn_CSV("res/EmotionsCSV/love.csv",';');
        Map<String, List<String>> surprise = rd.read_AllColumn_CSV("res/EmotionsCSV/surprise.csv",';');
        Map<String, List<String>> sadness = rd.read_AllColumn_CSV("res/EmotionsCSV/sadness.csv",';');
        Map<String, List<String>> fear = rd.read_AllColumn_CSV("res/EmotionsCSV/fear.csv",';');
        Map<String, List<String>> anger = rd.read_AllColumn_CSV("res/EmotionsCSV/anger.csv",';');




        SentiStrengthSentiment st = new SentiStrengthSentiment();
        Map<String,Double> posScore= st.SentiStrengthgetScoreForAllDocs(docsWithoutURLUserMentionTknz,0);
        System.out.println("Calculating positive score..");
        Map<String,Double> negScore= st.SentiStrengthgetScoreForAllDocs(docsWithoutURLUserMentionTknz,1);
        System.out.println("Calculating negative score...");
        //ora scrivo sul CSV

        //passo gli ID letti in ordine dal coso originale, con i vari documenti
        CsvElementsTFIDF csv= new CsvElementsTFIDF();
        csv.setDocuments(docsWithoutURLtknz);
        csv.setUnigramTFIDF(unigramsTFIDF);
        csv.setBigramTFIDF(bigramsTFIDF);
        csv.setPositiveTFIDF(posTFIDF);
        csv.setNegativeTFIDF(negTFIDF);
        csv.setNeutralTFIDF(neuTFIDF);
        csv.setAmbiguosTFIDF(ambiTFIDF);
        csv.setPos_score(posScore);
        csv.setNeg_score(negScore);



        //Creazione dei csv
        csv.setLabels(joy.get("joy"));
        WriterCSV writerCSV= new WriterCSV();
        writerCSV.writeCsvFile("outputEmotion/ScoreTfIdfForEmotion/OutputJoy.csv",csv);
        csv.setLabels(love.get("love"));
        writerCSV.writeCsvFile("outputEmotion/ScoreTfIdfForEmotion/OutputLove.csv",csv);
        csv.setLabels(surprise.get("surprise"));
        writerCSV.writeCsvFile("outputEmotion/ScoreTfIdfForEmotion/OutputSurprise.csv",csv);
        csv.setLabels(sadness.get("sadness"));
        writerCSV.writeCsvFile("outputEmotion/ScoreTfIdfForEmotion/OutputSadness.csv",csv);
        csv.setLabels(fear.get("fear"));
        writerCSV.writeCsvFile("outputEmotion/ScoreTfIdfForEmotion/OutputFear.csv",csv);
        csv.setLabels(anger.get("anger"));
        writerCSV.writeCsvFile("outputEmotion/ScoreTfIdfForEmotion/OutputAnger.csv",csv);*/


        //politeness
        Politeness  pt= new Politeness();
        pr.writeDocsValuesOnFile(pt.createFormatForInput("res/docsWithoutURLUserMention"),"res/politeness");



    }

}


