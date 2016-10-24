package printing;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import models.Document;

import models.Document;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Questa classe, usando la libreria di Apache : commons , crea un file CSV.
 */
public class WriterCSV {

    // Delimiter used in CSV file
    private static final String NEW_LINE_SEPARATOR = "\n";
    private List<DatasetRow> list = new ArrayList<>();
    public void writeCsvFile(String outputName,List<models.Document> documents,String[] column,boolean withHeader,Character delimiter,boolean withoutMixed){
        list=new ArrayList<>();
        if(withoutMixed) {
           for (Document d : documents) {
               if (d.getFinaLabel()!=null && !d.getFinaLabel().equals("mixed"))
                   build_Row(d,column);
           }
         }
         else{
            for (Document d : documents) {
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
            for (DatasetRow d : list) {
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

    private void build_Row(Document d,String[] column){
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
        DatasetRow dr=null;
         if(!id.equals("") && !label.equals("") && !comment.equals("") && !fear.equals("") && !sadness.equals("") && !surprise.equals("") && !joy.equals("") && !love.equals("") && !anger.equals("")){
             //caso tutti
             dr = new DatasetRow.DatasetRowBuilder()
                     .setId(d.getId())
                     .setLove(d.get_Love())
                     .setJoy(d.get_Joy())
                     .setSurprise(d.get_Surprise())
                     .setAnger(d.get_Anger())
                     .setSadness(d.get_Sadness())
                     .setFear(d.get_Fear())
                     .setLabel(d.getFinaLabel())
                     .setComment(d.getComment())
                     .build();}
         else if(!label.equals("") && !comment.equals("") && !id.equals(""))
            dr = new DatasetRow.DatasetRowBuilder().setIdCommentLabel(d.getId(),d.getComment(),d.getFinaLabel()).build();
         else if(!label.equals("") && !comment.equals("")){
             dr = new DatasetRow.DatasetRowBuilder().setCommentLabel(d.getComment(),d.getFinaLabel()).build();
         }
         else if(!comment.equals("")){
             dr = new DatasetRow.DatasetRowBuilder().setComment(d.getComment()).build();
         }
        list.add(dr);
    }

    public void writeTokenized(String file,String format) {
        File inputTokenized = new File(file+"_TOKENIZED"+format);
        FileWriter fw = null;
        try {
            fw = new FileWriter(inputTokenized);
            System.out.println("Tokenizing input corpus ...");
            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new FileReader(file+format), new CoreLabelTokenFactory(),
                    "ptb3Escaping=false,untokenizable=allKeep,tokenizeNLs=true");
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();

                String s = String.valueOf(PTBTokenizer.getNewlineToken());
                if (String.valueOf(label).equals(s))
                    fw.append(System.lineSeparator());
                else{
                    if(String.valueOf(label).charAt(0)=='"') {
                        label.setValue("");
                        fw.append(label + " ");
                    }
                    else
                        fw.append(label + " ");
                }

            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
