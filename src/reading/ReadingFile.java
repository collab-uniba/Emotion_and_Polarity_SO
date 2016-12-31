package reading;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francesco on 29/12/2016.
 */
public class ReadingFile {
    public List<String> read(String pathIn) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathIn));
        List<String> l= new ArrayList<>();
        try {

            String line = br.readLine();

            while (line != null) {
                l.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return l;
    }

}
