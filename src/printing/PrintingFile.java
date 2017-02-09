package printing;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import model.DocumentForPoliteness;
import utility.Utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Francesco on 23/12/2016.
 */
public class PrintingFile {
    Utility l= new Utility();
    public void print(String pathOu,List<String> texts){
        File input = new File(pathOu);
        FileWriter fw = null;
        try {
            fw = new FileWriter(input);
            for(String s : texts){
                fw.append(s + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTokenized(String pathIn,String pathOut) {
        File inputTokenized = new File( pathOut);
        FileWriter fw = null;
        try {
            fw = new FileWriter(inputTokenized);
            System.out.println("Tokenizing input corpus ...");
            PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new FileReader(pathIn), new CoreLabelTokenFactory(),
                    "ptb3Escaping=false,untokenizable=allKeep,tokenizeNLs=true");
            while (ptbt.hasNext()) {
                CoreLabel label = ptbt.next();
                String s = String.valueOf(PTBTokenizer.getNewlineToken());
                if (String.valueOf(label).equals(s))
                    fw.append(System.lineSeparator());
                else{
                    if(String.valueOf(label).charAt(0)=='"') {
                        label.setValue("");
                        fw.append(label + " ");
                    }
                    else
                        fw.append(label + " ");
                }

            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void writeDocsValuesOnFile(List<DocumentForPoliteness> docs, String pathOut){
        String header = "# -*- coding: utf-8 -*- \nTEST_DOCUMENTS = [ \n  # Polite requests \n";
        int totDocs=0;
        try {
            FileWriter out = new FileWriter(pathOut);
            out.append(header);
            for(DocumentForPoliteness d : docs){
                out.append("{ \n ");
                String text= "\"text\": "+ "\""+d.getText()+" \"" + ",\n ";
                out.append(text);

                text= "\"sentences\": [ \n   ";
                out.append(text);
                if(d.getSentences().size()==0){
                    out.append(" ], \n");
                }
                for(int i=0;i<d.getSentences().size();i++){
                        if(i+1==d.getSentences().size()){
                            text="\" "+ d.getSentences().get(i) + " \""+ "\n  ],\n ";

                        }
                        else
                            text="\" "+ d.getSentences().get(i) + " \"" +",\n   ";
                        out.append(text);
                    }
                text= "\"parses\": [ \n   ";
                out.append(text);
                if(d.getParse().size()==0){
                    out.append(" ] \n");
                }
                for(int i=0;i<d.getParse().size();i++){
                    if(i+1==d.getParse().size()){
                        text= d.getParse().get(i)+ "\n  ]\n ";
                    }
                    else
                        text= d.getParse().get(i) +",\n   ";
                    out.append(text);
                }
                if(totDocs+1==docs.size())
                    out.append(" }\n] ");
                else
                    out.append("}, \n ");
                totDocs++;
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void printIDF(Map<String,Double> idfs,String pathOut){
        List<String> idf= new ArrayList<>();
        for(String k : idfs.keySet()){
            String finale = k + " " + idfs.get(k)+ "\n";
            idf.add(finale);
        }
        print(pathOut,idf);
    }



    /**
     * Print "grram-integer" the integer is incremented during the map reading.
     * * * @param map grams
     * @param n
     */
    public void printNgrams(SortedMap<String,String> map, int n,String path) {
        String file = "";
        String t = "";
        //Create underDirectory
        l.directoryCreator(path+"/Dictionary");

        switch (n){
            case 1:
                file = path+"/Dictionary/"+"UnigramsList.txt";
                t = "uni";
                break;
            case 2:
                file = path+"/Dictionary/"+"BigramsList.txt";
                t = "bi";
                break;
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(file));
            int i = 0;
            for (String s: map.keySet()) {
                fw.append(s + "\t" + t + i);
                fw.append(System.lineSeparator());
                i++;
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
