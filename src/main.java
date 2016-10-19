
import printing.WriteCSV;
import reading.Reader_CSV_GROUP1;
import models.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class main {
    public static void main(String[] args) {
        String FORMAT = ".csv";
        Reader_CSV_GROUP1 readerCSVGROUP1 = new Reader_CSV_GROUP1();
        List<Document> documentsFinal = new ArrayList<>();
        TreeSet<Document> tr= new TreeSet<Document>();
        int l = 0;
        int start = 1;// group1 1 (prende :1 )
        int n = 0;
        int max=0;
        while (l < 3) {
            TreeSet<Document> tr1= new TreeSet<Document>();
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
            String lastFileMergedGroup2="";
            if(l==1){
                tr.addAll(documents);//ordino gli elementi
                documents.clear();
                documents.addAll(tr);
                lastFileMergedGroup2=args[args.length - 1] + "_" + args[start-1] + "_" + "merged_with_duplied" + FORMAT;
                new WriteCSV().writeCsvFile(lastFileMergedGroup2, documents);
            }
            else {
                String lastFileMerged = args[args.length - 1] + "_" + args[start - 1] + "_" + "merged" + FORMAT;
                if(l==2)
                    new WriteCSV().writeCsvFile(lastFileMerged, documents);
                new WriteCSV().writeCsvFile_withoutValues(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents);
            }
            if(l==1){
                //adesso documents sono ordinati
                documents.clear();
                 readerCSVGROUP1.create_dcs_from_File("group2_special",lastFileMergedGroup2, documents);
                lastFileMergedGroup2 = args[args.length - 1] + "_" + args[start-1] + "_" + "merged_no_duplied" + FORMAT;
                new WriteCSV().writeCsvFile(lastFileMergedGroup2, documents);
                new WriteCSV().writeCsvFile_withoutValues(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents);
            }
            documentsFinal.addAll(documents);
            l++;
            start = max + 2;

        }
        //writing total complete Group1, 2 and 3 .csv
        new WriteCSV().writeCsvFile_withoutValues(args[args.length - 1] + "_OrtuEtAll"+ FORMAT, documentsFinal);
    }
}

