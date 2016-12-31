package computing;

import edu.stanford.nlp.util.StringUtils;
import replacing.Patterns;
import utility.Utility;

import java.io.*;
import java.util.*;

/**
 * Created by Francesco on 31/12/2016.
 */
public class Grams{
    /*
 * Returns a collection of n-grams from an input string
 */
     Collection<String> getNgrams(String input, int n){
        Collection<String> ss = StringUtils.getNgrams(new ArrayList<>(Arrays.asList(input.split("\\s+"))), n, n);
        if(n>1){
            Iterator<String> iter = ss.iterator();
            ss = new ArrayList<>();
            while(iter.hasNext())
                ss.add(iter.next().replace(" ", "_"));
        }
        return ss;
    }

    /*
	 * Return the map of goldstandard's ngrams with an incremental integer
	 */
    public SortedMap<String, String> importNgrams(InputStream is)
            throws IOException {
        SortedMap<String, String> map = new TreeMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String row;
        int i = 0;
        while ((row = br.readLine()) != null) {
            line = row.split("\\s+")[0];
            map.put(line, String.valueOf(i));
            i++;
        }
        br.close();
        return map;
    }

    /*
     * Remove urls from string
     */
    private String removeUrl(String s) {
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
     * Remove user mention from a string
     */
    private String removeUserMention(String s) {
        String res = "";
        if (s.length() > 0) {
            String[] ss = s.split("\\s+");
            for (String string : ss) {
                if (!(string.charAt(0) == '@')) {
                    res += string + " ";
                }
            }
            if (res.length() > 0)
                res = res.substring(0, res.length() - 1);
        }
        return res;

    }

    /*
     * Create a file containing a list of n-grams
     */
    private void printNgrams(SortedMap<String, Integer> map, int n) throws IOException {
        String file = "";
        String t = "";
        switch (n){
            case 1:
                file = "./UnigramsList";
                t = "uni";
                break;
            case 2:
                file = "./BigramsList";
                t = "bi";
                break;
        }
        FileWriter fw = new FileWriter(new File(file));
        int i = 1;
        for (String s: map.keySet()) {
            fw.append(s + "\t" + t + i);
            fw.append(System.lineSeparator());
            i++;
        }
        fw.flush();
        fw.close();
    }



    public SortedMap<String, Integer> getPositionWordMap(File f, int j, int n) throws IOException {
        SortedMap<String, Integer> map = new TreeMap<>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        String row;
        int i = 0;
        Collection<String> ss;
        while ((row = br.readLine()) != null) {
            line = row.split(";")[j];
            line = line.toLowerCase();
            line = removeUrl(line);
            line = removeUserMention(line);
            if (line.length() > 0) {
                ss = getNgrams(line, n);
                for (String string : ss) {
                    if (!(string.equals("?") || string.equals("!")))
                        if (!map.containsValue(string)) {
                            map.put(string, i);
                            i++;
                        }}}}
        printNgrams(map, n);
        br.close();
        return map;
    }
}
