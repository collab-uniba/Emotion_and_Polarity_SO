package printing;

import models.Document;\

import models.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
/**
 * Created by Francesco on 14/10/2016.
 */
public class WriteCSV {

    // Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    public void writeCsvFile(String outputName,List<models.Document> group)  {
        List<DatasetRow> list = new ArrayList<>();
        List<String> header= new ArrayList<>();
        header.add("ID");
        String[] HEADERS={"LOVE","JOY","SURPRISE","ANGER","SADNESS","FEAR"};
        Collections.addAll(header,HEADERS);
        header.add("LABEL");
        header.add("COMMENT");

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
                l.add("t" + i);

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

}
