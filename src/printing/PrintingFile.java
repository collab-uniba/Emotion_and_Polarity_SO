package printing;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Francesco on 23/12/2016.
 */
public class PrintingFile {
    public void print(String pathOu,List<String> texts){
        File input = new File(pathOu);
        FileWriter fw = null;
        try {
            fw = new FileWriter(input);
            for(String s : texts){
                fw.append(s + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTokenized(String pathIn,String pathOut) {
        File inputTokenized = new File( pathOut);
        FileWriter fw = null;
        try {
            fw = new FileWriter(inputTokenized);
            System.out.println("Tokenizing input corpus ...");
            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new FileReader(pathIn), new CoreLabelTokenFactory(),
                    "ptb3Escaping=false,untokenizable=allKeep,tokenizeNLs=true");
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                String s = String.valueOf(PTBTokenizer.getNewlineToken());
                if (String.valueOf(label).equals(s))
                    fw.append(System.lineSeparator());
                else{
                    if(String.valueOf(label).charAt(0)=='"') {
                        label.setValue("");
                        fw.append(label + " ");
                    }
                    else
                        fw.append(label + " ");
                }

            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
