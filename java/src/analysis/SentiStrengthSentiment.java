package analysis;

import model.Document;
import uk.ac.wlv.sentistrength.SentiStrength;

import java.io.*;
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
	public Map<String,Document> SentiStrengthgetScoreForAllDocs(Map<String, Document> docs, int ScoreType){
		Document d= null;
		for(String id:docs.keySet()){
			d=docs.get(id);
			String result=SentiStrengthgetScore(d.getText());
			String[] split = result.split(";");
			if(ScoreType==0)
				d.setPos_score(Double.valueOf(split[0]));
			else
				d.setNeg_score(Double.valueOf(split[1]));
		}
		return docs;
	}

	private String SentiStrengthgetScore(String text){
		SentiStrength sentiStrength = new SentiStrength();
		String ssthInitialisation[] = {"sentidata", "java/res/SentiStrength_Data/", "explain"};
		sentiStrength.initialise(ssthInitialisation);
		String score = sentiStrength.computeSentimentScores(text);
		String[] split = score.split("\\s+");

		return  split[0] + ";" + split[1];
	}

}

