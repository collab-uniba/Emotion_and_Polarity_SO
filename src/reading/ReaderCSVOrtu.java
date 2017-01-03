package reading;

import model.DocumentOrtu;
import model.EmotionOrtu;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import replacing.RemoveURL;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;



/**
 * Legge  i documenti dal file csv;
 */

public class ReaderCSVOrtu {

   public List<DocumentOrtu> create_dcs_from_File(String group, String pathFile, List<DocumentOrtu> l) {
       Reader in = null;

       try {
            in = new FileReader(pathFile);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> list=new ArrayList<>();
           String[] finalLabel=null;
           for (CSVRecord record : records) {
                list.add(record);
            }

            for(int i=0;i<list.size();i++) {
                int[] arr = new int[6];
                DocumentOrtu dc = new DocumentOrtu();
                CSVRecord r = list.get(i);
                switch (group) {
                    case "group1_noDuplied":
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
                    case "group1_noDuplied_MajAgOnCommentPolarity":
                         finalLabel= new String[4];
                        for (int j = 0; j <= 3; j++) {
                            int s = j;
                             finalLabel[s]= r.get("label");
                            s++;
                            if (s < 4) {
                                i++;
                                if (i < list.size() - 1)
                                    r = list.get(i);
                            }
                        }
                        break;
                    case "group2_noDuplied":
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
                    case "group2_noDuplied_MajAgOnCommentPolarity":
                        finalLabel= new String[3];
                        for (int j = 0; j <= 2; j++) {
                            int s = j;
                            finalLabel[s]= r.get("label");
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
               if( group.equals("group1")){
                   dc.setComment(r.get("comment"));
                   EmotionOrtu emo= new EmotionOrtu(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],1);
                   dc.setEmotions(emo);
               }
               else
                if (group.equals("group2") || group.equals("group3") ) {
                    String s= r.get("comment").replaceAll("\n", " ");
                    s=s.replaceAll("\r"," ");
                    dc.setComment(s);
                    EmotionOrtu emo= new EmotionOrtu(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],1);
                    dc.setEmotions(emo);
                }
                else
                    if( group.equals("group2_noDuplied") || group.equals("group1_noDuplied")) {
                        EmotionOrtu emo= new EmotionOrtu(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],2);
                        dc.setComment(r.get("comment"));
                        dc.setEmotions(emo);
                    }
                else
                    if(group.equals("group1_noDuplied_MajAgOnCommentPolarity")){
                        dc.setComment(r.get("comment"));
                        dc.setFinaLabelBaseOnEachCommentPolarity(finalLabel,3);
                    }
                    else
                    if(group.equals("group2_noDuplied_MajAgOnCommentPolarity") ){
                        dc.setComment(r.get("comment"));
                        dc.setFinaLabelBaseOnEachCommentPolarity(finalLabel,2);
                    }
                l.add(dc);
            }
            } catch (IOException e) {
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

    public List<DocumentOrtu> read_Tokenized_remove_Url(String pathFile){
        RemoveURL rmurl= new RemoveURL();

        FileReader in = null;
        List<DocumentOrtu> l=new ArrayList<>();
        try {
            in = new FileReader(pathFile);
            CSVFormat format =  CSVFormat.EXCEL.withQuote(';');
            CSVParser csvFileParser = new CSVParser(in, format);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                DocumentOrtu d= new DocumentOrtu();
                d.setComment(rmurl.removeUrlTwo(csvRecord.get(0)));
                l.add(d);
            }
            csvFileParser.close();
        } catch (IOException e) {
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