import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

/**
 * Created by Francesco on 13/10/2016.
 */
public class Utils {
    private Pattern pattern = Pattern.compile("[0-9]+");

    List<Document> create_Docs_From_File(File file){
        List<Document> l=new ArrayList<>();
        int[] arr = new int[6];

        String s = null;
        int num_Docs=4;
        BufferedReader bf = null;
        String[] group= new String[4];
        Pattern pattern = Pattern.compile("[0-9]+");
        try {
            bf = new BufferedReader(new FileReader(file));
            try {
                while ((s = bf.readLine()) != null) {
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
      System.out.println(comment);
        return comment;
    }

    private  void update_Annotations(int[] values,int[] arr){
        for (int i = 0; i < values.length; i++) {
            if (values[i]==1) {
                arr[i] += 1;
            }
        }
    }

    private Boolean[] results_From_Annotations(int[] arr){
        Boolean[] b= new Boolean[6];
        for(int i=0;i<6;i++){
            b[i] = arr[i] >= 2;
        }
        return b;
    }
}