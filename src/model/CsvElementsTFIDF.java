package model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Francesco on 28/12/2016.
 */
public class CsvElementsTFIDF {
    private List<String> id;
    private List<String> documents;
    private List<Map<String,Double>> unigramTFIDF;
    private  List<Map<String,Double>>  bigramTFIDF;
    private  List<Map<String,Double>>  positiveTFIDF;
    private  List<Map<String,Double>>  negativeTFIDF;
    private  List<Map<String,Double>>  neutralTFIDF;
    private  List<Map<String,Double>>  ambiguosTFIDF;

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

    public List<Map<String, Double>> getUnigramTFIDF() {
        return unigramTFIDF;
    }

    public void setUnigramTFIDF(List<Map<String, Double>> unigramTFIDF) {
        this.unigramTFIDF = unigramTFIDF;
    }

    public List<Map<String, Double>> getBigramTFIDF() {
        return bigramTFIDF;
    }

    public void setBigramTFIDF(List<Map<String, Double>> bigramTFIDF) {
        this.bigramTFIDF = bigramTFIDF;
    }

    public List<Map<String, Double>> getPositiveTFIDF() {
        return positiveTFIDF;
    }

    public void setPositiveTFIDF(List<Map<String, Double>> positiveTFIDF) {
        this.positiveTFIDF = positiveTFIDF;
    }

    public List<Map<String, Double>> getNegativeTFIDF() {
        return negativeTFIDF;
    }

    public void setNegativeTFIDF(List<Map<String, Double>> negativeTFIDF) {
        this.negativeTFIDF = negativeTFIDF;
    }

    public List<Map<String, Double>> getNeutralTFIDF() {
        return neutralTFIDF;
    }

    public void setNeutralTFIDF(List<Map<String, Double>> neutralTFIDF) {
        this.neutralTFIDF = neutralTFIDF;
    }

    public List<Map<String, Double>> getAmbiguosTFIDF() {
        return ambiguosTFIDF;
    }

    public void setAmbiguosTFIDF(List<Map<String, Double>> ambiguosTFIDF) {
        this.ambiguosTFIDF = ambiguosTFIDF;
    }
}
