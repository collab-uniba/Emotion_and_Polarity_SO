package replacing;

/**
 * Created by Francesco on 23/12/2016.
 */
public class RemoveURL {
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

    public static String cleanUrl(String text) {
        text = text.replace("http:// ", "http://");
        text = text.replace("https:// ", "https://");
        text = text.replace("http://www. ", "http://www.");
        text = text.replace("https://www. ", "https://www.");
        return text;
    }
}
