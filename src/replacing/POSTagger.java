package replacing;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger {

    /**
     * POS tagging of the input string
     * @return the tagged string
     */
    public String posTag(String input){
        // Initialize the tagger
        MaxentTagger tagger = new MaxentTagger(
                "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        // The tagged string
        return tagger.tagString(input);

    }

}
