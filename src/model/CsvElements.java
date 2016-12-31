package model;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Francesco on 28/12/2016.
 */
public class CsvElements {
    private List<String> id;
    private List<String> documents;
    private List<LinkedHashMap<String,Double>> unigramTFIDF;
    private  List<LinkedHashMap<String,Double>>  bigramTFIDF;
    private  List<LinkedHashMap<String,Double>>  positiveTFIDF;
    private  List<LinkedHashMap<String,Double>>  negativeTFIDF;
    private  List<LinkedHashMap<String,Double>>  neutralTFIDF;
    private  List<LinkedHashMap<String,Double>>  ambiguosTFIDF;

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public List<LinkedHashMap<String, Double>> getUnigramTFIDF() {
        return unigramTFIDF;
    }

    public void setUnigramTFIDF(List<LinkedHashMap<String, Double>> unigramTFIDF) {
        this.unigramTFIDF = unigramTFIDF;
    }

    public List<LinkedHashMap<String, Double>> getBigramTFIDF() {
        return bigramTFIDF;
    }

    public void setBigramTFIDF(List<LinkedHashMap<String, Double>> bigramTFIDF) {
        this.bigramTFIDF = bigramTFIDF;
    }

    public List<LinkedHashMap<String, Double>> getPositiveTFIDF() {
        return positiveTFIDF;
    }

    public void setPositiveTFIDF(List<LinkedHashMap<String, Double>> positiveTFIDF) {
        this.positiveTFIDF = positiveTFIDF;
    }

    public List<LinkedHashMap<String, Double>> getNegativeTFIDF() {
        return negativeTFIDF;
    }

    public void setNegativeTFIDF(List<LinkedHashMap<String, Double>> negativeTFIDF) {
        this.negativeTFIDF = negativeTFIDF;
    }

    public List<LinkedHashMap<String, Double>> getNeutralTFIDF() {
        return neutralTFIDF;
    }

    public void setNeutralTFIDF(List<LinkedHashMap<String, Double>> neutralTFIDF) {
        this.neutralTFIDF = neutralTFIDF;
    }

    public List<LinkedHashMap<String, Double>> getAmbiguosTFIDF() {
        return ambiguosTFIDF;
    }

    public void setAmbiguosTFIDF(List<LinkedHashMap<String, Double>> ambiguosTFIDF) {
        this.ambiguosTFIDF = ambiguosTFIDF;
    }
}
