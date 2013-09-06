package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;





import com.googlecode.objectify.Key;

import connexus.model.*;

public class Manage extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1609921843328426049L;
	public static final String uri = "/manage";
	public static final String dispatcher = "/WEB-INF/jsp/manage.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		InitializeContext(req, resp); // Base site context initialization

		List<Stream> myStreams = ofy().load().type(Stream.class).ancestor(cuser.getKey()).list();
//		for (Stream stream : myStreams ) {
//			System.err.println("STREAM REC: " + stream.toString());
//		}
		req.setAttribute("myStreamList", myStreams);

		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		InitializeContext(req, resp); // Base site context initialization

		if (req.getParameter("delete") != null) {
			deleteStream(req, resp);
		} else {
			alertWarning(req, "Internal error: command not implemented yet.");
		}
		
		resp.sendRedirect(uri);
	}
	
	private void deleteStream(HttpServletRequest req, HttpServletResponse resp) {
		// todo: delete_%id
		for (String objectIdStr : req.getParameterValues("delete")) {
			if (objectIdStr.length() < 1) {
				continue;
			}
			Long objectId = Long.parseLong(objectIdStr);

			Stream stream = getStreamById(objectId);
			if (stream == null){
				alertError(req, "Stream does not exist.");
				return;
			}
			
			// TODO: logging
			System.err.println("Deleting stream: " + stream);
			
			// Nuke it
			ofy().delete().entities(stream).now();
			alertSuccess(req, "Stream deleted: " + stream.getName());

		}

	}
	
	// TODO: move to model class? same for user?
	private Stream getStreamById(Long objectId) {	
		return ofy().load().type(Stream.class).parent(cuser).id(objectId).get();
	}

}