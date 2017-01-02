package replacing;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.ArrayList;
import java.util.List;

public class POSTagger {
    private MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
    /**
     * POS tagging of the input string
     * @return the tagged string
     */
    public String posTag(String input){
        // Initialize the tagger
       // MaxentTagger tagger = new MaxentTagger(
         //       "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger" );
        // The tagged string
        return tagger.tagString(input);

    }

    public List<String> posTag(List<String> documents){
        List<String> docsPostTagged= new ArrayList<String>();
        for(String doc: documents){
            docsPostTagged.add(posTag(doc));
        }
       return docsPostTagged;
    }
}
