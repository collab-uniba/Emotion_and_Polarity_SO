package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Francesco on 07/01/2017.
 *Serve per creare il modello da dare in input a politeness.model.score
 *
 */
public class DocumentForPoliteness {

    private String text;
    private List<String> sentences;
    private List<String> parse;

    public List<String> getParse() {
        return parse;
    }

    public void setParse(List<String> parse) {
        this.parse = parse;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }






}
