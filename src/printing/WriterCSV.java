package printing;

import OrtuValidation.model.DatasetRowOrtu;
import OrtuValidation.model.DocumentOrtu;
import OrtuValidation.model.EmotionOrtu;
import model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

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

    public void writeCsvFile(String outputName,Map<Integer, Document> documents,int numUnigrams,int numBigrams,Set<String> pos,Set<String> neg ,Set<String> neutr,Set<String> amb) throws IOException {
        List<DatasetRowTFIDF> list = new ArrayList<>();


        Document d= null;
        Map<String,Double> unigramsTFIDF=null;
        Map<String,Double> bigramsTFIDF=null;
        Map<String,Double> positivesTFIDF=null;
        Map<String,Double> negativesTFIDF=null;
        Map<String,Double> neutralsTFIDF=null;
        Map<String,Double> ambiguosTFIDF=null;
        double pos_score;
        double neg_score;
        double polite;
        double impolite;
        String label="";
        DatasetRowTFIDF dr;
        for(Integer id: documents.keySet()){
            d=documents.get(id);
            unigramsTFIDF= d.getUnigramTFIDF();
            bigramsTFIDF=d.getBigramTFIDF();
            positivesTFIDF=d.getPositiveTFIDF();
            negativesTFIDF=d.getNegativeTFIDF();
            neutralsTFIDF=d.getNeutralTFIDF();
            ambiguosTFIDF= d.getAmbiguosTFIDF();
            pos_score=d.getPos_score();
            neg_score=d.getNeg_score();
            polite=d.getPoliteness();
            impolite=d.getImpoliteness();
            label=d.getLabel();

            List<Double> tf_idf= new ArrayList<>();



            //aggiungo la riga degli unigrammi
            for(int i=0;i<numUnigrams;i++){
                if(unigramsTFIDF.keySet().contains(String.valueOf(i))){
                    tf_idf.add(unigramsTFIDF.get(String.valueOf(i)));
                }
                else
                    tf_idf.add(0.0);
            }
            //aggiungo la riga dei bigrammi

            for(int i=0;i<numBigrams;i++){
                if(bigramsTFIDF.keySet().contains(String.valueOf(i))){
                    tf_idf.add(bigramsTFIDF.get(String.valueOf(i)));
                }
                else
                    tf_idf.add(0.0);
            }
            //aggiungo la riga dei positivi
//love joy ecc..
            for(String s:pos){
                if(positivesTFIDF.keySet().contains(s)){
                    tf_idf.add(positivesTFIDF.get(s));
                }
                else
                    tf_idf.add(0.0);
            }

            //aggiungo la riga dei negativi
            for(String s:neg){
                if(negativesTFIDF.keySet().contains(s)){
                    tf_idf.add(negativesTFIDF.get(s));
                }
                else
                    tf_idf.add(0.0);
            }

            //aggiungo la riga dei neutri
            for(String s: neutr){
                if(neutralsTFIDF.keySet().contains(s)){
                    tf_idf.add(neutralsTFIDF.get(s));
                }
                else
                    tf_idf.add(0.0);
            }
            //aggiungo la riga degli ambigui

            for(String s:amb){
                if(ambiguosTFIDF.keySet().contains(s)){
                    tf_idf.add(ambiguosTFIDF.get(s));
                }
                else
                    tf_idf.add(0.0);
            }

            dr = new DatasetRowTFIDF.DatasetRowBuilder()
                    .setDocument("t"+ id)
                    .setPosScore(pos_score)
                    .setNegScore(neg_score)
                    .setPoliteness(polite)
                    .setImpoliteness(impolite)
                    .setTf_idf(tf_idf)
                    .setAffectiveLabel(label)
                    .build();
            list.add(dr);
        }


        //INTESTO LE COLONNE
        List<String> header = new ArrayList<>();
       // header.add("id");
        header.add("id");
        header.add("pos_score");
        header.add("neg_score");
        header.add("polite");
        header.add("impolite");

        populateHeader(unigramsTFIDF,header,"uni");
        populateHeader(bigramsTFIDF,header,"bi");
        populateHeader(positivesTFIDF,header,"");
        populateHeader(negativesTFIDF,header,"");
        populateHeader(neutralsTFIDF,header,"");
        populateHeader(ambiguosTFIDF,header,"");

        header.add("label");

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
            for (DatasetRowTFIDF dx: list) {
                if ((j % 50) == 0) {
                    System.out.println("Printing line:" + j);
                }
                List l = new ArrayList();
              //  l.add(d.getId());
                l.add(dx.getDocument());
                l.add(dx.getPos_score());
                l.add(dx.getNeg_score());
                l.add(dx.getPoliteness());
                l.add(dx.getImpoliteness());
                //ATTENTO AD ADDALL !
                l.addAll(dx.getTf_idf());
                l.add(dx.getAffective_label());

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
