package reading;

import models.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Legge  i documenti dal file csv;
 */
public class Reader_CSV_GROUP1 {
    public List<Document> create_Docs_From_File(File file){
        List<Document> l=new ArrayList<>();
        String s = null;
        int num_Docs=4;
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(file));
            try {
                while ((s = bf.readLine()) != null) {
                    int[] arr = new int[6];
                    Document dc= new Document();
                    update_Annotations(get_Values(s),arr);
                    for (int m = 1; m < num_Docs; m++) {
                        if ((s = bf.readLine()) != null) {
                            update_Annotations(get_Values(s),arr);
                        }
                    }
                    dc.setNumber(get_Id(s));
                    dc.setComment(get_Comment(s));
                    dc.setSentiments(results_From_Annotations(arr));
                    l.add(dc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return l;
    }

    //questo recupera solo l'id.
    private String get_Id(String doc_Readed) {
        String[] ss = doc_Readed.split(";");
        return ss[0];
    }
    //recupera i valori
    private int[] get_Values(String doc_Readed) {
        String[] ss = doc_Readed.split(";");
        int a[] = new int[6];
        for(int i=1;i<ss.length-1;i++){
            //sullo splitting non ci siamo : problema -> se
            if(ss[i].equals("x"))
                    a[i-1]=1;
        }
        return a;
    }

    //recupera il commento
    private String get_Comment(String doc_Readed) {
        String[] ss = doc_Readed.split(";");
        int i=1;
        String comment = "";
        while(i<=ss.length-1){
            if(!(ss[i].equals("x") || ss[i].equals(""))){
                comment+=ss[i];
                if(i+1<ss.length-1)
                  comment+=";";
            }
            i++;
        }
        return comment;
    }

    //Questo metodo fa questo : ho un array iniziale vuoto , costituito da 0,0,0,0 , ogni volta che viene passato un array che contiene le annotazioni
    //di un annotatore , vengono incrementati nell'array finale gli elementi con stessa corrispondenza di una unità.
    private  void update_Annotations(int[] values,int[] arr){
        for (int i = 0; i < values.length; i++) {
            if (values[i]==1) {
                arr[i] += 1;
            }
        }
    }
//se due annotatori hanno espresso stesso giudizio su un elemento allora questo è avrà "x" come finale : esempio joy , due annotatori annotano "x" su joy , quindi joy finale avrà "x"
    private String[] results_From_Annotations(int[] arr){
        String[] b= new String[6];
        for(int i=0;i<6;i++){
            if(arr[i]>=2){
                b[i]="x";
            }
            else b[i]=" ";
        }
        return b;
    }
}