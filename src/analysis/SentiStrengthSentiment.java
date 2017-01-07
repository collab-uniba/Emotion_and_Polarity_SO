package analysis;

import uk.ac.wlv.sentistrength.SentiStrength;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SentiStrengthSentiment {

	public static void main(String[] args) throws IOException {
		FileWriter fw = null;
		BufferedReader br = null;
		try {
		SentiStrengthSentiment ss = new SentiStrengthSentiment();
		br  = new BufferedReader(new FileReader(args[0]));
		fw = new FileWriter(new File(args[1]));
		String line;

			while((line = br.readLine()) != null){
				if(line.length()>0){
					fw.append(ss.SentiStrengthgetScore(line));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fw.flush();
			fw.close();
			br.close();
		}
	}

	public SentiStrengthSentiment() {
		super();
	}

	/**
	 *
	 * @param docs
	 * @param  ScoreType : 1 (negative ) , 0 (positive)
	 * @return
	 */
	public Map<String,Double> SentiStrengthgetScoreForAllDocs(List<String> docs,int ScoreType){
		List<Map<String,Double>> l= new ArrayList<>();
		Map<String,Double> docScore = new LinkedHashMap<>();
		for(String doc : docs){
			String result=SentiStrengthgetScore(doc);
			String[] split = result.split(";");
			docScore.put(doc,Double.valueOf(split[ScoreType]));
		}
		return docScore;
	}

	public String SentiStrengthgetScore(String text){
		SentiStrength sentiStrength = new SentiStrength();
		String ssthInitialisation[] = {"sentidata", "src/analysis/lib/SentiStrength_Data/", "explain"};
		sentiStrength.initialise(ssthInitialisation);
		String score = sentiStrength.computeSentimentScores(text);
		String[] split = score.split("\\s+");

		System.out.println(text +  split[0] +    split[1]);
		return  split[0] + ";" + split[1];
	}

}

