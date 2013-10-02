package connexus;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;

public class Config {
	public static final String productName = "Connex.us";
	public static final String productDomain = "connexus-apt.appspot.com";
	public static final String productURL = "http://" + productDomain;

	public static final String productAdminEmail = "planders@gmail.com";
	
    public static final long siteId = 1;
    
    public static final String emailSubject = "[APT-Preston Landers] Trending streams for " + productDomain;
    
    // Maximum search results to show
    public static final int maxSearchResults = 5;

    public static int getMaxSearchResults() {
    	return maxSearchResults;
    }
    
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    
	public static String getURIWithParams(String uri, List<String[]> params) {
		URIBuilder builder;
		try {
			builder = new URIBuilder(uri);
			for (String[] paramKV: params) {
				if (paramKV[1].length() > 0) {
					builder.setParameter(paramKV[0], paramKV[1]);
				}
			}
			
			return builder.toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
		return uri;
	}

	public static String escapeHTML(String inp) {
		return inp.replace("<", "&lt;").replace(">","&gt;");
	}
    
}
