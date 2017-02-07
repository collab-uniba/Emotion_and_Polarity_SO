package main;


import Exceptions.CsvColumnNotFound;
import analysis.Politeness;
import analysis.SentiStrengthSentiment;
import computing.Grams;
import computing.Ids;
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



public class Main {
    public static void main(String[] args) throws IOException {

        Grams gr = new Grams();
        Removing rem = new Removing();
        Utility u = new Utility();
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf = new ReadingFile();


        if (args.length == 3 || args.length == 5 || args.length == 6 || args.length == 7) {
            String p6 = null;
            String p7 = null;
            if (args.length == 6) {
                p6 = args[5];
            }
            if (args.length == 7) {
                p6 = args[5];
                p7 = args[6];
            }

            //PRE-PROCESSING, tokenizing, urlRemoving
            //Tokenizzatore


            List<String> inputCorpus = null;
            try {
                int l = 0;
                //se calcolo la politeness , trovo il delimitatore alla poszione numero 2
                if (args[1].equals("-P")) {
                    l = 2;
                } else
                    l = 4;

                inputCorpus = rd.read_Column_CSV(args[0], "text", args[l].charAt(0));
                pr.print("res/onlyText.txt", inputCorpus);


                TokenizeCorpus tk = new TokenizeCorpus();
                tk.tokenizerByToken("res/onlyText.txt", "res/onlyText_TOKENIZED.txt");
                List<String> inputCorpusTknz = rdf.read("res/onlyText_TOKENIZED.txt");


                //Remove URL,usermention and special string
                List<String> docsWithoutURLtknz = rem.removeUrlOne(inputCorpusTknz);
                // pr.print("res/docsWithoutURLTknz",docsWithoutURLtknz);
                //remove user mention

                List<String> docsWithoutURLUserMentionTknz = rem.removeUserMention(docsWithoutURLtknz);
                // pr.print("res/docsWithoutURLUserMention",docsWithoutURLUserMentionTknz);


                //Remove special String
                List<String> docsWithotSpCUrlUsMtTknz = rem.escaping(docsWithoutURLUserMentionTknz);
                pr.print("res/docsWithoutURLUsMentSpCharTknz.txt", docsWithotSpCUrlUsMtTknz);

//, "type=" + "\"submit\"" + ">"
                if (!args[1].equals("-P")) {
                    SortedMap<String, String> unigrams = null;
                    SortedMap<String, String> bigrams = null;
                    //extracting bigram or unigram lists
                    if ((p6 != null && p6.equals("-G")) || (p7 != null && p7.equals("-G"))) {
                        System.out.println("Extracting unigrams..");
                        unigrams = gr.getPositionWordMap(new File("res/docsWithoutURLUsMentSpCharTknz.txt"), 0, 1);
                        System.out.println("Unigrams Extracted successfully!");

                        System.out.println("Extracting bigrams...");
                        bigrams = gr.getPositionWordMap(new File("res/docsWithoutURLUsMentSpCharTknz.txt"), 0, 2);
                        System.out.println("Bigrams Extracted successfully!");
                    } else {
                        unigrams = gr.importNgrams("res/Grams/UnigramsList.txt");
                        System.out.println("unigrams loaded");
                        bigrams = gr.importNgrams("res/Grams/BigramsList.txt");
                        System.out.println("bigrams loaded");
                    }

                    System.out.println("\n");

                    //Creo il map con indice del documento
                    Map<String, Document> documents = new LinkedHashMap<>();
                    List<String> ids = rd.read_Column_CSV(args[0], "id", args[3].charAt(0));
                    int pos_doc = 0;
                    for (String i : ids) {
                        Document d = new Document();
                        d.setText(docsWithotSpCUrlUsMtTknz.get(pos_doc));
                        d.setId(i);
                        documents.put(i, d);
                        pos_doc++;
                    }
                    System.out.println(documents.size());
                    //FINE PREPROCESSING


                    //*****tf-idf****/
                    TF_IDFComputer cl = new TF_IDFComputer();


                   // Ids ids_unigrams =
                    cl.tf_idf(documents, unigrams, 1, "unigrams");
                    System.out.println("Tf-idf for unigrams , computed");
                   // Ids ids_bigrams =
                    cl.tf_idf(documents, bigrams, 2, "bigrams");
                    System.out.println("Tf-idf for bigrams , computed");


                    ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();


                    Map<String, List<String>> pos = rd.read_AllColumn_CSV("res/WordnetCategories/positive_emotion.csv", ';');
                    Map<String, List<String>> neg = rd.read_AllColumn_CSV("res/WordnetCategories/negative_emotion.csv", ';');
                    Map<String, List<String>> neu = rd.read_AllColumn_CSV("res/WordnetCategories/neutral_emotion.csv", ';');
                    Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("res/WordnetCategories/ambiguos-emotion.csv", ';');


                    List<String> paths = new ArrayList<>();
                    paths.add("res/WordnetCategories/neutral_emotion.csv");
                    paths.add("res/WordnetCategories/ambiguos-emotion.csv");
                    paths.add("res/WordnetCategories/positive_emotion.csv");
                    paths.add("res/WordnetCategories/negative_emotion.csv");

                    replacer.replaceTermsWithMarks(documents, paths);

                   // Ids ids_positives =
                            cl.tf_idf(documents, u.createMap(pos), 1, "positives");
                    // Ids ids_negatives =
                             cl.tf_idf(documents, u.createMap(neg), 1, "negatives");
                    //Ids ids_neutrals =
                    cl.tf_idf(documents, u.createMap(neu), 1, "neutrals");
                    //Ids ids_ambiguos =
                            cl.tf_idf(documents, u.createMap(ambiguos), 1, "ambiguos");


                    SentiStrengthSentiment st = new SentiStrengthSentiment();
                    st.SentiStrengthgetScoreForAllDocs(documents, 0);
                    System.out.println("Reading positive score..");
                    st.SentiStrengthgetScoreForAllDocs(documents, 1);
                    System.out.println("Reading negative score...");

                    System.out.println("Reading politeness and impoliteness..");
                    List<String> politeness = rd.read_Column_CSV(args[1], "polite", ',');
                    List<String> impoliteness = rd.read_Column_CSV(args[1], "impolite", ',');

                    System.out.println("Reading modality..");
                    List<String> min_modality = rd.read_Column_CSV(args[2], "min_modality", ',');
                    List<String> max_modality = rd.read_Column_CSV(args[2], "max_modality", ',');

                    System.out.println("Reading moods..");

                    List<String> indicatives = rd.read_Column_CSV(args[2], "indicative", ',');
                    List<String> conditional = rd.read_Column_CSV(args[2], "conditional", ',');
                    List<String> subjunctive = rd.read_Column_CSV(args[2], "subjunctive", ',');
                    List<String> imperative = rd.read_Column_CSV(args[2], "imperative", ',');


                    System.out.println("Reading emotions...");
                    //costruisco il path in cui si trovano le emozioni in base al dominio che può essere Stack Overflow o quello di Ortu
                    String partial_path = "res/";
                    if (args[4].equals("-S")) {
                        partial_path += "StackOverflowCSV/";
                    } else
                        partial_path += "OrtuGroup3CSV/";

                    List<String> joy = null;
                    List<String> love = null;
                    List<String> sadness = null;
                    List<String> anger = null;
                    List<String> surprise = null;
                    List<String> fear = null;
                    String specific = "All";
                    if (p6 != null && !p6.equals("-G")) {
                        switch (p6) {
                            case "-J": {
                                specific = "J";
                                joy = rd.read_Column_CSV(partial_path + "joy.csv", "label", args[3].charAt(0));
                                break;
                            }
                            case "-L": {
                                specific = "L";
                                love = rd.read_Column_CSV(partial_path + "love.csv", "label", args[3].charAt(0));
                                break;
                            }
                            case "-S": {
                                specific = "S";
                                sadness = rd.read_Column_CSV(partial_path + "sadness.csv", "label", args[3].charAt(0));
                                break;
                            }
                            case "-A": {
                                specific = "A";
                                anger = rd.read_Column_CSV(partial_path + "anger.csv", "label", args[3].charAt(0));
                                break;
                            }
                            case "-Sp": {
                                specific = "Sp";
                                surprise = rd.read_Column_CSV(partial_path + "surprise.csv", "label", args[3].charAt(0));
                                break;
                            }
                            case "-F": {
                                specific = "F";
                                fear = rd.read_Column_CSV(partial_path + "fear.csv", "label", args[3].charAt(0));
                                break;
                            }
                            default: {
                                joy = rd.read_Column_CSV(partial_path + "joy.csv", "label", args[3].charAt(0));
                                love = rd.read_Column_CSV(partial_path + "love.csv", "label", args[3].charAt(0));
                                sadness = rd.read_Column_CSV(partial_path + "sadness.csv", "label", args[3].charAt(0));
                                anger = rd.read_Column_CSV(partial_path + "anger.csv", "label", args[3].charAt(0));
                                if (args[4].equals("-S")) {
                                    surprise = rd.read_Column_CSV(partial_path + "surprise.csv", "label", args[3].charAt(0));
                                    fear = rd.read_Column_CSV(partial_path + "fear.csv", "label", args[3].charAt(0));
                                }
                            }
                        }
                    } else {
                        joy = rd.read_Column_CSV(partial_path + "joy.csv", "label", args[3].charAt(0));
                        love = rd.read_Column_CSV(partial_path + "love.csv", "label", args[3].charAt(0));
                        sadness = rd.read_Column_CSV(partial_path + "sadness.csv", "label", args[3].charAt(0));
                        anger = rd.read_Column_CSV(partial_path + "anger.csv", "label", args[3].charAt(0));
                        //Se è per stack overflow allora ho 6 emozioni, altrimenti sono 4 per il dataset di ORtu gruppo
                        if (args[4].equals("-S")) {
                            surprise = rd.read_Column_CSV(partial_path + "surprise.csv", "label", args[3].charAt(0));
                            fear = rd.read_Column_CSV(partial_path + "fear.csv", "label", args[3].charAt(0));
                        }
                    }

                    System.out.println("Emotions readed...");

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

                    u.directoryCreator("outputEmotion");
                    WriterCSV writerCSV = new WriterCSV();

                    switch (specific) {
                        case "All": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(joy.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputJoy.csv", documents);

                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(sadness.get(pos_doc));
                                pos_doc++;
                            }

                            writerCSV.writeCsvFile("outputEmotion/OutputSadness.csv", documents);
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(anger.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputAnger.csv", documents);
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(love.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputLove.csv", documents);


                            if (args[4].equals("-S")) {
                                pos_doc = 0;
                                for (String id : documents.keySet()) {
                                    d = documents.get(id);
                                    d.setLabel(surprise.get(pos_doc));
                                    pos_doc++;
                                }
                                writerCSV.writeCsvFile("outputEmotion/OutputSurprise.csv", documents);
                                pos_doc = 0;
                                for (String id : documents.keySet()) {
                                    d = documents.get(id);
                                    d.setLabel(fear.get(pos_doc));
                                    pos_doc++;
                                }
                                writerCSV.writeCsvFile("outputEmotion/OutputFear.csv", documents);
                            }
                            break;
                        }
                        case "A": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(anger.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputAnger.csv", documents);
                            break;
                        }
                        case "J": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(joy.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputJoy.csv", documents);
                            break;
                        }
                        case "F": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(fear.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputFear.csv", documents);
                            break;
                        }
                        case "Sp": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(surprise.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputSurprise.csv", documents);
                            break;
                        }
                        case "L": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(love.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputLove.csv", documents);
                            break;
                        }
                        case "S": {
                            pos_doc = 0;
                            for (String id : documents.keySet()) {
                                d = documents.get(id);
                                d.setLabel(sadness.get(pos_doc));
                                pos_doc++;
                            }
                            writerCSV.writeCsvFile("outputEmotion/OutputSadness.csv", documents);
                            break;
                        }
                    }
                } else {
                    //politeness
                    Politeness pt = new Politeness();
                    u.directoryCreator("docsFormattedForPoliteness");
                    pr.writeDocsValuesOnFile(pt.createFormatForInput("res/docsWithoutURLUsMentSpCharTknz"), "docsFormattedForPoliteness/docs.py");
                }
            } catch (CsvColumnNotFound csvColumnNotFound) {
                System.err.println(csvColumnNotFound.getMessage());
            }
        } else
            System.err.println("Wrong params!!  You have to read the Readme file for check what are the parameters needed.");

    }
}


