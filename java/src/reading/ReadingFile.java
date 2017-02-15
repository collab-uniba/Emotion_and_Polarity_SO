package reading;

import OrtuValidation.model.DocumentOrtu;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import replacing.Removing;

import java.io.*;
import java.util.*;

/**
 * Created by Francesco on 29/12/2016.
 */
public class ReadingFile {
    public List<String> read(String pathIn) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathIn));
        List<String> l= new ArrayList<>();
        try {

            String line = br.readLine();

            while (line != null) {
                l.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return l;
    }

    public Map<String,Double> readIDF(String type , String path) throws FileNotFoundException{
        path = path + type+".txt";
        Map<String, Double> map = new LinkedHashMap<>();
        File file= new File(path);
        String row;
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            row = scanner.nextLine();
            String[] ss=  row.split("\\s+");
            map.put(ss[0], Double.valueOf(ss[1]));
        }
        scanner.close();
        return map;
    }


    public List<DocumentOrtu> read_Tokenized_And_remove_Url(String pathFile){
        Removing rmurl= new Removing();

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
