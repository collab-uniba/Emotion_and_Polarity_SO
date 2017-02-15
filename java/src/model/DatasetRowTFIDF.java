package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for printing in .csv files
 */
public class DatasetRowTFIDF {

	//private  final String id;
	private  final String document;
    private final String pos_score;
    private final String neg_score;
    private final String politeness;
    private final String impoliteness;
    private final Document.Mood mood;
    private final List<String> tf_idf;
    private final String affective_label;
    private final String baseline_label;
    private final String min_modality;
    private final String max_modality;

    public String getBaseline_label() {
        return baseline_label;
    }

    public String getMin_modality() {
        return min_modality;
    }

    public String getMax_modality() {
        return max_modality;
    }

    public List<String> getMood() {
        List<String> moods= new ArrayList<String>();
        moods.add(mood.getIndicative());
        moods.add(mood.getImperative());
        moods.add(mood.getConditional());
        moods.add(mood.getSubjunctive());
        return moods;
    }

    public String getPoliteness() {
        return politeness;
    }

    public String getImpoliteness() {
        return impoliteness;
    }

    public String getPos_score() {
        return pos_score;
    }

    public String getNeg_score() {
        return neg_score;
    }

    public String getAffective_label() {
        return affective_label;
    }

    public String getDocument() {
        return document;
    }

    public List<String> getTf_idf() {
        return tf_idf;
    }



   private DatasetRowTFIDF(DatasetRowBuilder builder) {
        this.document= builder.document;
        this.pos_score= builder.pos_score;
        this.neg_score=builder.neg_score;
        this.politeness=builder.politeness;
        this.impoliteness=builder.impoliteness;
        this.min_modality=builder.min_modality;
        this.max_modality=builder.max_modality;
        this.mood=builder.mood;
        this.tf_idf= builder.tf_idf;
        this.affective_label= builder.affective_label;
        this.baseline_label=builder.baseline_label;

	}
	 public static class DatasetRowBuilder {
         private String document;
         private String pos_score;
         private String neg_score;
         private String politeness;
         private String impoliteness;
         private String min_modality;
         private String max_modality;
         private Document.Mood mood;
         private List<String> tf_idf;
         private String affective_label;
         private String baseline_label;


         public DatasetRowBuilder  setMood(Document.Mood mood) {
             this.mood= mood;
             return this;
         }

         public DatasetRowBuilder  setMaxModality(String max_modality) {
             this.max_modality=max_modality;
             return this;
         }
         public DatasetRowBuilder  setMinModality(String min_modality) {
             this.min_modality=min_modality;
             return this;
         }
         public DatasetRowBuilder  setDocument(String document) {
             this.document = document;
             return this;
         }
         public DatasetRowBuilder  setPosScore(String pos_score) {
             this.pos_score=pos_score;
             return this;
         }
         public DatasetRowBuilder  setNegScore(String neg_score) {
             this.neg_score= neg_score;
             return this;
         }
         public DatasetRowBuilder  setPoliteness(String politeness) {
             this.politeness= politeness;
             return this;
         }
         public DatasetRowBuilder  setImpoliteness(String impoliteness) {
             this.impoliteness= impoliteness;
             return this;
         }
         public DatasetRowBuilder  setTf_idf(List<String> tf_idf) {
             this.tf_idf=tf_idf;
             return this;
         }
         public DatasetRowBuilder  setAffectiveLabel(String affective_label) {
             this.affective_label= affective_label;
             return this;
         }
         public DatasetRowBuilder  setBaselineLabel(String baseline_label) {
             this.baseline_label= baseline_label;
             return this;
         }

         public DatasetRowTFIDF build() {

             return new DatasetRowTFIDF(this);
        }
	}
}