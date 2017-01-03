package main;


import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {



   /* public static void main(String[] args) throws IOException {
       /* Grams gr = new Grams();

        //PRE-PROCESSING, tokenizing, urlRemoving, Post_Tagging
        //Tokenizzatore
        PrintingFile pr = new PrintingFile();
        ReadingCSV rd = new ReadingCSV();
        ReadingFile rdf= new ReadingFile();
        Map<String, List<String>> inputCorpus = rd.read_AllColumn_CSV("res/inputCorpus.csv");
        pr.print("res/onlyText", inputCorpus.get("comment"));


        TokenizeCorpus tk = new TokenizeCorpus();
        tk.tokenizerByToken("res/onlyText", "res/onlyText_TOKENIZED");
        List<String> inputCorpusTknz = rdf.read("res/onlyText_TOKENIZED");


        //Remove non-english words
        RemoveNotEnglishWords rmNotEnglishWords= new RemoveNotEnglishWords();
        List<String> paths = new ArrayList<>();
        paths.add("res/neutral_emotion.csv");
        paths.add("res/ambiguos-emotion.csv");
        paths.add("res/positive_emotion.csv");
        paths.add("res/negative_emotion.csv");

        List<String> docsWithoutNotEnglishWords= rmNotEnglishWords.removeNonEnglishWord(inputCorpusTknz,paths);
        pr.print("res/docsWithoutNotEnglishWords",docsWithoutNotEnglishWords);
*/

      /* Post tagger*/
       /* POSTagger pt = new POSTagger();
        List<String> docsPostTagged =pt.posTag(inputCorpusTknz);
        pr.print("res/docsPostTagged",docsPostTagged);*/

        //Remove URL
       /* RemoveURL rmurl= new RemoveURLOne();
        List<String> docsWithoutURLTknz= rmurl.removeUrlOne(docsPostTagged);
        pr.print("res/docsWithoutURLTokenized",docsWithoutURLTknz);*/




          /*extracting bigram or unigram lists*/
      /* SortedMap<String, Integer> unigram= gr.getPositionWordMap(new File("res/docsWithoutURLTokenized"), 0, 1);
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

        System.out.println("\n");*/

        //FINE PREPROCESSING


        //tf-idf-bigrams
     /*   TF_IDFComputer cl= new TF_IDFComputer();
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
        writerCSV.writeCsvFile("output.csv",csv);*/
    }
}

