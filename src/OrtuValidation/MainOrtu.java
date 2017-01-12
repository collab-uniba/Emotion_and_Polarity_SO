package OrtuValidation;

import OrtuValidation.computing.Ortu;
import OrtuValidation.model.DocumentOrtu;
import printing.WriterCSV;
import reading.ReadingFile;
import tokenizer.TokenizeCorpus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


/**
 * Created by Francesco on 03/01/2017.
 */
public class MainOrtu {

    public static void main(String[] args) throws IOException {
        String FORMAT = ".csv";


        List<DocumentOrtu> documentsEmotion = new ArrayList<>();
        List<DocumentOrtu> documentsPolarity = new ArrayList<>();
        TreeSet<DocumentOrtu> tr = new TreeSet<DocumentOrtu>();


        String[] HEADERS = {"id", "love", "joy", "surprise", "anger", "sadness", "fear", "label", "comment"};
        String[] HEADERS2 = {"id", "comment", "label"};
        String[] HEADERS3 = {"comment"};
        String[] HEADERS4 = {"comment", "label"};

        WriterCSV wr = new WriterCSV();
        Ortu ortu= new Ortu();


        //GROUP 1
        List<DocumentOrtu> documentGroup1= new ArrayList<>();
        ortu.bygroups("group1",args[0],',',documentGroup1);

        //label for each rater
        wr.writeCsvFileTwo("outOrtu/group1/group1_finalLabelForEachRater" + FORMAT, documentGroup1, HEADERS, true, ',', false);


        //final label for each comment based on emotion
        documentGroup1.clear();//deve ripopolare i documents del gruppo 1 senza duplicati
        ortu.bygroups("group1_noDuplied",  args[0],',', documentGroup1);
        wr.writeCsvFileTwo("outOrtu/group1/group1_finalLabelForEachComment_MajAgOnEmotionsWithoutMixed" + FORMAT, documentGroup1, HEADERS, true, ',',true );
        wr.writeCsvFileTwo("outOrtu/group1/group1_finalLabelForEachComment_MajAgOnEmotionsWithMixed" + FORMAT, documentGroup1, HEADERS, true, ',',false );
        documentsEmotion.addAll(documentGroup1);

        //final label for each comment based on polarity
        documentGroup1.clear();
        ortu.bygroups("group1_noDuplied_MajAgOnCommentPolarity",  "outOrtu/group1/group1_finalLabelForEachRater" + FORMAT, ',',documentGroup1);
        wr.writeCsvFileTwo( "outOrtu/group1/group1_finalLabelForEachComment_MajAgOnPolarityWithoutMixed" + FORMAT, documentGroup1, HEADERS2, true, ',', true);
        wr.writeCsvFileTwo("outOrtu/group1/group1_finalLabelForEachComment_MajAgOnPolarityWithMixed" + FORMAT, documentGroup1, HEADERS2, true, ',',false);
        documentsPolarity.addAll(documentGroup1);



        ///****GROUP 2***///
        List<DocumentOrtu> documentGroup2 = new ArrayList<>();
        //Gruppo 2
        for (int i = 1; i < 4; i++){
            List<DocumentOrtu> documentPartial2= new ArrayList<>();
            ortu.bygroups("group2", args[i],',',documentPartial2);
            wr.writeCsvFileTwo("outOrtu/group2/group2_" + i + FORMAT, documentPartial2, HEADERS, true, ',', false);
            documentGroup2.addAll(documentPartial2);
        }

        //ordino gli elementi
        tr.addAll(documentGroup2);
        documentGroup2.clear();
        documentGroup2.addAll(tr);
        wr.writeCsvFileTwo("outOrtu/group2/group2_merged_finalLabelForEachRater" + FORMAT, documentGroup2, HEADERS, true, ',', false);

        //final label for each comment based on emotion
        documentGroup2.clear();
        ortu.bygroups("group2_noDuplied", "outOrtu/group2/group2_merged_finalLabelForEachRater"+FORMAT,',',documentGroup2);
        wr.writeCsvFileTwo( "outOrtu/group2/group2_merged_finalLabelForEachComment_MajAgOnEmotionsWithMixed" + FORMAT, documentGroup2, HEADERS, true, ',', false);
        wr.writeCsvFileTwo( "outOrtu/group2/group2_merged_finalLabelForEachComment_MajAgOnEmotionsWithoutMixed" + FORMAT, documentGroup2, HEADERS, true, ',', true);
        documentsEmotion.addAll(documentGroup2);


        //final label for each comment based on polarity
        documentGroup2.clear();
        ortu.bygroups("group2_noDuplied_MajAgOnCommentPolarity","outOrtu/group2/group2_merged_finalLabelForEachRater" + FORMAT,',', documentGroup2);
        wr.writeCsvFileTwo("outOrtu/group2/group2_merged_finalLabelForEachComment_MajAgOnPolarityWithMixed" + FORMAT, documentGroup2, HEADERS2, true, ',', false);
        wr.writeCsvFileTwo("outOrtu/group2/group2_merged_finalLabelForEachComment_MajAgOnPolarityWithoutMixed" + FORMAT, documentGroup2, HEADERS2, true, ',', true);
        documentsPolarity.addAll(documentGroup2);
        ///****END GROUP 2***///

        ///****GROUP 3***///
        List<DocumentOrtu> documentGroup3 = new ArrayList<>();
        int f=1;
        for (int i = 4; i < 8; i++){
            List<DocumentOrtu> documentPartialGroup3= new ArrayList<>();
            ortu.bygroups("group3", args[i],',', documentPartialGroup3);
            wr.writeCsvFileTwo( "outOrtu/group3/group3_" + f + FORMAT, documentPartialGroup3, HEADERS, true, ',', false);
            f++;
            documentGroup3.addAll(documentPartialGroup3);
        }
        wr.writeCsvFileTwo("outOrtu/group3/group3_merged__finalLabelForEachComment"+ FORMAT, documentGroup3, HEADERS, true, ',', false);
        documentsEmotion.addAll(documentGroup3);
        documentsPolarity.addAll(documentGroup3);
        ///****GROUP 3***///


        //writing total complete Group1, 2 and 3 .csv
        wr.writeCsvFileTwo("outOrtu/others/OrtuEtAl_merged_labelBasedOnEmotion_NoMixed" + FORMAT, documentsEmotion, HEADERS2, true, ';', true);
        wr.writeCsvFileTwo( "outOrtu/others/OrtuEtAl_ForSenti4SD_merged_labelBasedOnEmotion_NoMixed" + FORMAT, documentsEmotion, HEADERS3, false, ';', true);
        wr.writeCsvFileTwo("outOrtu/others/OrtuEtAl_merged_labelBasedOnPolarity_NoMixed" + FORMAT, documentsPolarity, HEADERS2, true, ';', true);
        wr.writeCsvFileTwo("outOrtu/others/OrtuEtAl_ForSenti4SD_merged_labelBasedOnPolarity_NoMixed" + FORMAT, documentsPolarity, HEADERS3, false, ';', true);

        //QUI Scrivo il tokenized
        TokenizeCorpus tk= new TokenizeCorpus();
        tk.tokenizerByToken("outOrtu/others/OrtuEtAl_ForSenti4SD_merged_labelBasedOnEmotion_NoMixed"+FORMAT,"outOrtu/others/OrtuEtAl_TOKENIZED");


        List<DocumentOrtu> onlyComments_withoutURL = new ArrayList<>();
        ReadingFile rd= new ReadingFile();
        //Qui riapro il tokenized
        onlyComments_withoutURL.addAll(rd.read_Tokenized_And_remove_Url( "outOrtu/others/OrtuEtAl_TOKENIZED"));
        int i = 0;
        System.out.println("doc final " + documentsEmotion.size());
        System.out.println(" " + onlyComments_withoutURL.size());
        for (int j = 0; j < documentsEmotion.size() - 1; j++) {
            if (i < onlyComments_withoutURL.size() - 1) {
                DocumentOrtu d1 = onlyComments_withoutURL.get(i);
                documentsEmotion.get(j).setComment(d1.getComment());
                i++;
            }
        }
        wr.writeCsvFileTwo("outOrtu/others/OrtuEtAl_ForSenti4SD_merged_labelBasedOnEmotion_WithoutURL_NoMixed" + FORMAT, documentsEmotion, HEADERS4, false, ';', true);


        System.out.println("doc final Comment Polarity " + documentsPolarity.size());
        System.out.println(" " + onlyComments_withoutURL.size());
        for (int j = 0; j < documentsPolarity.size() - 1; j++) {
            if (i < onlyComments_withoutURL.size() - 1) {
                DocumentOrtu d1 = onlyComments_withoutURL.get(i);
                documentsPolarity.get(j).setComment(d1.getComment());
                i++;
            }
        }
        wr.writeCsvFileTwo("outOrtu/others/OrtuEtAl_ForSenti4SD_merged_labelBasedOnPolarity_WithoutURL_NoMixed" + FORMAT, documentsPolarity, HEADERS4, false, ';', true);
    }

}
