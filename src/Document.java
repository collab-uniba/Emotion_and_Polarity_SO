/**
 * Created by Francesco on 13/10/2016.
 */



public class Document {
    private String comment;
    private String number;
    /*ORDINE LABEL : LOVE, JOY,SURPRISE,ANGER,SADNESS,FEAR*/
    private String[] sentiments;
    private String finaLabel;
    private enum label{POSITIVE,NEGATIVE,NEUTRAL,MIXED};

   void setComment(String comment) {
        this.comment = comment;
    }

    void setNumber(String number) {
        this.number = number;
    }

    void setSentiments(String[] sentiments) {
        this.sentiments = sentiments;
        if(sentiments!=null)
             setFinaLabel();
    }
// la position serve per l'ordine dinamico
    private boolean get_love_joy(){
        return (sentiments[0].equals("x") || sentiments[1].equals("x"));
    }
    private boolean get_anger__sadness_fear(){
        return (sentiments[3].equals("x") || sentiments[4].equals("x") || sentiments[5].equals("x"));
    }
    /*questo metodo setta la lb. finale
    - se (almeno una label tra sadness, anger, fear) e (nessuna label tra love and joy) = negative
    - se (almeno una label tra love and joy) e (nessuna label tra sadness, anger, fear) = positive
    - se sia una label positiva che una negativa -> mixed
    - se nessuna label -> neutral
    */
    private void setFinaLabel() {
        if(get_anger__sadness_fear() && !get_love_joy()){
            finaLabel=label.POSITIVE.toString();
        }
        else
            if(get_love_joy() && !get_anger__sadness_fear()){
                finaLabel=label.NEGATIVE.toString();
            }
        else
            if(get_love_joy() && get_anger__sadness_fear()){
                finaLabel=label.MIXED.toString();
            }
        else
            if(!(get_love_joy() && get_anger__sadness_fear())){
                finaLabel=label.NEUTRAL.toString();
            }
    }

    @Override
    public String toString() {
        String sentiment=" ";
        for(int i=0;i<sentiments.length;i++){
            sentiment+=sentiments[i];
            sentiment+=" ";
        }

        return number+"  " + sentiment + "  " + finaLabel ;
    }
}
