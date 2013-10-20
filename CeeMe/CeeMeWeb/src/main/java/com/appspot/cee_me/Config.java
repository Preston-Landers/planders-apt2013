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

    // For the endpoints
    public static final String ANDROID_CLIENT_ID = "771214203866-9vmc9ha8rvco92ul4dfc74r00t0nj6ls.apps.googleusercontent.com";
    public static final String WEB_CLIENT_ID = "771214203866-7btn313m3jrh49s1dhd837ssm1n1lrd0.apps.googleusercontent.com";
    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

    // Maximum search results to show
    public static final int maxSearchResults = 5;
    public static final int API_CACHE_TIME_SEC = 10; // # of seconds cache is good for in API calls

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
			e.printStackTrace(System.err);
		}
		return uri;
	}

	public static String escapeHTML(String inp) {
		return inp.replace("<", "&lt;").replace(">","&gt;");
	}

    /**
     * normalize strings for account lookup
     * Apparently google account names/emails are case sensitive
     * @param inp
     * @return
     */
    public static String norm(String inp) {
        return inp.toLowerCase();
    }
    
}
