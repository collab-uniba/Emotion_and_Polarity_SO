package printing;

import model.CsvElements;
import model.DatasetRow;
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
    private List<DatasetRow> list = null;
    private Utility u = new Utility();

    public void writeCsvFile(String outputName, CsvElements model) throws IOException {
        list = new ArrayList<>();
        List<String> ids = model.getId();
        List<String> docs = model.getDocuments();
        List<LinkedHashMap<String, Double>> unigrams = model.getUnigramTFIDF();
        List<LinkedHashMap<String, Double>> bigrams = model.getBigramTFIDF();
        List<LinkedHashMap<String, Double>> positives = model.getPositiveTFIDF();
        List<LinkedHashMap<String, Double>> negatives = model.getNegativeTFIDF();
        List<LinkedHashMap<String, Double>> neutrals = model.getNeutralTFIDF();
        List<LinkedHashMap<String, Double>> ambiguos = model.getAmbiguosTFIDF();

        int i = 0;


        DatasetRow dr;
//        for (String id : ids) {
        for (String doc : docs) {
            List<Double> tf_idf= new ArrayList<>();
            LinkedHashMap<String, Double> unigramsTFIDF = unigrams.get(i);
            LinkedHashMap<String, Double> bigramsTFIDF = bigrams.get(i);
            LinkedHashMap<String, Double> positivesTFIDF = positives.get(i);
            LinkedHashMap<String, Double> negativessTFIDF = negatives.get(i);
            LinkedHashMap<String, Double> neutralsTFIDF = neutrals.get(i);
            LinkedHashMap<String, Double> ambiguosTFIDF = ambiguos.get(i);


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


            dr = new DatasetRow.DatasetRowBuilder()
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
            for (DatasetRow d : list) {
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

    private void populateHeader(LinkedHashMap<String, Double> grams,List<String> header,String head) {
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

}
