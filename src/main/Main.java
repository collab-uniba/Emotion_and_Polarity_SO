package main;


import exceptions.CsvColumnNotFound;
import analysis.Politeness;
import analysis.SentiStrengthSentiment;
import computing.Grams;
import computing.TermFrequency_InverseDocumentFrequency;
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

/*
-i res/StackOverflowCSV/anger.csv -P res/textsPoliteAndImpolite.csv -M res/textsMoodAndModality.csv -d ; -e emotion
-i res/StackOverflowCSV/anger.csv -P res/textsPoliteAndImpolite.csv -M res/textsMoodAndModality.csv -d ; -G -e emotion
-i res/StackOverflowCSV/anger.csv -P -d ;
 */

public class Main {
    public static void main(String[] args) throws IOException {

        Grams gr = new Grams();
        Removing rem = new Removing();
        Utility u = new Utility();
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf = new ReadingFile();

        int numArgs= args.length;

        if (numArgs == 5 || numArgs == 10 || numArgs== 11 ) {
            //Take the args values
            String emotionIndicate="";
            String input="";
            String politeFile="";
            boolean createFormatForPoliteness=false;
            String moodFile="";
            Character delimiter=' ';
            boolean extractDictionary=false;
            if(numArgs==5) {
                for (int i = 0; i < args.length; i++) {
                    switch (args[i]) {
                        case "-i": {
                            input = args[i + 1];
                            break;
                        }
                        case "-P": {
                            createFormatForPoliteness = true;
                            break;
                        }
                        case "-d": {
                            delimiter = args[i + 1].charAt(0);
                            break;
                        }

                    }
                }
            }
            else {
                for(int i=0;i<args.length;i++) {
                    switch (args[i]) {
                        case "-i": {
                            input = args[i + 1];
                            break;
                        }
                        case "-P": {
                            politeFile = args[i + 1];
                            break;
                        }
                        case "-M": {
                            moodFile = args[i + 1];
                            break;
                        }
                        case "-e": {
                            emotionIndicate = args[i + 1];
                            break;
                        }
                        case "-G": {
                            extractDictionary = true;
                            break;
                        }
                        case "-d": {
                            delimiter = args[i + 1].charAt(0);
                            break;
                        }
                    }
                }
            }


            String[] ss = input.split("/");
            String fileCsv= ss[ss.length-1].replaceAll(".csv","");
            String path="Output_"+fileCsv;
            u.directoryCreator(path);
            u.directoryCreator(path+"/ElaboratedFiles");




            //PRE-PROCESSING, tokenizing, urlRemoving
            //Tokenizzatore


            List<String> inputCorpus = null;
            try {

                inputCorpus = rd.read_Column_CSV(input, "text", delimiter);
                pr.print(path+"/ElaboratedFiles/onlyText.txt", inputCorpus);


                TokenizeCorpus tk = new TokenizeCorpus();
                tk.tokenizerByToken(path+"/ElaboratedFiles/onlyText.txt", path+"/ElaboratedFiles/onlyText_TOKENIZED.txt");
                List<String> inputCorpusTknz = rdf.read(path+"/ElaboratedFiles/onlyText_TOKENIZED.txt");


                //Remove URL,usermention and special string
                List<String> docsWithoutURLtknz = rem.removeUrlOne(inputCorpusTknz);
                //remove user mention

                List<String> docsWithoutURLUserMentionTknz = rem.removeUserMention(docsWithoutURLtknz);

                //Remove special String
                List<String> docsPrePreProcessed = rem.escaping(docsWithoutURLUserMentionTknz);
                pr.print(path+"/ElaboratedFiles/onlyText_PreProcessed.txt", docsPrePreProcessed);

                if (!createFormatForPoliteness) {

                    SortedMap<String, String> unigrams = null;
                    SortedMap<String, String> bigrams = null;
                    //extracting bigram or unigram lists
                    if (extractDictionary) {
                        unigrams = gr.getPositionWordMap(new File(path+"/ElaboratedFiles/onlyText_PreProcessed.txt"),path,0, 1);
                        bigrams = gr.getPositionWordMap(new File(path+"/ElaboratedFiles/onlyText_PreProcessed.txt"),path, 0, 2);
                    } else {
                        unigrams = gr.importNgrams(path + "/Dictionary/UnigramsList.txt", 1);
                        bigrams = gr.importNgrams(path + "/Dictionary/BigramsList.txt", 2);
                    }


                    //Creo il map con indice del documento
                    Map<String, Document> documents = new LinkedHashMap<>();
                    List<String> ids = rd.read_Column_CSV(input, "id", delimiter);
                    int pos_doc = 0;
                    for (String i : ids) {
                        Document d = new Document();
                        d.setText(docsPrePreProcessed.get(pos_doc));
                        d.setId(i);
                        documents.put(i, d);
                        pos_doc++;
                    }
                    //END PREPROCESSING


                    //*****tf-idf****/
                    TermFrequency_InverseDocumentFrequency cl = new TermFrequency_InverseDocumentFrequency();


                    cl.tf_idf(documents, unigrams, 1, "unigrams",path);
                    System.out.println("Tf-idf for unigrams , computed");


                    cl.tf_idf(documents, bigrams, 2, "bigrams",path);
                    System.out.println("Tf-idf for bigrams , computed");


                    ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();


                    Map<String, List<String>> pos = rd.read_AllColumn_CSV("Resources/WordnetCategories/positive_emotion.csv", ';');
                    Map<String, List<String>> neg = rd.read_AllColumn_CSV("Resources/WordnetCategories/negative_emotion.csv", ';');
                    Map<String, List<String>> neu = rd.read_AllColumn_CSV("Resources/WordnetCategories/neutral_emotion.csv", ';');
                    Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("Resources/WordnetCategories/ambiguos-emotion.csv", ';');


                    List<String> paths = new ArrayList<>();
                    paths.add("Resources/WordnetCategories/neutral_emotion.csv");
                    paths.add("Resources/WordnetCategories/ambiguos-emotion.csv");
                    paths.add("Resources/WordnetCategories/positive_emotion.csv");
                    paths.add("Resources/WordnetCategories/negative_emotion.csv");

                    replacer.replaceTermsWithMarks(documents, paths,path);


                    cl.tf_idf(documents, u.createMap(pos), 1, "positives",path);
                    cl.tf_idf(documents, u.createMap(neg), 1, "negatives",path);
                    cl.tf_idf(documents, u.createMap(neu), 1, "neutrals",path);
                    cl.tf_idf(documents, u.createMap(ambiguos), 1, "ambiguos",path);


                    SentiStrengthSentiment st = new SentiStrengthSentiment();
                    st.SentiStrengthgetScoreForAllDocs(documents, 0);
                    System.out.println("Reading positive score..");
                    st.SentiStrengthgetScoreForAllDocs(documents, 1);
                    System.out.println("Reading negative score...");

                    System.out.println("Reading politeness and impoliteness..");
                    List<String> politeness = rd.read_Column_CSV(politeFile, "polite", ',');
                    List<String> impoliteness = rd.read_Column_CSV(politeFile, "impolite", ',');

                    System.out.println("Reading modality..");
                    List<String> min_modality = rd.read_Column_CSV(moodFile, "min_modality", ',');
                    List<String> max_modality = rd.read_Column_CSV(moodFile, "max_modality", ',');

                    System.out.println("Reading moods..");

                    List<String> indicatives = rd.read_Column_CSV(moodFile, "indicative", ',');
                    List<String> conditional = rd.read_Column_CSV(moodFile, "conditional", ',');
                    List<String> subjunctive = rd.read_Column_CSV(moodFile, "subjunctive", ',');
                    List<String> imperative = rd.read_Column_CSV(moodFile, "imperative", ',');


                    System.out.println("Reading emotions...");

                    //1 dataset per volta.
                    List<String> emotion=null;


                    emotion = rd.read_Column_CSV(input, "label", delimiter);


                    System.out.println("Emotion readed...");

                    //SETTO I TF_IDF
                    Document d = null;
                    pos_doc = 0;
                    for (String id : documents.keySet()) {
                        d = documents.get(id);
                        d.setMood(new Document.Mood(conditional.get(pos_doc), imperative.get(pos_doc), subjunctive.get(pos_doc), indicatives.get(pos_doc)));
                        d.setMin_modality(Double.valueOf(min_modality.get(pos_doc)));
                        d.setMax_modality(Double.valueOf(max_modality.get(pos_doc)));
                        d.setPoliteness(Double.valueOf(politeness.get(pos_doc)));
                        d.setImpoliteness(Double.valueOf(impoliteness.get(pos_doc)));
                        pos_doc++;
                    }
                    WriterCSV writerCSV = new WriterCSV();
                    pos_doc = 0;
                    for (String id : documents.keySet()) {
                        d = documents.get(id);
                        d.setLabel(emotion.get(pos_doc));
                        pos_doc++;
                    }


                    String nameOutput= path+"/features-"+emotionIndicate+".csv";
                    writerCSV.writeCsvFile(nameOutput, documents);




                    }
                 else {
                    //politeness
                    Politeness pt = new Politeness();
                    pr.writeDocsValuesOnFile(pt.createFormatForInput(path+"/ElaboratedFiles/onlyText_PreProcessed.txt"), path+"/ElaboratedFiles/docs.py");
                }
            } catch (CsvColumnNotFound | FileNotFoundException | InterruptedException e) {
                System.err.println(e.getMessage());
            }
        } else
            System.err.println("Wrong params!!  You have to read the Readme file for check what are the parameters needed.");
    }
}


