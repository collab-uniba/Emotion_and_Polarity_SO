
import printing.WriteCSV;
import reading.Reader_CSV_GROUP1;
import models.Document;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        String FORMAT= ".csv";
        Reader_CSV_GROUP1 readerCSVGROUP1 = new Reader_CSV_GROUP1();
        List<Document> documents = new ArrayList<>();
        int n=Integer.parseInt(args[1]);
        n+=2; //perchè i primi due devi saltarli, indicano group e num documenti
        int startPoint=0;
        int endPoint=0;
        for(int j=2;j<n;j++){
            readerCSVGROUP1.create_dcs_from_File(args[0], args[j], documents);
            endPoint=documents.size();
            int numOut=j-1;
            new WriteCSV().writeCsvFile(args[n]+"_"+numOut+FORMAT, documents.subList(startPoint,endPoint));
            startPoint=endPoint;
          }
        new WriteCSV().writeCsvFile(args[n]+"_"+"merged"+FORMAT, documents);
        new WriteCSV().writeCsvFile_withoutValues(args[n]+"_"+"withoutValues"+FORMAT, documents);
    }
}

