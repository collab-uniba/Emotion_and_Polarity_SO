package computing;

import edu.stanford.nlp.util.StringUtils;
import printing.PrintingFile;
import replacing.Patterns;
import replacing.Removing;
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

    /**
     *  Return the map of goldstandard's ngrams with an incremental integer converted in string.
     *
	 */
    public SortedMap<String, String> importNgrams(String path)
            throws IOException {
        SortedMap<String, String> map = new TreeMap<>();

       // InputStream is = .getResourceAsStream("res/UnigramsList");
        File file= new File(path);
       // BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String row;
        StringBuilder result = new StringBuilder("");
        int i = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                row = scanner.nextLine();
                line = row.split("\\s+")[0];
                map.put(line, String.valueOf(i));
                i++;
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
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
    public SortedMap<String, String> getPositionWordMap(File f, int j, int n)  {
        SortedMap<String, String> map = new TreeMap<>();
        BufferedReader br = null;
        PrintingFile pr = new PrintingFile();
        Removing rm= new Removing();
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            String row;
            int i = 0;
            Collection<String> ss;
            while ((row = br.readLine()) != null) {
                line = row.split(";")[j];
                line = line.toLowerCase();
                line = rm.removeUrlOne(line);
                line = rm.removeUserMention(line);
                if (line.length() > 0) {
                    ss = getNgrams(line, n);
                    for (String string : ss) {
                        if (!(string.equals("?") || string.equals("!")))
                            if (!map.containsValue(string)) {
                                map.put(string,String.valueOf(i));
                                i++;
                            }}}}
            pr.printNgrams(map, n);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("File not found or reading problem");
        }
        return map;
    }
}
