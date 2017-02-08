package utility;

import java.io.File;
import java.util.*;

public class Utility {
	public Map<String,String> createMap(Map<String,List<String>> l){
		LinkedHashMap<String,String> ps= new LinkedHashMap<>();
		for(String s :l.keySet()) {
			ps.put(s,s);
		}
		return ps;
	}

	public boolean directoryExists(String directoryName){
		File theDir = new File(directoryName);
		return theDir.exists();
	}
	public void directoryCreator(String directoryName){
		File theDir = new File(directoryName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + directoryName);
			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(SecurityException se){
				//handle it
			}
			if(result) {
				System.out.println("DIR created");
			}
		}
	}

}