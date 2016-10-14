import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
/**
 * Created by Francesco on 13/10/2016.
 */
public class main {
    public static void main(String[] args) {
            Utils utils= new Utils();
            List<Document> group=utils.create_Docs_From_File(new File("src/Joint_dataset_group1_Updated.csv"));


    }


}
