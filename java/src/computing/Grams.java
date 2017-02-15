package computing;

import edu.stanford.nlp.util.StringUtils;
import printing.PrintingFile;
import replacing.Removing;

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
    public SortedMap<String, String> importNgrams(String path, int n) throws FileNotFoundException{
        SortedMap<String, String> map = new TreeMap<>();
        File file= new File(path);
        String row;
        int i = 0;
        Scanner scanner = new Scanner(file);
        if (n == 1)
            System.out.println("Loading unigrams..");
        else
            System.out.println("Loading bigrams..");

        while (scanner.hasNextLine()) {
            row = scanner.nextLine();
            String[] ss = row.split("\\s+");
            ss[1] = ss[1].replace("uni","");
            ss[1] = ss[1].replace("bi","");
            map.put(ss[0], ss[1]);
            i++;
        }
        scanner.close();
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
    public SortedMap<String, String> getPositionWordMap(File f,String fileCsv,int j, int n)  {
        SortedMap<String, String> map = new TreeMap<>();
        BufferedReader br = null;
        PrintingFile pr = new PrintingFile();
        Removing rm= new Removing();

        try {
            if (n==1)
              System.out.println("Extracting unigrams..");
            else
                System.out.println("Extracting bigrams..");
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
            //reset id after the map has order by keys
            i=0;
            for(String s: map.keySet()){
                map.put(s,String.valueOf(i));
                i++;
            }
            pr.printNgrams(map, n,fileCsv);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("File: "+ f.getAbsolutePath());
        }
        return map;
    }
}
