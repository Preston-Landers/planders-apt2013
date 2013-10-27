package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.ConnexusContext;
import connexus.model.*;

public class Manage extends ConnexusServletBase {
	
	private static final long serialVersionUID = 1609921843328426049L;
	public static final String uri = "/manage";
	public static final String dispatcher = "/WEB-INF/jsp/manage.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization
        CUser cuser = cContext.getCuser();

		List<Stream> myStreams;
		if (cuser != null) {
			myStreams = ofy().load().type(Stream.class).ancestor(cuser.getKey()).list();
		} else {
			myStreams = new ArrayList<Stream>();
		}
		req.setAttribute("myStreamList", myStreams);
		
		// initialize my subscriptions (cuser could be null, you get an empty
		// list)
        if (cuser != null) {
		    List<Subscription> mySubs = Subscription.getSubscriptionsForUser(cuser.getKey());
		    req.setAttribute("mySubscriptions", mySubs);
        }

		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

        ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization

		if (req.getParameter("doDelete") != null) {
			if (req.getParameter("delete") == null) {
				alertError(req, "You must select a stream to delete first.");
				resp.sendRedirect(uri);
				return;
			}
			deleteStream(cContext, req, resp);
		} else {
			alertWarning(req, "Internal error: command not implemented yet.");
		}
		
		resp.sendRedirect(uri);
	}
	
	private void deleteStream(ConnexusContext cContext, HttpServletRequest req, HttpServletResponse resp) {
        CUser cuser = cContext.getCuser();
		for (String objectIdStr : req.getParameterValues("delete")) {
			if (objectIdStr == null || objectIdStr.length() < 1) {
				continue;
			}
			Long objectId = Long.parseLong(objectIdStr);

			Stream stream = Stream.getById(objectId, cuser);
			if (stream == null){
				alertError(req, "Stream does not exist.");
				return;
			}
			
			if (stream.deleteStream()) {
				alertSuccess(req, "Stream deleted: " + stream.getName());
				
			} else {
				alertError(req, "Stream could not be deleted: " + stream.getName());
			}

		}

	}
	
	


}