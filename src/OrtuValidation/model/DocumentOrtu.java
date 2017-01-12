package OrtuValidation.model;

public class DocumentOrtu implements Comparable<Object>{
    private String comment;
    private String id;
    private EmotionOrtu emotionOrtu;
    private String finaLabel;
    private enum label{positive,negative,neutral,mixed};
    public String getId(){
        return id;
    }
    public String getComment(){
        return comment;
    }
    public String getFinaLabel(){
        return finaLabel;
    }


   public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(String id) {
        this.id=id;
    }

    public void setEmotions(EmotionOrtu emotionOrtu) {
        this.emotionOrtu = emotionOrtu;
        if(emotionOrtu !=null)
             setFinaLabelBasedOnEmotions();
    }

    private boolean is_love_joy(){
        return (emotionOrtu.isLove() || emotionOrtu.isJoy());
    }
    private boolean is_anger__sadness_fear(){
        return (emotionOrtu.isAnger() || emotionOrtu.isSadness() || emotionOrtu.isFear());
    }
    /*questo metodo setta la lb. finale
    - se (almeno una label tra sadness, anger, fear) e (nessuna label tra love and joy) = negative
    - se (almeno una label tra love and joy) e (nessuna label tra sadness, anger, fear) = positive
    - se sia una label positiva che una negativa -> mixed
    - se nessuna label -> neutral
    */
    private void setFinaLabelBasedOnEmotions() {
        if(is_anger__sadness_fear() && !is_love_joy()){
            finaLabel= label.negative.toString();
        }
        else
            if(is_love_joy() && !is_anger__sadness_fear()){
                finaLabel= label.positive.toString();
            }
        else
            if(is_love_joy() && is_anger__sadness_fear()){
                finaLabel= label.mixed.toString();
            }
        else
            if(!(is_love_joy() && is_anger__sadness_fear())){
                finaLabel= label.neutral.toString();
            }
    }


    private int positive;
    private int negative;
    private int mixed;
    private int neutral;
    private void count_polarity(String[] labels){
        positive=0;
        negative=0;
        mixed=0;
        neutral=0;
        for(String x : labels){
            if(x.equals("positive"))
                positive++;
            else
                if(x.equals("negative"))
                    negative++;
            else
                if(x.equals("mixed"))
                    mixed++;
            else
                if(x.equals("neutral"))
                    neutral++;
        }
    }

    /*questo metodo setta la lb. finale


   */
    public void setFinaLabelBaseOnEachCommentPolarity(String[] labels,int n){
			count_polarity(labels);
            //Casi generici in cui di sicuro ho almeno 2 di una cosa e poi il resto neutral
            if(positive>=2 && negative==0 && mixed==0){
                this.finaLabel= label.positive.toString();
            }
            else  if(negative>=2 && positive==0 && mixed==0){
                this.finaLabel= label.negative.toString();
            }
            else  if(mixed>=2 && positive==0 && negative==0){
                this.finaLabel= label.mixed.toString();
            }
            else  if(neutral>=n && mixed<=1 && positive<=1 && negative <=1 ){
                this.finaLabel= label.neutral.toString();
            }
            //Casi mixed
            else if(neutral>=0 && mixed >=0 && positive >=0 && negative >=0){
                this.finaLabel= label.mixed.toString();
            }
    }

    public EmotionOrtu getEmotionOrtu() {
        return emotionOrtu;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof DocumentOrtu){
            DocumentOrtu d= (DocumentOrtu) o;
            int id1= Integer.parseInt(id);
            int id2= Integer.parseInt(d.getId());
            if(id1>=id2){
                return 1;
            }
            if(id1<id2) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return id;
    }
}
