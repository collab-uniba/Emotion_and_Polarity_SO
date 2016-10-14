package printing;

import models.Document;

import java.util.List;

/**
 * Created by Francesco on 14/10/2016.
 */
public class WriteCSV {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void writeCsvFile(String outputName,List<models.Document> group)  {
        for (Document d:group) {
            DatasetRow dr = new DatasetRow.DatasetRowBuilder()
                    .setLove(d.get_Love())
                    .setJoy(d.get_Joy())
                    .setSurprise(d.get_Surprise())
                    .setAnger(d.get_Anger())
                    .setSadness(d.get_Sadness())
                    .setFear(d.get_Fear())
                    .build();

        }

    }

}
