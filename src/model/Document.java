package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Francesco on 12/01/2017.
 */
public class Document {

    private String id;
    private String text;
    private String textReplaced;
    private double pos_score;
    private double neg_score;
    private double politeness;
    private double impoliteness;
    private Mood m;
    private double min_modality;
    private double max_modality;
    private Map<String,Double> unigramTFIDF;
    private Map<String,Double>  bigramTFIDF;
    private Map<String,Double>  positiveTFIDF;
    private Map<String,Double>  negativeTFIDF;
    private Map<String,Double>  neutralTFIDF;
    private Map<String,Double>  ambiguosTFIDF;
    private String label;
    private String labelBaseline;

    public String getLabelBaseline() {
        return labelBaseline;
    }

    public void setLabelBaseline(String labelBaseline) {
        this.labelBaseline = labelBaseline;
    }

    public Mood getMood() {
        return m;
    }

    public void setMood(Mood m) {
        this.m = m;
    }

    public double getMin_modality() {
        return min_modality;
    }

    public void setMin_modality(double min_modality) {
        this.min_modality = min_modality;
    }

    public double getMax_modality() {
        return max_modality;
    }

    public void setMax_modality(double max_modality) {
        this.max_modality = max_modality;
    }

    public String getTextReplaced() {
        return textReplaced;
    }

    public void setTextReplaced(String textReplaced) {
        this.textReplaced = textReplaced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPos_score() {
        return pos_score;
    }

    public void setPos_score(double pos_score) {
        this.pos_score = pos_score;
    }

    public double getNeg_score() {
        return neg_score;
    }

    public void setNeg_score(double neg_score) {
        this.neg_score = neg_score;
    }

    public double getPoliteness() {
        return politeness;
    }

    public void setPoliteness(double politeness) {
        this.politeness = politeness;
    }

    public double getImpoliteness() {
        return impoliteness;
    }

    public void setImpoliteness(double impoliteness) {
        this.impoliteness = impoliteness;
    }

    public Map<String, Double> getUnigramTFIDF() {
        return unigramTFIDF;
    }

    public void setUnigramTFIDF(Map<String, Double> unigramTFIDF) {
        this.unigramTFIDF = unigramTFIDF;
    }

    public Map<String, Double> getBigramTFIDF() {
        return bigramTFIDF;
    }

    public void setBigramTFIDF(Map<String, Double> bigramTFIDF) {
        this.bigramTFIDF = bigramTFIDF;
    }

    public Map<String, Double> getPositiveTFIDF() {
        return positiveTFIDF;
    }

    public void setPositiveTFIDF(Map<String, Double> positiveTFIDF) {
        this.positiveTFIDF = positiveTFIDF;
    }

    public Map<String, Double> getNegativeTFIDF() {
        return negativeTFIDF;
    }

    public void setNegativeTFIDF(Map<String, Double> negativeTFIDF) {
        this.negativeTFIDF = negativeTFIDF;
    }

    public Map<String, Double> getNeutralTFIDF() {
        return neutralTFIDF;
    }

    public void setNeutralTFIDF(Map<String, Double> neutralTFIDF) {
        this.neutralTFIDF = neutralTFIDF;
    }

    public Map<String, Double> getAmbiguosTFIDF() {
        return ambiguosTFIDF;
    }

    public void setAmbiguosTFIDF(Map<String, Double> ambiguosTFIDF) {
        this.ambiguosTFIDF = ambiguosTFIDF;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public static class Mood{
        private String conditional;
        private String imperative;
        private String subjunctive;
        private String indicative;

        public Mood(String conditional, String imperative, String subjunctive, String indicative) {
            this.conditional = conditional;
            this.imperative = imperative;
            this.subjunctive = subjunctive;
            this.indicative = indicative;
        }

        protected String getConditional() {
            return conditional;
        }

        protected void setConditional(String conditional) {
            this.conditional = conditional;
        }

        protected String getImperative() {
            return imperative;
        }

        protected void setImperative(String imperative) {
            this.imperative = imperative;
        }

        protected String getSubjunctive() {
            return subjunctive;
        }

        protected void setSubjunctive(String subjunctive) {
            this.subjunctive = subjunctive;
        }

        protected String getIndicative() {
            return indicative;
        }

        protected void setIndicative(String indicative) {
            this.indicative = indicative;
        }
    }
}
