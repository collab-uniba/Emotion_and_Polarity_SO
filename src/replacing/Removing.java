package replacing;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francesco on 23/12/2016.
 */
public class Removing {
    /*
         * Remove urls from string
         */
    public String removeUrlOne(String s) {
        String res = "";
        String[] ss = s.split("\\s+");
        for (String string : ss) {
            if (!Patterns.WEB_URL.matcher(string).matches()) {
                res += string + " ";
            }
        }
        if (res.length() > 0)
            res = res.substring(0, res.length() - 1);
        return res;
    }



    /*
	 * Remove urls from string
	 */
    public String removeUrlTwo(String s) {
        String res = "";
        String[] ss = s.split("\\s+");
        String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
        for (String string : ss) {
            UrlValidator urlValidator = new UrlValidator(schemes);
            if (!urlValidator.isValid(string)) {
                String[] xmls= string.split(":");
                if(xmls.length>0 && (xmls[0].equals("xmls") || xmls[0].equals("xmlns"))) {
                    String[] xl = string.split("'");
                    if(xl.length>1){
                        String[] sm = xl[1].split(":");
                        if (sm.length>1 && !(sm[0].equals("http") || sm[0].equals("https")))
                            res += string + " ";}
                }
                else {
                    String[] sm = string.split(":");
                    if (sm.length > 0 && !(sm[0].equals("http") || sm[0].equals("https")))
                        res += string + " ";
                }

            }
        }
        if (res.length() > 0)
            res = res.substring(0, res.length() - 1);
        return res;
    }


    public List<String> removeUrlOne(List<String> doc){
        List<String> docsWithoutURL= new ArrayList<String>();
        for(String s : doc){
            docsWithoutURL.add(removeUrlOne(s));
        }
        return docsWithoutURL;
    }

    public List<String> removeUrlTwo(List<String> doc){
        List<String> docsWithoutURL= new ArrayList<String>();
        for(String s : doc){
            docsWithoutURL.add(removeUrlTwo(s));
        }
        return docsWithoutURL;
    }


    private String removeQuotesSpecialString(String doc, String specialString){
        String[] ss = doc.split("\\s+");
        String res="";
        for(int i=0 ; i<ss.length;i++){
            if(ss[i].equals(specialString)){
                ss[i]=ss[i].replaceAll("\"","'");
            }
            res+=" " + ss[i];
        }
        return res;
    }

    public List<String> removeQuotesSpecialString(List<String> docs,String toRemove){
        List<String> docsWithoutSpString= new ArrayList<>();
        for(String doc:docs){
           docsWithoutSpString.add(removeQuotesSpecialString(doc,toRemove));
        }
        return docsWithoutSpString;
    }

}
