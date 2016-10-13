import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.regex.Pattern;
/**
 * Created by Francesco on 13/10/2016.
 */
public class main {
    public static void main(String[] args) {
        try {

            HashMap<String,Boolean[]> features;
            HashMap<String,String> comments;

            BufferedReader bf= new BufferedReader(new FileReader(new File("src/Joint_dataset_group1.csv")));
            String s=null;
            Pattern pattern = Pattern.compile("[0-9]+");
            int i=0;
            int j=0;
            while((s=bf.readLine())!=null) {
                char[] cr = s.toCharArray();
                j = 0;
                //Cerco "NUMERO;;;;;;", il resto è il commento, ovviamente.
                while (cr[j] != ';') {
                    j++;
                }
                j = j + 7;
                //Splitto sulla base del ; , il "Numero;;;;;;;"
                String ssub = s.substring(0, j);
                String[] ss = ssub.split(";");
                //se il primo elemento della sottostringa è un numero (ovviamente..)
                if (pattern.matcher(ss[0]).matches()) {
                    Boolean[] bool = new Boolean[6];
                    for (i = 0; i < ss.length-1; i++) {
                        bool[i] = !ss[i + 1].equals("");
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }


}
