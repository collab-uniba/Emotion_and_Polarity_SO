
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import printing.WriterCSV;
import reading.ReaderCSV;
import models.Document;
import utility.Utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class main {
    public static void main(String[] args) {
        String FORMAT = ".csv";
        ReaderCSV rd = new ReaderCSV();
        List<Document> documentsFinal = new ArrayList<>();
        TreeSet<Document> tr= new TreeSet<Document>();
        List<String> headerFirstType= new ArrayList<>();
        String[] HEADERS={"id","love","joy","surprise","anger","sadness","fear","label","comment"};
        String[] HEADERS2= {"id","comment","label"};
        String[] HEADERS3= {"comment"};
        String[] HEADERS4= {"comment","label"};
        WriterCSV wr= new WriterCSV();
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
                rd.create_dcs_from_File(args[start - 1], args[j], documents);
                endPoint = documents.size();
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start -1] + "_" + numOut + FORMAT, documents.subList(startPoint, endPoint),
                        HEADERS,true,',',false);
                startPoint = endPoint;
                numOut++;
            }
            String lastFileMergedGroup2="";
            if(args[start-1].equals("group2")){
                tr.addAll(documents);//ordino gli elementi
                documents.clear();
                documents.addAll(tr);
                lastFileMergedGroup2=args[args.length - 1] + "_" + args[start-1] + "_" + "merged_with_duplied" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup2, documents,HEADERS,true,',',false);
            }
            else {
                String lastFileMerged = args[args.length - 1] + "_" + args[start - 1] + "_" + "merged" + FORMAT;
                if(l==2)
                    wr.writeCsvFile(lastFileMerged, documents,HEADERS,true,',',false);
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents,HEADERS2,true,';',true);
            }
            if(args[start-1].equals("group2")){
                //adesso documents sono ordinati
                documents.clear();
                 rd.create_dcs_from_File("group2_special",lastFileMergedGroup2, documents);
                lastFileMergedGroup2 = args[args.length - 1] + "_" + args[start-1] + "_" + "merged_no_duplied" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup2, documents,HEADERS,true,',',false);
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents,HEADERS2,true,';',true);
            }
            documentsFinal.addAll(documents);
            l++;
            start = max + 2;
        }
        //writing total complete Group1, 2 and 3 .csv
        wr.writeCsvFile(args[args.length - 1] + "_OrtuEtAl"+ FORMAT,documentsFinal,HEADERS2,true,';',true);
        wr.writeCsvFile(args[args.length - 1] + "_OrtuEtAl_ForSenti4SD"+ FORMAT, documentsFinal,HEADERS3,false,';',true);
        wr.writeTokenized(args[args.length - 1] + "_OrtuEtAl_ForSenti4SD",FORMAT);
        List<Document> onlyComments_withoutURL= new ArrayList<>();
        onlyComments_withoutURL.addAll(rd.read_Tokenized_remove_Url(args[args.length - 1] + "_OrtuEtAl_ForSenti4SD_TOKENIZED"+FORMAT));
        int i=0;
        for(Document d:documentsFinal){
            while(i<onlyComments_withoutURL.size()-1){
                d.setComment(onlyComments_withoutURL.get(i).getComment());
                i++;
                break;
            }
        }
        wr.writeCsvFile(args[args.length - 1]+"_"+"_OrtuEtAl_WithoutURL"+FORMAT,documentsFinal,HEADERS4,true,';',false);
    }
}

