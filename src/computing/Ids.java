package computing;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Francesco on 16/01/2017.
 * Questa classe serve per memorizzare tutti gli unigrammi  o bigrammi o emozioni con TF_IDF >0.0
 */
public class Ids {
    private Set<String> ids_emo = new LinkedHashSet<>();
    private Set<Integer> ids_grams= new TreeSet<>();


    public Set<String> getIds_emo() {
        return ids_emo;
    }

    public void setIds_emo(Set<String> ids_emo) {
        this.ids_emo = ids_emo;
    }

    public Set<Integer> getIds_grams() {
        return ids_grams;
    }

    public void setIds_grams(Set<Integer> ids_grams) {
        this.ids_grams = ids_grams;
    }
}
