package utility;

import java.util.*;

public class Utility {
	public LinkedHashMap<String,String> createMap(HashMap<String,List<String>> l){
		LinkedHashMap<String,String> ps= new LinkedHashMap<>();
		for(String s :l.keySet()) {
			ps.put(s,s);
		}
		return ps;
	}

}