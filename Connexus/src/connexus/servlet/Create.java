package connexus.servlet;

import static connexus.OfyService.ofy;
import connexus.Config;
import connexus.EmailHelper;
import connexus.model.Stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;

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
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization
		CharMatcher wspace = CharMatcher.is(' ');

		String streamName = req.getParameter("name");
		String subscribersStr = req.getParameter("subscribers");
		String subscribersNote = req.getParameter("subscribersNote");
		String tags = req.getParameter("tags");
		String coverURL = req.getParameter("cover");

		List<String[]> params = new ArrayList<String[]>();
		params.add(new String[]{"subscribers", subscribersStr});
		params.add(new String[]{"subscribersNote", subscribersNote});
		params.add(new String[]{"tags", tags});
		params.add(new String[]{"cover", coverURL});
		
		// Look for any streams of this name. 
		List<Stream> existingStreams = ofy().load().type(Stream.class)
				.ancestor(site).filter("name ==", streamName).list();
		if (existingStreams.size() > 0) {
			alertError(req, "Sorry, but a stream of that name already exists. Please select another name.");
			
			String newUri = Config.getURIWithParams(uri, params);
			resp.sendRedirect(newUri);
			return;
		}
		
		
		// TODO: more validation...
		streamName = wspace.trimFrom(streamName);
		if (streamName == null || streamName.length() < 1) {
			alertError(req, "I'm sorry but you need to give your stream a name. Any name will do.");
			String newUri = Config.getURIWithParams(uri, params);
			resp.sendRedirect(newUri);
			return;
		}

		// TODO: deal with subscribers emails
		
		List<String> tagsList = Arrays.asList(tags.split("\\s*,\\s*")); 

		Stream stream = new Stream(null, cuser.getKey(), streamName);
		stream.setCoverURL(coverURL);
		stream.setTags(tagsList);
		
		ofy().save().entities(stream).now();
		
		if (subscribersStr != null && subscribersStr.length() > 0) {
			EmailHelper.sendStreamCreateInvitation(stream, subscribersStr, subscribersNote);
		}
		
		alertSuccess(req, "Created a stream named " + streamName);
		
		resp.sendRedirect(Manage.uri);
	}
}