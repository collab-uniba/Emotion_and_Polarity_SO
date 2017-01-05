package printing;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import utility.Utility;

/**
 * Questa classe, usando la libreria di Apache : commons , crea un file CSV.
 */
public class WriterCSV {

    // Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    private Utility u = new Utility();

    public void writeCsvFile(String outputName, CsvElementsTFIDF model) throws IOException {
        List<DatasetRowTFIDF> list = new ArrayList<>();
        List<String> ids = model.getId();
        List<String> docs = model.getDocuments();
        List<Map<String, Double>> unigrams = model.getUnigramTFIDF();
        List<Map<String, Double>> bigrams = model.getBigramTFIDF();
        List<Map<String, Double>> positives = model.getPositiveTFIDF();
        List<Map<String, Double>> negatives = model.getNegativeTFIDF();
        List<Map<String, Double>> neutrals = model.getNeutralTFIDF();
        List<Map<String, Double>> ambiguos = model.getAmbiguosTFIDF();

        int i = 0;


        DatasetRowTFIDF dr;
//        for (String id : ids) {
        for (String doc : docs) {
            List<Double> tf_idf= new ArrayList<>();
           Map<String, Double> unigramsTFIDF = unigrams.get(i);
            Map<String, Double> bigramsTFIDF = bigrams.get(i);
            Map<String, Double> positivesTFIDF = positives.get(i);
            Map<String, Double> negativessTFIDF = negatives.get(i);
            Map<String, Double> neutralsTFIDF = neutrals.get(i);
            Map<String, Double> ambiguosTFIDF = ambiguos.get(i);


            //aggiungo la riga degli unigrammi
            for (String s : unigramsTFIDF.keySet()) {
                tf_idf.add(unigramsTFIDF.get(s));
            }
            //aggiungo la riga dei brigrammi
            for (String s : bigramsTFIDF.keySet()) {
                tf_idf.add(bigramsTFIDF.get(s));
            }
            //aggiungo la riga dei positivi
            for (String s : positivesTFIDF.keySet()) {
                tf_idf.add(positivesTFIDF.get(s));
            }
            //aggiungo la riga dei negativi
            for (String s : negativessTFIDF.keySet()) {
                tf_idf.add(negativessTFIDF.get(s));
            }
            //aggiungo la riga dei neutri
            for (String s : neutralsTFIDF.keySet()) {
                tf_idf.add(neutralsTFIDF.get(s));
            }
            //aggiungo la riga degli ambigui
            for (String s : ambiguosTFIDF.keySet()) {
                tf_idf.add(ambiguosTFIDF.get(s));
            }


            dr = new DatasetRowTFIDF.DatasetRowBuilder()
                    .setDocument("t"+ i)
                    .setTf_idf(tf_idf)
                    .build();
            list.add(dr);
            i++;
    }

        //INTESTO LE COLONNE
        List<String> header = new ArrayList<>();
       // header.add("id");
        header.add("id");

        populateHeader(unigrams.get(0),header,"uni");
        populateHeader(bigrams.get(0),header,"bi");
        populateHeader(positives.get(0),header,"");
        populateHeader(negatives.get(0),header,"");
        populateHeader(neutrals.get(0),header,"");
        populateHeader(ambiguos.get(0),header,"");

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        // Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter(',');
        try {
            // initialize FileWriter object
            fileWriter = new FileWriter(outputName);
            // initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            //qui mette le colonne
            csvFilePrinter.printRecord(header);
            int j = 0;
            for (DatasetRowTFIDF d : list) {
                if ((j % 50) == 0) {
                    System.out.println("Printing line:" + j);
                }
                List l = new ArrayList();
              //  l.add(d.getId());
                l.add(d.getDocument());
                //ATTENTO AD ADDALL !
                l.addAll(d.getTf_idf());

                csvFilePrinter.printRecord(l);
                j++;
            }
            System.out.println("CSV file " + outputName + " was created successfully.");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }

    /**this method populate header for tf-idf*/
    private void populateHeader(Map<String, Double> grams,List<String> header,String head) {
        int ii = 1;
        for (String k : grams.keySet()) {
            if(head.isEmpty()){
                header.add(k);
            }
            else
                 header.add(head + ii);
            ii++;
        }
    }


    /**
     * this method is used for ortu validation
     * @param outputName
     * @param documents
     * @param column
     * @param withHeader
     * @param delimiter
     * @param withoutMixed
     */
    private List<DatasetRowOrtu> listOrtu=null;
    public void writeCsvFileTwo(String outputName, List<DocumentOrtu> documentOrtus, String[] column, boolean withHeader, Character delimiter, boolean withoutMixed){
        listOrtu=new ArrayList<>();
        if(withoutMixed) {
            for (DocumentOrtu d : documentOrtus) {
                if (d.getFinaLabel()!=null && !d.getFinaLabel().equals("mixed"))
                    build_Row(d,column);
            }
        }
        else{
            for (DocumentOrtu d : documentOrtus) {
                build_Row(d,column);
            }
        }
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        // Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter(delimiter);

        try {

            // initialize FileWriter object
            fileWriter = new FileWriter(outputName);
            // initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            // Create CSV file header
            if(withHeader)
                csvFilePrinter.printRecord(column);
            int i = 0;
            for (DatasetRowOrtu d : listOrtu) {
                if ((i % 50) == 0) {
                    System.out.println("Printing line:" + i);
                }
                List l = new ArrayList();
                for(String s : column){
                    switch(s){
                        case "id":
                            l.add(d.getId());
                            continue;
                        case "love":
                            l.add(d.getLove());
                            continue;
                        case "joy":
                            l.add(d.getJoy());
                            continue;
                        case "surprise":
                            l.add(d.getSurprise());
                            continue;
                        case "anger":
                            l.add(d.getAnger());
                            continue;
                        case "sadness":
                            l.add(d.getSadness());
                            continue;
                        case "fear":
                            l.add(d.getFear());
                            continue;
                        case "label":
                            l.add(d.getLabel());
                            continue;
                        case "comment":
                            l.add(d.getComment());
                    }
                }

                csvFilePrinter.printRecord(l);
                i++;
            }
            System.out.println("CSV file " + outputName +" was created successfully.");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }

    private void build_Row(DocumentOrtu d, String[] column){
        String id="";
        String love="";
        String joy="";
        String surprise="";
        String anger="";
        String sadness="";
        String fear="";
        String label="";
        String comment="";
        for(String s : column){
            switch(s){
                case "id":
                    id="yes";
                    continue;
                case "love":
                    love="yes";
                    continue;
                case "joy":
                    joy="yes";
                    continue;
                case "surprise":
                    surprise="yes";
                    continue;
                case "anger":
                    anger="yes";
                    continue;
                case "sadness":
                    sadness="yes";
                    continue;
                case "fear":
                    fear ="yes";
                    continue;
                case "label":
                    label="yes";
                    continue;
                case "comment":
                    comment="yes";
            }
        }
        DatasetRowOrtu dr=null;
        if(!id.equals("") && !label.equals("") && !comment.equals("") && !fear.equals("") && !sadness.equals("") && !surprise.equals("") && !joy.equals("") && !love.equals("") && !anger.equals("")){
            //caso tutti
            EmotionOrtu emo = d.getEmotionOrtu();
            dr = new DatasetRowOrtu.DatasetRowBuilder()
                    .setId(d.getId())
                    .setLove(emo.getLove())
                    .setJoy(emo.getJoy())
                    .setSurprise(emo.getSurprise())
                    .setAnger(emo.getAnger())
                    .setSadness(emo.getSadness())
                    .setFear(emo.getFear())
                    .setLabel(d.getFinaLabel())
                    .setComment(d.getComment())
                    .build();}
        else if(!label.equals("") && !comment.equals("") && !id.equals(""))
            dr = new DatasetRowOrtu.DatasetRowBuilder().setIdCommentLabel(d.getId(),d.getComment(),d.getFinaLabel()).build();
        else if(!label.equals("") && !comment.equals("")){
            dr = new DatasetRowOrtu.DatasetRowBuilder().setCommentLabel(d.getComment(),d.getFinaLabel()).build();
        }
        else if(!comment.equals("")){
            dr = new DatasetRowOrtu.DatasetRowBuilder().setComment(d.getComment()).build();
        }
        listOrtu.add(dr);
    }




}
