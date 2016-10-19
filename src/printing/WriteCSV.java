package printing;

import models.Document;

import models.Document;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Questa classe, usando la libreria di Apache : commons , crea un file CSV.
 */
public class WriteCSV {

    // Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    public void writeCsvFile(String outputName,List<models.Document> group)  {
        List<DatasetRow> list = new ArrayList<>();
        List<String> header= new ArrayList<>();
        header.add("id");
        String[] HEADERS={"love","joy","surprise","anger","sadness","fear"};
        Collections.addAll(header,HEADERS);
        header.add("label");
        header.add("comment");

        for (Document d:group) {
            DatasetRow dr = new DatasetRow.DatasetRowBuilder()
                    .setId(d.getId())
                    .setLove(d.get_Love())
                    .setJoy(d.get_Joy())
                    .setSurprise(d.get_Surprise())
                    .setAnger(d.get_Anger())
                    .setSadness(d.get_Sadness())
                    .setFear(d.get_Fear())
                    .setLabel(d.getFinaLabel())
                    .setComment(d.getComment())
                    .build();
            list.add(dr);
        }

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        // Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter(',');

        try {

            // initialize FileWriter object
            fileWriter = new FileWriter(outputName);
            // initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            // Create CSV file header
            csvFilePrinter.printRecord(header);
            int i = 0;
            for (DatasetRow d : list) {
                if ((i % 50) == 0) {
                    System.out.println("Printing line:" + i);
                }
                List l = new ArrayList();
                l.add(d.getId());
                l.add(d.getLove());
                l.add(d.getJoy());
                l.add(d.getSurprise());
                l.add(d.getAnger());
                l.add(d.getSadness());
                l.add(d.getFear());
                l.add(d.getLabel());
                l.add(d.getComment());
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

    //scrive solo ID;commento;LabelFinale E senza i mixed!
    public void writeCsvFile_withoutValues(String outputName,List<models.Document> group)  {
        List<DatasetRow> list = new ArrayList<>();
        List<String> header= new ArrayList<>();
        header.add("id");
        header.add("comment");
        header.add("label");

        for (Document d:group) {
            if(!d.getFinaLabel().equals("mixed")) {
                DatasetRow dr = new DatasetRow.DatasetRowBuilder()
                        .setId(d.getId())
                        .setComment(d.getComment())
                        .setLabel(d.getFinaLabel())
                        .build();
                list.add(dr);
            }
        }

        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        // Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter(';');

        try {

            // initialize FileWriter object
            fileWriter = new FileWriter(outputName);
            // initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            // Create CSV file header
            csvFilePrinter.printRecord(header);
            int i = 0;
            for (DatasetRow d : list) {
                if ((i % 50) == 0) {
                    System.out.println("Printing line:" + i);
                }
                List l = new ArrayList();
                l.add(d.getId());
                l.add(d.getComment());
                l.add(d.getLabel());
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

}
