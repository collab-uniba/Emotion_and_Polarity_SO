package computing;

import edu.stanford.nlp.util.StringUtils;
import replacing.Patterns;

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

    /**
     *  Return the map of goldstandard's ngrams with an incremental integer converted in string.
     *
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

    /**
     * Print "grram-integer" the integer is incremented during the map reading.
     * * * @param map grams
     * @param n
     */
    private void printNgrams(SortedMap<String, Integer> map, int n) {
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
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(file));
            int i = 1;
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


    /**
     * Extract  unigrams or bigrams , for example,like in our case ordered by key,
     * the integer associated indicates when the key was putted into the map.
     * @param f file where are the texts from which mine the grams
     * @param j
     * @param n indicates the grams'Number. e.g unigrams -> n=1 , bigrams -> n=2
     * @return Unigrams and its id. <Ngram , ID>
     * @throws IOException  file not found or reading problem
     * */
    public SortedMap<String, Integer> getPositionWordMap(File f, int j, int n)  {
        SortedMap<String, Integer> map = new TreeMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
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
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("File not found or reading problem");
        }
        return map;
    }
}
