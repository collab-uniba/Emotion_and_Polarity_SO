package reading;

import exceptions.CsvColumnNotFound;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ReadingCSV {



    public List<String> read_Column_CSV(String pathFile,String columnName,Character delimiter) throws CsvColumnNotFound {
        List<String> tr= new ArrayList<>();
        FileReader in = null;
        try {
            in = new FileReader(pathFile);
            CSVFormat format =  CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter(delimiter).withQuote('"');
            CSVParser csvFileParser = new CSVParser(in, format);
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            Map<String,Integer> mapHeader=  csvFileParser.getHeaderMap();
            if(mapHeader.keySet().contains(columnName)) {
                for (CSVRecord csvRecord : csvRecords) {
                    tr.add(csvRecord.get(columnName));
                }
            }
            else {
                System.out.println("Problem during the search of the specified column!\n");
                System.out.println("These are the columns : \n" );
                for(String c : mapHeader.keySet()) {
                    System.out.println(c + " all its chars: ");
                    char[] chars= c.toCharArray();
                    for (int i =0;i<c.length();i++){
                        System.out.println(chars[i]+" ");
                    }
                    System.out.println("Probably there is a special char. The column that you would could be this \n "+ c+"\n");
                    System.out.println("Retry with this column name : "+  c);
                }
                throw new CsvColumnNotFound("Csv doesn't countain the column: "+columnName);
            }

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
