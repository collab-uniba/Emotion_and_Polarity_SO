package model;

import java.util.List;

/**
 * Class for printing in .csv files
 */
public class DatasetRowTFIDF {

	//private  final String id;
	private  final String document;
    private final Double pos_score;
    private final Double neg_score;
    private final Double politeness;
    private final Double impoliteness;
    private  final List<Double> tf_idf;
    private final String affective_label;

    public Double getPoliteness() {
        return politeness;
    }

    public Double getImpoliteness() {
        return impoliteness;
    }

    public Double getPos_score() {
        return pos_score;
    }

    public Double getNeg_score() {
        return neg_score;
    }

    public String getAffective_label() {
        return affective_label;
    }

    public String getDocument() {
        return document;
    }

    public List<Double> getTf_idf() {
        return tf_idf;
    }


   private DatasetRowTFIDF(DatasetRowBuilder builder) {
        this.document= builder.document;
        this.pos_score= builder.pos_score;
        this.neg_score=builder.neg_score;
        this.politeness=builder.politeness;
        this.impoliteness=builder.impoliteness;
        this.tf_idf= builder.tf_idf;
        this.affective_label= builder.affective_label;

	}
	 public static class DatasetRowBuilder {
         private  String document;
         private  Double pos_score;
         private  Double neg_score;
         private Double politeness;
         private Double impoliteness;
         private  List<Double> tf_idf;
         private  String affective_label;


         public DatasetRowBuilder  setDocument(String document) {
             this.document = document;
             return this;
         }
         public DatasetRowBuilder  setPosScore(Double pos_score) {
             this.pos_score=pos_score;
             return this;
         }
         public DatasetRowBuilder  setNegScore(Double neg_score) {
             this.neg_score= neg_score;
             return this;
         }
         public DatasetRowBuilder  setPoliteness(Double politeness) {
             this.politeness= politeness;
             return this;
         }
         public DatasetRowBuilder  setImpoliteness(Double impoliteness) {
             this.impoliteness= impoliteness;
             return this;
         }
         public DatasetRowBuilder  setTf_idf(List<Double> tf_idf) {
             this.tf_idf=tf_idf;
             return this;
         }
         public DatasetRowBuilder  setAffectiveLabel(String affective_label) {
             this.affective_label= affective_label;
             return this;
         }

         public DatasetRowTFIDF build() {
            return new DatasetRowTFIDF(this);
        }
	}
}