package reading;

import com.sun.prism.impl.Disposer;
import edu.stanford.nlp.optimization.QNMinimizer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ReadingCSV {

    /**
     * Read all csv  columns
     * @param pathFile where is the file to read
     * @return
     */
    public Map<String,List<String>> read_AllColumn_CSV(String pathFile,Character delimiter){
        //Hashmap perchè i nomi delle colonne devono essere univoci, mentre List perchè i duplicati mi servono, e non mi servono in ordine
        LinkedHashMap<String, List<String>> tr= new LinkedHashMap<>();
        FileReader in = null;
        try {
            in = new FileReader(pathFile);
            CSVFormat format =  CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter(delimiter);
            CSVParser csvFileParser = new CSVParser(in, format);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

           Map<String,Integer> mapHeader=  csvFileParser.getHeaderMap();
              for(String key:  mapHeader.keySet()){
                  tr.put(key,new ArrayList<>());
              }
            //Aggiungo le sottoliste di ogni chiave
            for(String  columnName : tr.keySet()){
                for (CSVRecord csvRecord : csvRecords) {
                        tr.get(columnName).add(csvRecord.get(columnName));
                }
            }
            //Stampa a video
         /* Set<String> key=  tr.keySet();

            for(String k : key){
                System.out.println(k+ ":");
                for(String m : tr.get(k)){
                    System.out.println(m);
                }
                System.out.println( "\n" );
            }*/

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
        return tr;
    }
}
