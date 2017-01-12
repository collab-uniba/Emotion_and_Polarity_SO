package OrtuValidation.computing;

import OrtuValidation.model.DocumentOrtu;
import OrtuValidation.model.EmotionOrtu;
import reading.ReadingCSV;

import java.util.List;
import java.util.Map;

/**
 * Created by Francesco on 03/01/2017.
 */
public class Ortu {

    private Map<String,List<String>> group=null;
    private List<String> id=null;
    private List<String> comment=null;
    private List<String> love=null;
    private List<String> joy=null;
    private List<String> anger=null;
    private List<String> surprise=null;
    private List<String> sadness=null;
    private List<String> fear=null;
    private List<String> label=null;

    //** POLARITY (positive,negative,neutral,mixed)
    private String[] finalLabel=null;

    //**EMOTIONS
    private int Love=0;
    private int Joy=0;
    private int Anger=0;
    private int Surprise= 0;
    private int Sadness=0;
    private int Fear=0;
    public void bygroups(String groupName,String path,Character delimiter,List<DocumentOrtu> l){
        ReadingCSV rd= new ReadingCSV();

        group  = rd.read_AllColumn_CSV(path,delimiter);
        id = group.get("\uFEFFid");//carattere speciale
        if(id==null){
            id= group.get("id");
        }
        comment = group.get("comment");//carattere speciale
        love = group.get("love");//carattere speciale
        joy = group.get("joy");//carattere speciale
        anger = group.get("anger");
        surprise= group.get("surprise");
        sadness= group.get("sadness");
        fear=group.get("fear");
        label=group.get("label");

        switch (groupName) {
            case "group1":{
                for(int i=0;i<id.size();i++){
                    String idActual = id.get(i);
                    if(love.get(i).equals("x")){
                        Love++;
                    }
                    if(joy.get(i).equals("x")){
                        Joy++;
                    }
                    if(anger.get(i).equals("x")){
                        Anger++;
                    }
                    if(fear.get(i).equals("x")){
                        Fear++;
                    }
                    if(sadness.get(i).equals("x")){
                        Sadness++;
                    }
                    if(surprise.get(i).equals("x")){
                        Surprise++;
                    }
                    DocumentOrtu d= createDocument(groupName,i);

                        //aggiungo il documento ai documenti finali
                        l.add(d);

                        //reset
                        Love=0;
                        Joy=0;
                        Sadness=0;
                        Anger=0;
                        Surprise=0;
                        Fear=0;
                    }
                    break;
                }
            case "group1_noDuplied_MajAgOnCommentPolarity":{
                int j = 0;
                finalLabel = new String[4];
                for (int i = 0; i < id.size(); i++) {
                    String idActual = id.get(i);
                    finalLabel[j] = label.get(i);
                    j++;
                    if (id.size() == i + 1 || !id.get(i + 1).equals(idActual)) {
                        DocumentOrtu d = createDocument(groupName, i);

                        //aggiungo il documento ai documenti finali
                        l.add(d);

                        //reset
                        j = 0;
                    }

                }
                break;
        }
            case "group2_noDuplied_MajAgOnCommentPolarity": {
                finalLabel = new String[3];
                int j = 0;
                for (int i = 0; i < id.size(); i++) {
                    String idActual = id.get(i);
                    finalLabel[j] = label.get(i);
                    j++;
                    if (id.size() == i + 1 || !id.get(i + 1).equals(idActual)) {
                        DocumentOrtu d = createDocument(groupName, i);

                        //aggiungo il documento ai documenti finali
                        l.add(d);

                        //reset
                        j = 0;
                    }
                }
                break;
            }
            default: {
                for (int i = 0; i < id.size(); i++) {
                    String idActual = id.get(i);
                    if (love.get(i).equals("x")) {
                        Love++;
                    }
                    if (joy.get(i).equals("x")) {
                        Joy++;
                    }
                    if (anger.get(i).equals("x")) {
                        Anger++;
                    }
                    if (fear.get(i).equals("x")) {
                        Fear++;
                    }
                    if (sadness.get(i).equals("x")) {
                        Sadness++;
                    }
                    if (surprise.get(i).equals("x")) {
                        Surprise++;
                    }
                    if (id.size() == i + 1 || !id.get(i + 1).equals(idActual)) {
                        DocumentOrtu d = createDocument(groupName, i);

                        //aggiungo il documento ai documenti finali
                        l.add(d);

                        //reset
                        Love = 0;
                        Joy = 0;
                        Sadness = 0;
                        Anger = 0;
                        Surprise = 0;
                        Fear = 0;
                    }

                }
            }
         }
    }

    private DocumentOrtu createDocument(String groupName,int i) {
        DocumentOrtu dc = new DocumentOrtu();
        switch (groupName) {
            case "group1": {
                dc.setId(id.get(i));
                dc.setComment(comment.get(i));
                EmotionOrtu emo = new EmotionOrtu(Love, Joy, Surprise, Sadness, Anger, Fear, 1);
                dc.setEmotions(emo);
                return dc;
            }
            case "group2": {
                dc.setId(id.get(i));
                String s = comment.get(i).replaceAll("\n", " ");
                s = s.replaceAll("\r", " ");
                dc.setComment(s);
                EmotionOrtu emo = new EmotionOrtu(Love, Joy, Surprise, Sadness, Anger, Fear, 1);
                dc.setEmotions(emo);
                return dc;
            }
            case "group3": {
                dc.setId(id.get(i));
                String s = comment.get(i).replaceAll("\n", " ");
                s = s.replaceAll("\r", " ");
                dc.setComment(s);
                EmotionOrtu emo = new EmotionOrtu(Love, Joy, Surprise, Sadness, Anger, Fear, 1);
                dc.setEmotions(emo);
                return dc;
            }
            case "group2_noDuplied": {
                dc.setId(id.get(i));
                EmotionOrtu emo = new EmotionOrtu(Love, Joy, Surprise, Sadness, Anger, Fear, 2);
                dc.setComment(comment.get(i));
                dc.setEmotions(emo);
                return dc;
            }
            case "group1_noDuplied":{
                dc.setId(id.get(i));
                EmotionOrtu emo = new EmotionOrtu(Love, Joy, Surprise, Sadness, Anger, Fear, 2);
                dc.setComment(comment.get(i));
                dc.setEmotions(emo);
                return dc;
            }

            case "group1_noDuplied_MajAgOnCommentPolarity": {
                     dc.setId(id.get(i));
                    dc.setComment(comment.get(i));
                    dc.setFinaLabelBaseOnEachCommentPolarity(finalLabel, 3);
                    return dc;
            }
            case "group2_noDuplied_MajAgOnCommentPolarity": {
                     dc.setId(id.get(i));
                    dc.setComment(comment.get(i));
                    dc.setFinaLabelBaseOnEachCommentPolarity(finalLabel, 2);
                    return dc;

            }
        }
        return dc;
    }
}
