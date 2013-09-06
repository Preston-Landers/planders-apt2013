package connexus.servlet;

//import static connexus.OfyService.ofy;
//import java.util.List;
//import connexus.CUser;

import static connexus.OfyService.ofy;
import connexus.model.Stream;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;
import com.googlecode.objectify.Key;

public class Create extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8233019154211372496L;
	public static final String uri = "/create";
	public static final String dispatcher = "/WEB-INF/jsp/create.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		InitializeContext(req, resp); // Base site context initialization
		
//		List<CUser> allUsersList = ofy().load().type(CUser.class).list();
//		for (CUser userRec : allUsersList) {
//			System.err.println("USER REC: " + userRec.toString());
//		}
//		req.setAttribute("userList", allUsersList);

		// throw new ServletException("Retrieving products failed!", e);
		
		// Force user to login screen before showing create
		if (guser == null) {
			resp.sendRedirect((String) req.getAttribute("loginURL"));
		}
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		InitializeContext(req, resp); // Base site context initialization

		String streamName = req.getParameter("name");
		String subscribersStr = req.getParameter("subscribers");
		String subscribersNote = req.getParameter("subscribersNote");
		String tags = req.getParameter("tags");
		String coverURL = req.getParameter("cover");
		
		// TODO: more validation...
		CharMatcher matcher = CharMatcher.is(' ');
		streamName = matcher.trimFrom(streamName);

		// TODO: deal with subscribers emails
		
		List<String> tagsList = Arrays.asList(tags.split("\\s*,\\s*")); 
				
		Stream stream = new Stream(null, cuser.getKey(), streamName);
		stream.setCoverURL(coverURL);
		stream.setTags(tagsList);
		
		ofy().save().entities(stream).now();
		
		alertSuccess(req, "Created a stream named " + streamName);
		
		resp.sendRedirect(Manage.uri);
	}
}