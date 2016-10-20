package utils;


public class Utility {

	/*
	 * Remove urls from string
	 */
	public String removeUrl(String s) {
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
	public String removeUserMention(String s) {
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




}
