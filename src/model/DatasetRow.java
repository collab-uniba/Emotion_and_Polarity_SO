package model;

import java.util.List;

/**
 * Class for printing in .csv files
 */
public class DatasetRow {

	//private  final String id;
	private  final String document;
    private  final List<Double> tf_idf;


    public String getDocument() {
        return document;
    }

    public List<Double> getTf_idf() {
        return tf_idf;
    }


   private DatasetRow(DatasetRowBuilder builder) {
        this.document= builder.document;
        this.tf_idf= builder.tf_idf;

	}
	 public static class DatasetRowBuilder {
         private   String document;
         private List<Double> tf_idf;

         public DatasetRowBuilder  setDocument(String document) {
             this.document = document;
             return this;
         }
         public DatasetRowBuilder  setTf_idf(List<Double> tf_idf) {
             this.tf_idf=tf_idf;
             return this;
         }
         public DatasetRow build() {
            return new DatasetRow(this);
        }
	}
}