package printing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Francesco on 23/12/2016.
 */
public class PrintingFile {
    public void print(String pathOu,List<String> comments){
        File input = new File(pathOu);
        FileWriter fw = null;
        try {
            fw = new FileWriter(input);
            for(String s : comments){
                fw.append(s + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
