package utility;


import org.apache.commons.validator.routines.UrlValidator;

public class Utility {
	/*
	 * Remove urls from string
	 */
	public String removeUrl(String s) {
		String res = "";
		String[] ss = s.split("\\s+");
		String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
		for (String string : ss) {
			UrlValidator urlValidator = new UrlValidator(schemes);
			if (!urlValidator.isValid(string)) {
				String[] xmls= string.split(":");
				if(xmls.length>0 && (xmls[0].equals("xmls") || xmls[0].equals("xmlns"))) {
					String[] xl = string.split("'");
					if(xl.length>1){
						String[] sm = xl[1].split(":");
						if (sm.length>1 && !(sm[0].equals("http") || sm[0].equals("https")))
							res += string + " ";}
				}
				else {
					String[] sm = string.split(":");
					if (sm.length > 0 && !(sm[0].equals("http") || sm[0].equals("https")))
						res += string + " ";
				}

			}
		}
		if (res.length() > 0)
			res = res.substring(0, res.length() - 1);
		return res;
	}
}
