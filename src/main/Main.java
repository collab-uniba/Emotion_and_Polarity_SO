package main;


import analysis.SentiStrengthSentiment;
import computing.Grams;
import computing.TF_IDFComputer;
import model.Document;
import printing.PrintingFile;
import printing.WriterCSV;
import reading.ReadingCSV;
import reading.ReadingFile;
import replacing.Removing;
import replacing.ReplacerTextWithMarks;
import tokenizer.TokenizeCorpus;
import utility.Utility;

import java.io.*;
import java.util.*;

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
           /* Post tagger*/
       /* POSTagger pt = new POSTagger();
        List<String> docsPostTagged =pt.posTag(inputCorpusTknz);
        pr.print("res/docsPostTagged",docsPostTagged);*/

public class Main {
    public static void main(String[] args) throws IOException {

        Grams gr = new Grams();
        Removing rem= new Removing();
        Utility u= new Utility();
   /*try {
       String[] cmd = {
               "/bin/bash",
               "-c",
               "echo password | python script.py '" + packe.toString() + "'"
       };
       Runtime.getRuntime().exec(cmd);
            Process p =  Runtime.getRuntime().exec("cmd python C:\\Users\\Francesco\\Desktop\\polFor27\\model.py");

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            System.out.println(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //PRE-PROCESSING, tokenizing, urlRemoving, Post_Tagging
        //Tokenizzatore
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf= new ReadingFile();

        List<String> inputCorpus = rd.read_Column_CSV(args[0],"comment",';');
        pr.print("res/onlyText", inputCorpus);


       TokenizeCorpus tk = new TokenizeCorpus();
        tk.tokenizerByToken("res/onlyText", "res/onlyText_TOKENIZED");
        List<String> inputCorpusTknz = rdf.read("res/onlyText_TOKENIZED");


         //Remove URL,usermention and special string
        List<String> docsWithoutURLtknz= rem.removeUrlOne(inputCorpusTknz);
       // pr.print("res/docsWithoutURLTknz",docsWithoutURLtknz);
        //remove user mention
        List<String> docsWithoutURLUserMentionTknz=rem.removeUserMention(docsWithoutURLtknz);
       // pr.print("res/docsWithoutURLUserMention",docsWithoutURLUserMentionTknz);


        //Remove special String
        List<String> docsWithotSpCUrlUsMtTknz= rem.removeQuotesSpecialString(docsWithoutURLUserMentionTknz,"type="+"\"submit\""+">");
        pr.print("res/docsWithoutURLUsMentSpCharTknz",docsWithotSpCUrlUsMtTknz);



        //extracting bigram or unigram lists
       /* System.out.println("Extracting unigrams..");
       SortedMap<String, String> unigrams = gr.getPositionWordMap(new File("res/docsWithoutURLUsMentSpCharTknz"), 0, 1);
        System.out.println("Unigrams Extracted successfully!");

        System.out.println("Extracting bigrams...");
       SortedMap<String,String> bigrams = gr.getPositionWordMap(new File("res/docsWithoutURLUsMentSpCharTknz"), 0, 2);
        System.out.println("Bigrams Extracted successfully!");

        System.out.println("\n");*/

        //Creo il map con indice del documento
        SortedMap<Integer, Document> documents= new TreeMap<>();
        int i=0;
        for(String doc: docsWithotSpCUrlUsMtTknz) {
            Document d= new Document();
            d.setText(doc);
            d.setId(i);
            documents.put(i,d);
            i++;
        }
        //FINE PREPROCESSING


        //*****tf-idf****/
       TF_IDFComputer cl= new TF_IDFComputer();

       SortedMap<String, String> unigrams = gr.importNgrams("res/Grams/UnigramsList");
        System.out.println("unigrams loaded");
        SortedMap<String, String> bigrams = gr.importNgrams("res/Grams/BigramsList");
        System.out.println("bigrams loaded");

        cl.tf_idf(documents,unigrams,1,"unigrams");
        System.out.println("Tf-idf for unigrams , computed");
        cl.tf_idf(documents,bigrams,2,"bigrams");
        System.out.println("Tf-idf for bigrams , computed");


        ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();

        Map<String, List<String>> pos = rd.read_AllColumn_CSV("res/EmotionsCSV/positive_emotion.csv",';');
        Map<String, List<String>> neg = rd.read_AllColumn_CSV("res/EmotionsCSV/negative_emotion.csv",';');
        Map<String, List<String>> neu = rd.read_AllColumn_CSV("res/EmotionsCSV/neutral_emotion.csv",';');
        Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("res/EmotionsCSV/ambiguos-emotion.csv",';');


        List<String> paths = new ArrayList<>();
        paths.add("res/EmotionsCSV/neutral_emotion.csv");
        paths.add("res/EmotionsCSV/ambiguos-emotion.csv");
        paths.add("res/EmotionsCSV/positive_emotion.csv");
        paths.add("res/EmotionsCSV/negative_emotion.csv");

        replacer.replaceTermsWithMarks(documents,paths);

        cl.tf_idf(documents,u.createMap(pos),1,"positives");
        cl.tf_idf(documents,u.createMap(neg),1,"negatives");
        cl.tf_idf(documents,u.createMap(neu),1,"neutrals");
        cl.tf_idf(documents,u.createMap(ambiguos),1,"ambiguos");



        SentiStrengthSentiment st = new SentiStrengthSentiment();
        st.SentiStrengthgetScoreForAllDocs(documents,0);
        System.out.println("Calculating positive score..");
        st.SentiStrengthgetScoreForAllDocs(documents,1);
        System.out.println("Calculating negative score...");

        System.out.println("Reading politeness and impoliteness..");
        List<String> politeness=rd.read_Column_CSV("res/textsPoliteAndImpolite.csv","polite",'§');
        List<String> impoliteness=rd.read_Column_CSV("res/textsPoliteAndImpolite.csv","impolite",'§');

        System.out.println("Reading modality..");
        List<String> min_modality= rd.read_Column_CSV("res/moodAndModality.csv","min_modality",'§');
        List<String> max_modality= rd.read_Column_CSV("res/moodAndModality.csv","max_modality",'§');

        System.out.println("Reading moods..");

        System.out.println("Reading modality...");
        List<String> indicatives=rd.read_Column_CSV("res/moodAndModality.csv","indicative",'§');
        List<String> conditional=rd.read_Column_CSV("res/moodAndModality.csv","conditional",'§');
        List<String> subjunctive= rd.read_Column_CSV("res/moodAndModality.csv","subjunctive",'§');
        List<String> imperative= rd.read_Column_CSV("res/moodAndModality.csv","imperative",'§');


        System.out.println("Reading emotions...");
        List<String> joy= rd.read_Column_CSV("res/EmotionsCSV/joy.csv","joy",';');
        List<String> love = rd.read_Column_CSV("res/EmotionsCSV/love.csv","love",';');
        List<String> surprise = rd.read_Column_CSV("res/EmotionsCSV/surprise.csv","surprise",';');
        List<String> sadness = rd.read_Column_CSV("res/EmotionsCSV/sadness.csv","sadness",';');
        List<String> fear = rd.read_Column_CSV("res/EmotionsCSV/fear.csv","fear",';');
        List<String> anger = rd.read_Column_CSV("res/EmotionsCSV/anger.csv","anger",';');

        //SETTO I TF_IDF
        Document d=null;
        for(Integer id:documents.keySet()){
            d= documents.get(id);
            d.setMood(new Document.Mood(conditional.get(id),imperative.get(id),subjunctive.get(id),indicatives.get(id)));
            d.setMin_modality(Double.valueOf(min_modality.get(id)));
            d.setMax_modality(Double.valueOf(max_modality.get(id)));
            d.setPoliteness(Double.valueOf(politeness.get(id)));
            d.setImpoliteness(Double.valueOf(impoliteness.get(id)));
            d.setLabel(joy.get(id));
        }
        u.directoryCreator("outputEmotion");
        WriterCSV writerCSV= new WriterCSV();
        int numUnig = unigrams.keySet().size();
        int numBigr= bigrams.keySet().size();


        writerCSV.writeCsvFile("outputEmotion/OutputJoy.csv",documents,numUnig,numBigr,pos.keySet(),neg.keySet(),neu.keySet(),ambiguos.keySet());

        for(Integer id:documents.keySet()){
            d= documents.get(id);
            d.setLabel(surprise.get(id));
        }
        writerCSV.writeCsvFile("outputEmotion/OutputSurprise.csv",documents,numUnig,numBigr,pos.keySet(),neg.keySet(),neu.keySet(),ambiguos.keySet());
        for(Integer id:documents.keySet()){
            d= documents.get(id);
            d.setLabel(sadness.get(id));
        }

        writerCSV.writeCsvFile("outputEmotion/OutputSadness.csv",documents,numUnig,numBigr,pos.keySet(),neg.keySet(),neu.keySet(),ambiguos.keySet());
        for(Integer id:documents.keySet()){
            d= documents.get(id);
            d.setLabel(fear.get(id));
        }
        writerCSV.writeCsvFile("outputEmotion/OutputFear.csv",documents,numUnig,numBigr,pos.keySet(),neg.keySet(),neu.keySet(),ambiguos.keySet());
        for(Integer id:documents.keySet()){
            d= documents.get(id);
            d.setLabel(anger.get(id));
        }
        writerCSV.writeCsvFile("outputEmotion/OutputAnger.csv",documents,numUnig,numBigr,pos.keySet(),neg.keySet(),neu.keySet(),ambiguos.keySet());
        for(Integer id:documents.keySet()){
            d= documents.get(id);
            d.setLabel(love.get(id));
        }
        writerCSV.writeCsvFile("outputEmotion/OutputLove.csv",documents,numUnig,numBigr,pos.keySet(),neg.keySet(),neu.keySet(),ambiguos.keySet());

        //politeness
       /*Politeness pt= new Politeness();
        pr.writeDocsValuesOnFile(pt.createFormatForInput("res/docsWithoutURLUsMentSpCharTknz"),"res/politeness");*/



    }

}


