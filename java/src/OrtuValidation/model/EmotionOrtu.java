package OrtuValidation.model;

/**
 * Created by Francesco on 03/01/2017.
 */
public class EmotionOrtu {
    private boolean love;
    private boolean joy;
    private boolean surprise;
    private boolean anger;
    private boolean sadness;
    private boolean fear;

    /**
     *
     * @param love
     * @param joy
     * @param surprise
     * @param anger
     * @param sadness
     * @param fear
     * @param n indicates the majority agreement , if it is 1 is due to we maintain the duplies example : Taking group 1  there are 4 raters for each comment.
     *          if we want to put the final label for each rater than we put n = 1;
     */

    public EmotionOrtu(int love, int joy, int surprise, int anger, int sadness, int fear, int n){
        setLove(love,n);
        setJoy(joy,n);
        setSurprise(surprise,n);
        setAnger(anger,n);
        setSadness(sadness,n);
        setFear(fear,n);
    }
    private void setLove(int love,int n){
        if(love>=n){
            this.love=true;
        }
    }
    private void setJoy(int joy,int n){
        if(joy>=n){
            this.joy=true;
        }
    }
    private void setSurprise(int surprise,int n){
        if(surprise>=n){
            this.surprise=true;
        }
    }
    private void setAnger(int anger,int n){
        if(anger>=n){
            this.anger=true;
        }
    }
    private void setSadness(int sadness,int n){
        if(sadness>=n){
            this.sadness=true;
        }
    }
    private void setFear(int fear,int n){
        if(fear>=n){
            this.fear=true;
        }
    }

    public boolean isLove() {
        return love;
    }

    public boolean isJoy() {
        return joy;
    }

    public boolean isSurprise() {
        return surprise;
    }

    public boolean isAnger() {
        return anger;
    }

    public boolean isSadness() {
        return sadness;
    }

    public boolean isFear() {
        return fear;
    }


    public String getLove() {
        if (isLove())
            return "x";
        return "";
    }

    public String getJoy() {
        if(isJoy())
            return "x";
        return "";
    }

    public String getSurprise() {
        if(isSurprise())
            return "x";
        return "";
    }

    public String getAnger() {
        if(isAnger())
            return "x";
        return "";
    }

    public String getSadness() {
        if(isSadness())
            return "x";
        return "";
    }

    public String getFear() {
        if(isFear())
            return "x";
        return "";
    }
}
