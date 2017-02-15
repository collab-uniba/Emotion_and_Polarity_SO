package classification;

import exceptions.CsvColumnNotFound;
import model.Document;
import printing.WriterCSV;
import reading.ReadingCSV;
import replacing.ReplacerTextWithMarks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Francesco on 14/02/2017.
 */
public class Baseline {

    public void baselineLexicalBased(Map<String, Document> docs, String emotion,String path) throws IOException {
        // ReadingFile rd = new ReadingFile();
        ReadingCSV rd = new ReadingCSV();
        ReplacerTextWithMarks rp = new ReplacerTextWithMarks();
        List<String> words = null;
        WriterCSV wt= new WriterCSV();
        String nameOutput = path + "testBaseline" + ".csv";
        try {
            switch (emotion) {
                case "love": {
                    words = rd.read_Column_CSV("java/res/WordnetCategories/positive_emotion.csv", "love", ';');
                    rp.setFinalLabelBaselineOnLexical(docs,words,"love");
                    wt.writeCsvFile(nameOutput,docs,true,"baseline");
                    break;
                }
                case "sadness": {
                    words = rd.read_Column_CSV("java/res/WordnetCategories/negative_emotion.csv", "sadness", ';');
                    break;
                }
                case "fear": {
                    List<String> words_pos = null;
                    List<String> words_neg = null;
                    List<String> words_ambiguos = null;

                    words_pos = rd.read_Column_CSV("java/res/WordnetCategories/positive_emotion.csv", "positive-fear", ';');
                    words_neg = rd.read_Column_CSV("java/res/WordnetCategories/negative_emotion.csv", "negative-fear", ';');
                    words_ambiguos = rd.read_Column_CSV("java/res/WordnetCategories/ambiguos_emotion.csv", "ambiguos-fear", ';');

                    break;
                }
                case "surprise": {
                    words = rd.read_Column_CSV("java/res/WordnetCategories/ambiguos_emotion.csv", "surprise", ';');
                    break;
                }
                case "joy": {
                    words = rd.read_Column_CSV("java/res/WordnetCategories/positive_emotion.csv", "joy", ';');
                    break;
                }
                case "anger": {
                    words = rd.read_Column_CSV("java/res/WordnetCategories/negative_emotion.csv", "general-dislike", ';');
                    break;
                }
            }
        } catch (CsvColumnNotFound csvColumnNotFound) {
            csvColumnNotFound.printStackTrace();
        }
    }
}
