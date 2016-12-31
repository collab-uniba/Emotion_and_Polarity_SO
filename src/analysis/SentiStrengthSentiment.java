package analysis;

import uk.ac.wlv.sentistrength.SentiStrength;

import java.io.*;

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
	
	public String SentiStrengthgetScore(String text){
		SentiStrength sentiStrength = new SentiStrength();
		String ssthInitialisation[] = {"sentidata", "src/analysis/sentistrength/lib/SentiStrength_Data/", "explain"};
		sentiStrength.initialise(ssthInitialisation);
		String score = sentiStrength.computeSentimentScores(text);
		String[] split = score.split("\\s+");


		return text + ";" + split[0] + ";" + split[1] + ";" + score;
	}

}

