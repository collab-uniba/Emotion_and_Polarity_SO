package main;


import exceptions.CsvColumnNotFound;
import analysis.Politeness;
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

/*
If you want to create the file for calculate the politeness those are the params:
    inputCorpus.csv -P delimiter
    specifing :
    inputCorpus.csv : in UTF-8 WITHOUT BOM , use notepad ++ , go in format and chose it.
     delimiter : ";" or ","

Else you have to give those params :
*  `file.csv`: the input file coded in **UTF-8 without BOM**, containing the corpus for the training; the format of the input file is specified [here](https://github.com/collab-uniba/Emotion_and_Polarity_SO/wiki/File-format-for-training-corpus).
* `delimiter`: the delimiter `;` or `,` used in the csv file
* `-G`: extract bigrams and unigrams (mandatory on the first run; extraction can be skipped afterwards for the same input file); dictionaries will be stored in `./<file.csv>/dictionary/unigrams.txt` and `./dictionary/<file.csv>/bigrams.txt`)
* `-e emotion`: the specific emotion for training the model, defined in {`joy`, `anger`, `sadness`, `love`, `surprise`, `fear`}

 To create textsPoliteAndImpolite.csv you have to run this jar with "inputCorpus.csv -P" to generate the file,alias docs.py, given as input from CalculatePoliteness.model.py To create textsMoodAndModality.csv you have to run this jar with "inputCorpus.csv -P" to generate the file,alias docs.py,given as input from CalculateMoodAndModality.moodAndModality.py

To give docs.py in input to those python files you have to put those files into same directory where the python (model.py or moodAndModality.py) are.
 */

public class Main {
    public static void main(String[] args) throws IOException {

        Grams gr = new Grams();
        Removing rem = new Removing();
        Utility u = new Utility();
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf = new ReadingFile();


        if (args.length == 3 || args.length == 5 || args.length == 6 ) {
            System.out.println("Creating output directory..");
            String[] ss = args[0].split("/");
            String emotionName= ss[ss.length-1].replaceAll(".csv","");
            String path="Output_"+emotionName;
            u.directoryCreator(path);
            u.directoryCreator(path+"/ElaboratedFiles");

            String p5 = null;
            String p6 = null;
            if (args.length == 5) {
                p5 = args[4];
            }
            if (args.length == 6) {
                p5 = args[4];
                p6 = args[5];
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
                    l = 3;

                inputCorpus = rd.read_Column_CSV(args[0], "text", args[l].charAt(0));
                pr.print(path+"/ElaboratedFiles/onlyText.txt", inputCorpus);


                TokenizeCorpus tk = new TokenizeCorpus();
                tk.tokenizerByToken(path+"/ElaboratedFiles/onlyText.txt", path+"/ElaboratedFiles/onlyText_TOKENIZED.txt");
                List<String> inputCorpusTknz = rdf.read(path+"/ElaboratedFiles/onlyText_TOKENIZED.txt");


                //Remove URL,usermention and special string
                List<String> docsWithoutURLtknz = rem.removeUrlOne(inputCorpusTknz);
                //remove user mention

                List<String> docsWithoutURLUserMentionTknz = rem.removeUserMention(docsWithoutURLtknz);

                //Remove special String
                List<String> docsWithotSpCUrlUsMtTknz = rem.escaping(docsWithoutURLUserMentionTknz);
                pr.print(path+"/ElaboratedFiles/onlyText_PreProcessed.txt", docsWithotSpCUrlUsMtTknz);

                if (!args[1].equals("-P")) {


                    SortedMap<String, String> unigrams = null;
                    SortedMap<String, String> bigrams = null;
                    //extracting bigram or unigram lists
                    if ((p5 != null && p5.equals("-G")) || (p6 != null && p6.equals("-G"))) {
                        System.out.println("Extracting unigrams..");
                        unigrams = gr.getPositionWordMap(new File(path+"/ElaboratedFiles/onlyText_PreProcessed.txt"),path,0, 1);
                        System.out.println("Unigrams Extracted successfully!");

                        System.out.println("Extracting bigrams...");
                        bigrams = gr.getPositionWordMap(new File(path+"/ElaboratedFiles/onlyText_PreProcessed.txt"),path, 0, 2);
                        System.out.println("Bigrams Extracted successfully!");
                    } else {
                        unigrams = gr.importNgrams(path+"/Dictionary/UnigramsList.txt");
                        System.out.println("unigrams loaded");
                        bigrams = gr.importNgrams(path+"/Dictionary/BigramsList.txt");
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
                    cl.tf_idf(documents, unigrams, 1, "unigrams",path);
                    System.out.println("Tf-idf for unigrams , computed");
                   // Ids ids_bigrams =
                    cl.tf_idf(documents, bigrams, 2, "bigrams",path);
                    System.out.println("Tf-idf for bigrams , computed");


                    ReplacerTextWithMarks replacer = new ReplacerTextWithMarks();


                    Map<String, List<String>> pos = rd.read_AllColumn_CSV("./resources/WordnetCategories/positive_emotion.csv", ';');
                    Map<String, List<String>> neg = rd.read_AllColumn_CSV("./resources/WordnetCategories/negative_emotion.csv", ';');
                    Map<String, List<String>> neu = rd.read_AllColumn_CSV("./resources/WordnetCategories/neutral_emotion.csv", ';');
                    Map<String, List<String>> ambiguos = rd.read_AllColumn_CSV("./resources/WordnetCategories/ambiguos-emotion.csv", ';');


                    List<String> paths = new ArrayList<>();
                    paths.add("./resources/WordnetCategories/neutral_emotion.csv");
                    paths.add("./resources/WordnetCategories/ambiguos-emotion.csv");
                    paths.add("./resources/WordnetCategories/positive_emotion.csv");
                    paths.add("./resources/WordnetCategories/negative_emotion.csv");

                    replacer.replaceTermsWithMarks(documents, paths);


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

                    //1 dataset per volta.
                    List<String> emotion=null;


                    emotion = rd.read_Column_CSV( args[0], "label", args[3].charAt(0));


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
                    String nameOutput= path+"/features-"+emotionName+".csv";
                    writerCSV.writeCsvFile(nameOutput, documents);

                    }
                 else {
                    //politeness
                    Politeness pt = new Politeness();
                    pr.writeDocsValuesOnFile(pt.createFormatForInput(path+"/ElaboratedFiles/onlyText_PreProcessed.txt"), path+"/ElaboratedFiles/docs.py");
                }
            } catch (CsvColumnNotFound csvColumnNotFound) {
                System.err.println(csvColumnNotFound.getMessage());
            }
        } else
            System.err.println("Wrong params!!  You have to read the Readme file for check what are the parameters needed.");

    }
}


