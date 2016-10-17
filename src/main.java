
import printing.WriteCSV;
import reading.Reader_CSV_GROUP1;
import models.Document;
import java.util.List;

public class main {
    public static void main(String[] args) {
            Reader_CSV_GROUP1 readerCSVGROUP1 = new Reader_CSV_GROUP1();
            List<Document> group= readerCSVGROUP1.create_dcs_from_File(args[0]);
            new WriteCSV().writeCsvFile(args[1],group);


    }
}

