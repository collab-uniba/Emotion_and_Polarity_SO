
import printing.WriteCSV;
import reading.Reader_CSV_GROUP1;
import models.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class main {
    public static void main(String[] args) {
        String FORMAT = ".csv";
        Reader_CSV_GROUP1 readerCSVGROUP1 = new Reader_CSV_GROUP1();
        List<Document> documentsFinal = new ArrayList<>();

        int l = 0;
        int start = 1;// group1 1 (prende :1 )
        int n = 0;
        int max=0;
        while (l < 3) {
            List<Document> documents = new ArrayList<>();
            n= Integer.parseInt(args[start]);
            max=n+start;
            int startPoint = 0;
            int endPoint = 0;
            int numOut=1;
            for (int j = start+1; j <= max; j++) {
                readerCSVGROUP1.create_dcs_from_File(args[start - 1], args[j], documents);
                endPoint = documents.size();
                new WriteCSV().writeCsvFile(args[args.length - 1] + "_" + args[start -1] + "_" + numOut + FORMAT, documents.subList(startPoint, endPoint));
                startPoint = endPoint;
                numOut++;
            }
            new WriteCSV().writeCsvFile(args[args.length - 1] + "_" + args[start-1] + "_" + "merged" + FORMAT, documents);
            new WriteCSV().writeCsvFile_withoutValues(args[args.length - 1] + "_" + args[start-1] + "_" + "withoutValues" + FORMAT, documents);
            l++;
            start = max + 2;
            //qqui aggiungo i documenti che stanno in
            documentsFinal.addAll(documents);
            System.out.println(documentsFinal.size());
        }

        //writing total complete Group1, 2 and 3 .csv
        new WriteCSV().writeCsvFile_withoutValues(args[args.length - 1] + "_OrtuEtAll"+ FORMAT, documentsFinal);
    }
}

