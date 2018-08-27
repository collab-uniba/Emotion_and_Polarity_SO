package analysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import model.DocumentForPoliteness;
import reading.ReadingFile;
import edu.stanford.nlp.parser.nndep.DependencyParser;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Politeness implements Serializable{

    final static Properties props;
    final static StanfordCoreNLP pipeline;
    final static DependencyParser dp;
    static {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("threads", "8");
        pipeline = new StanfordCoreNLP(props);
        dp = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL);
    }

    public static List<DocumentForPoliteness> createFormatForInput(String pathIn) {

        List<DocumentForPoliteness> docsValues = new ArrayList<>();

        try {
            ReadingFile rd= new ReadingFile();
            List<String> docs = rd.read(pathIn);

            docsValues = docs.parallelStream()
                    .map(doc -> {
                        DocumentForPoliteness dcVal = new DocumentForPoliteness();
                        List<String> sentencesDoc = new ArrayList<>();
                        List<String> dependencyParse = new ArrayList<>();

                        if(!doc.isEmpty()) {
                            // create an empty Annotation just with the given text
                            Annotation document = new Annotation(doc);
                            // run all Annotators on this text

                            pipeline.annotate(document);

                            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

                            for (CoreMap sentence : sentences) {
                                sentencesDoc.add(sentence.toString());
                                String prediction = dp.predict(sentence).toString();
                                dependencyParse.add(elaborateString(prediction));
                            }
                        }
                        dcVal.setSentences(sentencesDoc);
                        dcVal.setParse(dependencyParse);
                        dcVal.setText(doc);

                        return dcVal;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return docsValues;
    }

    private static String elaborateString(String prediction){
        String[] ss= prediction.split("\n");
        String finale="";
        String st= ss[1];
        ss= st.split(",(?![^()]*+\\))");
        String quad= "[";
        String quadF="]";


        if(ss.length==1){
            ss[0]=ss[0].replace("[","");
            ss[0]=ss[0].replace("]","");
            ss[0]=quad+"\""+ss[0]+"\""+quadF;
        }
        else {
            ss[0]=ss[0].replace("[","");
            ss[0]=quad+"\""+ss[0]+"\"";
            ss[ss.length - 1] = ss[ss.length - 1].replace("]", "");
            ss[ss.length - 1] = ss[ss.length - 1].replace(" ", "");
            ss[ss.length - 1] = ",\"" + ss[ss.length - 1] + "\"" + quadF;
            for (int i = 1; i < ss.length - 1; i++) {
                ss[i] = ss[i].replace(" ", "");
                ss[i] = "," + "\"" + ss[i] + "\"";
            }
        }
        for (String s : ss) {
            finale += s;
        }
        return finale;
    }

}
