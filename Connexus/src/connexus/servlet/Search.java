package connexus.servlet;

//import static connexus.OfyService.ofy;
//import java.util.List;
//import connexus.CUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;

import connexus.Config;
import connexus.model.Stream;

public class Search extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -164245092113045712L;
	public static final String uri = "/search";
	public static final String dispatcher = "/WEB-INF/jsp/search.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization
		
//		List<CUser> allUsersList = ofy().load().type(CUser.class).list();
//		for (CUser userRec : allUsersList) {
//			System.err.println("USER REC: " + userRec.toString());
//		}
//		req.setAttribute("userList", allUsersList);

		// throw new ServletException("Retrieving products failed!", e);
		
		if (req.getParameter("q") != null) {
			doHandleSearch(req, resp);
		}
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization
		alertInfo(req, "TODO: Not implemented yet.");
		resp.sendRedirect(uri);
	}

	public void doHandleSearch(HttpServletRequest req, HttpServletResponse resp) {
		String queryTerm = req.getParameter("q");
		queryTerm = CharMatcher.WHITESPACE.trimFrom(queryTerm);
		System.err.println("User " + cuser + " searched for " + queryTerm);
		if (queryTerm == null || queryTerm.length() < 1) {
			alertInfo(req, "Please type a search term.");
			return;
		}
		
		// Has searched for something, ok to show results pane
		req.setAttribute("showSearchResults", true);
		req.setAttribute("q", queryTerm);
		
		// some dummy results
		// No stream selected... let them browse all streams.
		List<Stream> searchResults = performStreamSearch(queryTerm);
		req.setAttribute("searchResultsList", searchResults);

	}

	public List<Stream> performStreamSearch(String queryTerm) {
		List<Stream> rv = new ArrayList<Stream>();
		int hits = 0;
		int max = Config.getMaxSearchResults();
		for (Stream stream : Stream.getAllStreams(site.getKey())) {
			if (hits > max) {
				break;
			}
			boolean match = false;
			// Check the stream name
			if (matchString(queryTerm, stream.getName()))
				match = true;
			// Check the owner's name
			else if (matchString(queryTerm, stream.getOwnerName()))
				match = true;
			// Now check all the tags.
			else if (stream.getTags() != null) {
				for (String tag : stream.getTags()) {
					if (matchString(queryTerm, tag)) {
						match = true;
						break;
					}
				}
			}
			// Now check the media comments??
			
			if (match) {
				rv.add(stream);
				hits++;
			}
			
		}
		return rv;
	}
	
	public boolean matchString(String queryTerm, String test) {
		Pattern pattern = Pattern.compile(queryTerm, Pattern.CASE_INSENSITIVE);
		for (String term : queryTerm.split("\\s")) {
			Matcher matcher = pattern.matcher(test);
			if (matcher.find())
				return true;			
		}
		return false;
	}
}