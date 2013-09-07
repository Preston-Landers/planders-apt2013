package connexus;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;

import connexus.servlet.Create;

public class Config {
	public static final String productName = "Connexus";
	
    public static final long siteId = 1;

	/*
	 * TODO: move somewhere else
	 */
	public static String getURIWithParams(List<String[]> params) {
		URIBuilder builder;
		try {
			builder = new URIBuilder(Create.uri);
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
		return Create.uri;
	}
    
}
