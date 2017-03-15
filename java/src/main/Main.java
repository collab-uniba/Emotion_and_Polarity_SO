package main;


import classification.Baseline;
import exceptions.CsvColumnNotFound;
import analysis.Politeness;
import analysis.SentiStrengthSentiment;
import computing.Grams;
import computing.TermFrequency_InverseDocumentFrequency;
import exceptions.WrongParamException;
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
    public static void main(String[] args) {

        Grams gr = new Grams();
        Removing rem = new Removing();
        Utility u = new Utility();
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf = new ReadingFile();

        int numArgs = args.length;

        //Take the args values
        String Tasktype = "";
        String emotionIndicate = "";
        String input = "";
        String politeFile = "";
        String executionMode = "";
        String moodFile = "";
        Character delimiter = ' ';
        boolean extractDictionary = false;
        boolean hasLabel = false;
        String testingCSV = "";

        try {
            //take the args
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-i": {
                        i++;
                        input = args[i];
                        break;
                    }
                    case "-T": {
                        i++;
                        testingCSV = args[i];
                        break;
                    }
                    case "-P": {
                        i++;
                        politeFile = args[i];
                        break;
                    }
                    case "-M": {
                        i++;
                        moodFile = args[i];
                        break;
                    }
                    case "-G": {
                        extractDictionary = true;
                        break;
                    }
                    case "-d": {
                        i++;
                        delimiter = args[i].charAt(0);
                        break;
                    }
                    case "-t": {
                        i++;
                        Tasktype = args[i];
                        if (Tasktype.equals("classification")) {
                            extractDictionary = false;
                        } else if (Tasktype.equals("training")) {
                            hasLabel = true;
                        }
                        break;
                    }
                    case "-L": {
                        hasLabel = true;
                        break;
                    }
                    case "-Ex": {
                        i++;
                        executionMode = args[i];
                        break;
                    }
                    case "-e": {
                        i++;
                        emotionIndicate = args[i];
                        break;
                    }
                    default: {
                        throw new WrongParamException("Wrong params!! " + args[i] + " Not allowed");
                    }
                }
            }

            //Create the output folder
            String[] ss = input.split("/");
            String fileCsv = ss[ss.length - 1].replaceAll(".csv", "");
            String path = Tasktype + "_" + fileCsv+ "_" + emotionIndicate;
            u.directoryCreator(path);
            u.directoryCreator(path + "/ElaboratedFiles");


            List<String> inputCorpus = null;
            try {
                //PRE-PROCESSING, tokenizing, urlRemoving
                //Tokenizzatore
                inputCorpus = rd.read_Column_CSV(input, "text", delimiter);
                pr.print(path + "/ElaboratedFiles/onlyText.txt", inputCorpus);


                TokenizeCorpus tk = new TokenizeCorpus();
                tk.tokenizerByToken(path + "/ElaboratedFiles/onlyText.txt", path + "/ElaboratedFiles/onlyText_TOKENIZED.txt");
                List<String> inputCorpusTknz = rdf.read(path + "/ElaboratedFiles/onlyText_TOKENIZED.txt");


                //Remove URL,usermention and special string
                List<String> docsWithoutURLtknz = rem.removeUrlOne(inputCorpusTknz);
                //remove user mention

                List<String> docsWithoutURLUserMentionTknz = rem.removeUserMention(docsWithoutURLtknz);

                //Remove special String
                List<String> docsPrePreProcessed = rem.escaping(docsWithoutURLUserMentionTknz);
                pr.print(path + "/ElaboratedFiles/onlyText_PreProcessed.txt", docsPrePreProcessed);



                if (executionMode.equals("baseline")) {
                    Baseline b = new Baseline();
                    Map<String, Document> documents = new LinkedHashMap<>();
                    List<String> ids = rd.read_Column_CSV(input, "id", ';');
                    List<String> labelsTest = rd.read_Column_CSV(testingCSV, "label", ',');
                    List<String> idsTest = rd.read_Column_CSV(testingCSV, "id", ',');



                    int pos_doc = 0;
                    for (String i : ids) {
                        Document d = new Document();
                        d.setText(docsPrePreProcessed.get(pos_doc));
                        d.setId(i);
                        documents.put(i, d);
                        pos_doc++;
                    }
                    pos_doc = 0;
                    for (String i : ids) {
                        if(!idsTest.contains(i))
                             documents.remove(i);
                        else {
                            documents.get(i).setLabel(labelsTest.get(pos_doc));
                            pos_doc++;
                        }
                    }
                    b.baselineLexicalBased(documents,emotionIndicate,path+"/");

                } else {
                    if (Tasktype.equals("classification") || Tasktype.equals("training")) {
                        //create map with <id , document>
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

                        SortedMap<String, String> unigrams = null;
                        SortedMap<String, String> bigrams = null;
                        //extracting bigram or unigram lists
                        if (extractDictionary) {
                            gr.getPositionWordMap(new File(path + "/ElaboratedFiles/onlyText_PreProcessed.txt"), path, 0, 1);
                            gr.getPositionWordMap(new File(path + "/ElaboratedFiles/onlyText_PreProcessed.txt"), path, 0, 2);

                            if (executionMode.equals("unigrams_1"))
                                unigrams = gr.importNgrams(path + "/n-grams/UnigramsList_1.txt", 1);
                            else if (executionMode.equals("unigrams_2"))
                                unigrams = gr.importNgrams(path + "/n-grams/UnigramsList_2.txt", 1);
                            else if (executionMode.equals("bigrams_1"))
                                bigrams = gr.importNgrams(path + "/n-grams/BigramsList_1.txt", 2);
                            else if (executionMode.equals("bigrams_2"))
                                bigrams = gr.importNgrams(path + "/n-grams/BigramsList_2.txt", 2);
                        } else {
                            if (executionMode.equals("unigrams_1"))
                                unigrams = gr.importNgrams(path + "/n-grams/UnigramsList_1.txt", 1);
                            else if (executionMode.equals("unigrams_2"))
                                unigrams = gr.importNgrams(path + "/n-grams/UnigramsList_2.txt", 1);
                            else if (executionMode.equals("bigrams_1"))
                                bigrams = gr.importNgrams(path + "/n-grams/BigramsList_1.txt", 2);
                            else if (executionMode.equals("bigrams_2"))
                                bigrams = gr.importNgrams(path + "/n-grams/BigramsList_2.txt", 2);
                        }


                        //END PREPROCESSING

                        //First csv file
                        //*****tf-idf****/
                        TermFrequency_InverseDocumentFrequency cl = new TermFrequency_InverseDocumentFrequency();
                        if (executionMode.equals("SenPolImpolMoodModality")) {
                            SentiStrengthSentiment st = new SentiStrengthSentiment();
                            st.SentiStrengthgetScoreForAllDocs(documents, 0);
                            System.out.println("Computing positive score..");
                            st.SentiStrengthgetScoreForAllDocs(documents, 1);
                            System.out.println("Computing negative score...");

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

                            String nameOutput = path + "/features-" + executionMode + ".csv";
                            writerCSV.writeCsvFile(nameOutput, documents, hasLabel, executionMode);
                        } else if (executionMode.equals("unigrams_1")) {
                            //*****tf-idf*
                            cl.tf_idf(documents, unigrams, 1, "unigrams_1", path, Tasktype);
                            WriterCSV writerCSV = new WriterCSV();
                            String nameOutput = path + "/features-" + "unigrams_1" + ".csv";
                            writerCSV.writeCsvFile(nameOutput, documents, hasLabel, executionMode);
                        } else if (executionMode.equals("bigrams_1")) {
                            cl.tf_idf(documents, bigrams, 2, "bigrams_1", path, Tasktype);

                            WriterCSV writerCSV = new WriterCSV();
                            String nameOutput = path + "/features-" + "bigrams_1" + ".csv";
                            writerCSV.writeCsvFile(nameOutput, documents, hasLabel, executionMode);
                        } else if (executionMode.equals("unigrams_2")) {
                            //*****tf-idf*
                            cl.tf_idf(documents, unigrams, 1, "unigrams_2", path, Tasktype);

                            WriterCSV writerCSV = new WriterCSV();
                            String nameOutput = path + "/features-" + "unigrams_2" + ".csv";
                            writerCSV.writeCsvFile(nameOutput, documents, hasLabel, executionMode);
                        } else if (executionMode.equals("bigrams_2")) {
                            cl.tf_idf(documents, bigrams, 2, "bigrams_2", path, Tasktype);
                            WriterCSV writerCSV = new WriterCSV();
                            String nameOutput = path + "/features-" + "bigrams_2" + ".csv";
                            writerCSV.writeCsvFile(nameOutput, documents, hasLabel, executionMode);
                        } else if (executionMode.equals("wordnet")) {
                            ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();

                            Map<String, List<String>> pos = rd.read_AllColumn_CSV("java/res/WordnetCategories/positive_emotion.csv", ';');
                            Map<String, List<String>> neg = rd.read_AllColumn_CSV("java/res/WordnetCategories/negative_emotion.csv", ';');
                            Map<String, List<String>> neu = rd.read_AllColumn_CSV("java/res/WordnetCategories/neutral_emotion.csv", ';');
                            Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("java/res/WordnetCategories/ambiguos-emotion.csv", ';');


                            replacer.replaceTermsWithMarks(documents, path);

                            cl.tf_idf(documents, u.createMap(pos), 1, "positives", path, Tasktype);
                            cl.tf_idf(documents, u.createMap(neg), 1, "negatives", path, Tasktype);
                            cl.tf_idf(documents, u.createMap(neu), 1, "neutrals", path, Tasktype);
                            cl.tf_idf(documents, u.createMap(ambiguos), 1, "ambiguos", path, Tasktype);


                            //1 dataset per volta.
                            List<String> emotion = null;
                            if (hasLabel) {
                                System.out.println("Reading emotions...");
                                emotion = rd.read_Column_CSV(input, "label", delimiter);
                                System.out.println("Emotion readed successfully!!");
                            }

                            Document d = null;
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                if (hasLabel)
                                    d.setLabel(emotion.get(pos_doc));
                                pos_doc++;
                            }
                            WriterCSV writerCSV = new WriterCSV();

                            String nameOutput = path + "/features-" + "wordnet" + ".csv";
                            writerCSV.writeCsvFile(nameOutput, documents, hasLabel, executionMode);
                        } else if (executionMode.equals("createDocFormat")) {
                            //politeness
                            Politeness pt = new Politeness();
                            pr.writeDocsValuesOnFile(pt.createFormatForInput(path + "/ElaboratedFiles/onlyText_PreProcessed.txt"), path + "/ElaboratedFiles/docs.py");
                        }


                    } else
                        System.err.println("Task type wrong!  Chose in : classification or training");
                }
            } catch(CsvColumnNotFound | InterruptedException | IOException e){
                System.err.println(e.getMessage());
            }
        } catch (WrongParamException e) {
            System.err.println(e.getMessage());
        }
    }
}


