import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Francesco on 13/10/2016.
 */
public class Utils {
    private Pattern pattern = Pattern.compile("[0-9]+");

    List<Document> create_Docs_From_File(File file){
        List<Document> l=new ArrayList<>();
        List<Boolean[]> annotations = new ArrayList<>();//NOTA QUESTO.
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
                    group[0] = s;
                    set_Doc_detail(group[0],dc);
                    get_sentiment(group[0]);
                    for (int m = 1; m < num_Docs; m++) {
                        Boolean[] b= new Boolean[6];
                        if ((s = bf.readLine()) != null) {
                            group[m] = s;
                            b= get_sentiment(group[m]);
                            annotations.add(b);
                        }
                    }
                    //per tutte le 4 annotazioni ottenute , chiama il metodo di dc passando le 4 annotazioni.
                    //alla fine le aggiungi al DC.

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

    private int  set_Doc_detail(String doc_Readed,Document d){
        int i=0;
        int j=0;
        char[] cr = doc_Readed.toCharArray();
        j = 0;
        //Cerco "NUMERO;;;;;;", il resto è il commento, ovviamente.
        while (cr[j] != ';') {
            j++;
        }
        j = j + 7;
        //Splitto sulla base del ; , il "Numero;;;;;;;"
        String ssub = doc_Readed.substring(0, j);
        String[] ss = ssub.split(";");
        d.setNumber(ss[0]);
        //inserisco il commento , togliendo prima TUTTE le zozzate finali dei ;
        int l=cr.length-1;
        while (cr[l] == ';') {
            l--;
        }
        d.setComment(doc_Readed.substring(j+1,l));
        return j;//NON VA BENE IL J QUI, SERVE CHE POI NELL'ALTRO METODO TI VAI A FARE I BOOLEAN , QUINDI DEVI RESTITUIRE
    }


    //se il primo elemento della sottostringa è un numero (ovviamente..)
       /* if (pattern.matcher(ss[0]).matches()) {
        Boolean[] bool = new Boolean[6];
        for (i = 0; i < ss.length-1; i++) {
            bool[i] = !ss[i + 1].equals("");
        }
    }*/

}