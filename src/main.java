
import printing.WriterCSV;
import reading.ReaderCSV;
import models.Document;
import utility.Utility;
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
            //questi sono solo i documenti che si rifescono ad Un gruppo in ogni ciclo,attenzione.
            List<Document> documents = new ArrayList<>();
            n= Integer.parseInt(args[start]);//numero di documenti associati ad un gruppo
            max=n+start;//indice max in cui sta l'ultimo documento
            int startPoint = 0;
            int endPoint = 0;
            int numOut=1;
            //Questo FOR crea i Group_1.csv, Group2_1.csv , Group2_2.csv ecc
            for (int j = start+1; j <= max; j++) {
                //args[start - 1] : nome del gruppo
                //args[j] : path del primo documento
                rd.create_dcs_from_File(args[start - 1], args[j], documents);
                endPoint = documents.size();
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start -1] + "_" + numOut + FORMAT, documents.subList(startPoint, endPoint),
                        HEADERS,true,',',false);
                startPoint = endPoint;
                numOut++;
            }



            String lastFileMergedGroup2="";
            String lastFileMergedGroup1="";
            if(args[start-1].equals("group2")){
                tr.addAll(documents);//ordino gli elementi
                documents.clear();
                documents.addAll(tr);
                lastFileMergedGroup2=args[args.length - 1] + "_" + args[start-1] + "_" + "merged_with_duplied" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup2, documents,HEADERS,true,',',false);
            }
            else
             if(args[start-1].equals("group1")){
                lastFileMergedGroup1=args[args.length - 1] + "_" + args[start-1] + "_" + "merged_with_duplied" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup1, documents,HEADERS,true,',',false);

            }
            else {
                //gruppo 3
                String lastFileMerged = args[args.length - 1] + "_" + args[start - 1] + "_" + "merged" + FORMAT;
                if(l==2)
                    wr.writeCsvFile(lastFileMerged, documents,HEADERS,true,',',false);
                //fine gruppo 3
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents,HEADERS2,true,';',true);
            }

            if(args[start-1].equals("group1")){
                //adesso documents sono ordinati
                documents.clear();
                //qui crei il merged_no_duplied_MajAgOnFinalLabel
                lastFileMergedGroup1=args[args.length - 1] + "_" + args[start-1] + "_" + "merged_with_duplied" + FORMAT;
                rd.create_dcs_from_File("group1_noDuplied_MajAgOnCommentPolarity",lastFileMergedGroup1,
                       documents);
                lastFileMergedGroup1 = args[args.length - 1] + "_" + args[start-1] + "_" + "merged_noDuplied_MajAgOnCommentPolarity" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup1, documents,HEADERS,true,',',false);



                documents.clear();//deve ripopolare i documents del gruppo 1 senza duplicati
                lastFileMergedGroup1=args[args.length - 1] + "_" + args[start-1] + "_" + "merged_with_duplied" + FORMAT;
                rd.create_dcs_from_File("group1_noDuplied",lastFileMergedGroup1, documents);
                lastFileMergedGroup1 = args[args.length - 1] + "_" + args[start-1] + "_" + "merged_no_duplied_MajAgOnEmotions" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup1, documents,HEADERS,true,',',false);
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents,HEADERS2,true,';',true);
            }
            else
            if(args[start-1].equals("group2")){
                //adesso documents sono ordinati
                documents.clear();//deve ripopolare i documents del gruppo 2 senza duplicati
                 rd.create_dcs_from_File("group2_noDuplied",lastFileMergedGroup2, documents);
                lastFileMergedGroup2 = args[args.length - 1] + "_" + args[start-1] + "_" + "merged_no_duplied_MajAgOnEmotions" + FORMAT;
                wr.writeCsvFile(lastFileMergedGroup2, documents,HEADERS,true,',',false);
                wr.writeCsvFile(args[args.length - 1] + "_" + args[start - 1] + "_" + "idCommentLabel" + FORMAT, documents,HEADERS2,true,';',true);
            }

            documentsFinal.addAll(documents);
            l++;
            start = max + 2;
        }
        //writing total complete Group1, 2 and 3 .csv
        Utility u = new Utility();
        wr.writeCsvFile(args[args.length - 1] + "_OrtuEtAl"+ FORMAT,documentsFinal,HEADERS2,true,';',true);
        wr.writeCsvFile(args[args.length - 1] + "_OrtuEtAl_ForSenti4SD"+ FORMAT, documentsFinal,HEADERS3,false,';',true);
        //QUI Scrivo il tokenized
        wr.writeTokenized(args[args.length - 1] + "_OrtuEtAl_ForSenti4SD",FORMAT);
        List<Document> onlyComments_withoutURL= new ArrayList<>();
        //Qui riapro il tokenized
        onlyComments_withoutURL.addAll(rd.read_Tokenized_remove_Url(args[args.length - 1] + "_OrtuEtAl_ForSenti4SD_TOKENIZED"+FORMAT));
        int i=0;
        System.out.println("doc final "+ documentsFinal.size());
        System.out.println(" " + onlyComments_withoutURL.size());
        for(int j=0;j<documentsFinal.size()-1;j++){
            if(i<onlyComments_withoutURL.size()-1){
                Document d1=onlyComments_withoutURL.get(i);
                documentsFinal.get(j).setComment(d1.getComment());
                i++;
            }
        }
        wr.writeCsvFile(args[args.length - 1]+"_"+"_OrtuEtAl_WithoutURL"+FORMAT,documentsFinal,HEADERS4,false,';',true);

    }
}

