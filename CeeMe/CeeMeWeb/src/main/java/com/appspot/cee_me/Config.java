package com.appspot.cee_me;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Config {
	public static final String productName = "Cee.me";
	public static final String productDomain = "cee-me.appspot.com";
	public static final String productURL = "http://" + productDomain;

	public static final String productAdminEmail = "planders@gmail.com";
	
    public static final long siteId = 1;
    
    // For the endpoints
    public static final String ANDROID_CLIENT_ID = "860742061992.apps.googleusercontent.com";
    public static final String WEB_CLIENT_ID = "860742061992-hluiaap2cfsl9dp1io7bc3au26vks2m6.apps.googleusercontent.com";
    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

    // Maximum search results to show
    public static final int maxSearchResults = 5;
    public static final int API_CACHE_TIME_SEC = 10; // # of seconds cache is good for in API calls

    // How to format Joda DateTimes for the user
    public static final String DATE_TIME_FORMAT_PATTERN = "EE, MMM d, YYYY @ HH:mm zz";

    // What do we say when we got a POST with an unknown or malformed command
    public static final String MSG_UNKNOWN_COMMAND = "Internal error: command unknown or not implemented yet.";
    public static final String MSG_NOT_DEVICE_OWNER = Config.MSG_NOT_DEVICE_OWNER;


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

    /**
     * Format a Joda DateTime to our preferred string representation
     * @param dateTime Joda DateTime instance
     * @return a formatted string
     */
    public static String formatDateTime(DateTime dateTime) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_TIME_FORMAT_PATTERN);
        return fmt.print(dateTime);
    }
}
