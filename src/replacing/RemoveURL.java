package replacing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francesco on 23/12/2016.
 */
public class RemoveURL {
    /*
         * Remove urls from string
         */
    public String removeUrl(String s) {
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

    public List<String> removeUrl(List<String> doc){
        List<String> docsWithoutURL= new ArrayList<String>();
        for(String s : doc){
           docsWithoutURL.add(removeUrl(s));
        }
        return docsWithoutURL;
    }

}
