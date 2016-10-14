import printing.WriteCSV;
import reading.Reader_CSV_GROUP1;
import models.Document;

import java.io.File;
import java.util.List;

/**
 * Created by Francesco on 13/10/2016.
 */
public class main {
    public static void main(String[] args) {
            Reader_CSV_GROUP1 readerCSVGROUP1 = new Reader_CSV_GROUP1();
            List<Document> group= readerCSVGROUP1.create_Docs_From_File(new File(args[0]));
            new WriteCSV().writeCsvFile(args[1],group);
    }


}
