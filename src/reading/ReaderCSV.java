package reading;

import models.Document;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import utility.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Legge  i documenti dal file csv;
 */

public class ReaderCSV {

   public List<Document> create_dcs_from_File(String group, String pathFile, List<Document> l) {
       Reader in = null;
       try {
            in = new FileReader(pathFile);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> list=new ArrayList<>();
            for (CSVRecord record : records) {
                list.add(record);
            }

            for(int i=0;i<list.size();i++) {
                int[] arr = new int[6];
                Document dc = new Document();
                CSVRecord r = list.get(i);
                switch (group) {
                    case "group1":
                        for (int j = 0; j <= 3; j++) {
                            update_Annotations(r, arr);
                            int s = j;
                            s++;
                            if (s < 4) {
                                i++;
                                if (i < list.size() - 1)
                                    r = list.get(i);
                            }
                        }
                        break;
                    case "group2_special":
                        for (int j = 0; j <= 2; j++) {
                            update_Annotations(r, arr);
                            int s = j;
                            s++;
                            if (s < 3) {
                                i++;
                                if (i < list.size() - 1)
                                    r = list.get(i);
                            }
                        }
                        break;
                    default:
                        update_Annotations(r, arr);
                        break;
                }
                dc.setId(r.get(0));
                if (group.equals("group2") || group.equals("group3")) {
                    String s= r.get("comment").replaceAll("\n", " ");
                    s=s.replaceAll("\r"," ");
                    dc.setComment(s);
                    dc.setSentiments(results_From_Annotations(arr, 1));
                }
                else
                    if(group.equals("group1") || group.equals("group2_special")){
                        dc.setComment(r.get("comment"));
                        dc.setSentiments(results_From_Annotations(arr, 2));
                }
                l.add(dc);
            }
        } catch (java.io.IOException e) {
             e.printStackTrace();
        }
  return l;
}
    //Questo metodo fa questo : ho un array iniziale vuoto , costituito da 0,0,0,0 , ogni volta che viene passato un array che contiene le annotazioni
    //di un annotatore , vengono incrementati nell'array finale gli elementi con stessa corrispondenza di una unità.
    private  void update_Annotations(CSVRecord rc,int[] arr){
        if(rc.get("love").equals("x")){
            arr[0]+=1;
        }
        if(rc.get("joy").equals("x")){
            arr[1]+=1;
        }
        if(rc.get("surprise").equals("x")){
            arr[2]+=1;
        }
        if(rc.get("anger").equals("x")){
            arr[3]+=1;
        }
        if(rc.get("sadness").equals("x")){
            arr[4]+=1;
        }
        if(rc.get("fear").equals("x")){
            arr[5]+=1;
        }
    }
//se due annotatori hanno espresso stesso giudizio su un elemento allora questo è avrà "x" come finale : esempio joy , due annotatori annotano "x" su joy , quindi joy finale avrà "x"
    private String[] results_From_Annotations(int[] arr,int n){
        String[] b= new String[6];
        for(int i=0;i<6;i++){
            if(arr[i]>=n){
                b[i]="x";
            }
            else b[i]=" ";
        }
        return b;
    }

    public List<Document> read_Tokenized_remove_Url(String pathFile){
        Utility u = new Utility();
        FileReader in = null;
        List<Document> l=new ArrayList<>();
        try {
            in = new FileReader(pathFile);
            CSVFormat format =  CSVFormat.EXCEL.withQuote(';');
            CSVParser csvFileParser = new CSVParser(in, format);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Document d= new Document();
                d.setComment(u.removeUrl(csvRecord.get(0)));
                l.add(d);
            }
            csvFileParser.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
            in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return l;
    }
}