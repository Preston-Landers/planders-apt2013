package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.StreamHandle;
import connexus.model.Stream;
import connexus.model.Subscription;

public class Subscribe extends ConnexusServletBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4578267704775630816L;
	public static final String uri = "/subscribe";
	public static final String dispatcher = "/WEB-INF/jsp/view.jsp";
	public static final String doneUri = View.uri;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization

		// List<CUser> allUsersList = ofy().load().type(CUser.class).list();
		// for (CUser userRec : allUsersList) {
		// System.err.println("USER REC: " + userRec.toString());
		// }
		// req.setAttribute("userList", allUsersList);

		// throw new ServletException("Retrieving products failed!", e);

		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization

		StreamHandle viewingStreamHandle;
		try {
			viewingStreamHandle = StreamHandle.getStreamHandleFromRequest(req,
					site);
			// viewingStream = viewingStreamHandle.getStream();
			// viewingStreamUser = viewingStreamHandle.getCuser();
			// req.setAttribute("viewingStream", viewingStream);
			// req.setAttribute("viewingStreamUser", viewingStreamUser);
		} catch (RuntimeException e) {
			viewingStreamHandle = new StreamHandle();
		}

		// Validate that we have a stream to deal with at this point
		if (viewingStreamHandle.getStream() == null) {
			alertError(req, "Stream not found.");
			resp.sendRedirect(doneUri);
			return;

		}

		if (cuser == null) {
			String redir = userService.createLoginURL(viewingStreamHandle.getStream().getViewURI());
			alertError(req, "<A HREF=\"" + redir + "\">Please log in to subscribe to streams.</A>");
			resp.sendRedirect(doneUri);
			return;
		}
		
		
		// Handle the request
		if (req.getParameter("subscribe") != null) {
			doSubscribe(req, resp, viewingStreamHandle);
		} else if (req.getParameter("unsubscribe") != null) {
			doUnsubscribe(req, resp, viewingStreamHandle);
		} else {
			alertError(req, "Internal error: unknown command.");
			resp.sendRedirect(doneUri);
			return;
		}

	}

	public void doUnsubscribe(HttpServletRequest req, HttpServletResponse resp,
			StreamHandle viewingStreamHandle) throws IOException {

		// See if we want to be redirected anywhere after this.
		String redirectURI = doneUri;
		if (req.getParameter("redir") != null) {
			redirectURI = req.getParameter("redir");
		}
		
		Subscription thisSub = null;
		List<Subscription> mySubs = Subscription.getSubscriptionsForUser(cuser);
		for (Subscription sub : mySubs) {
			Stream subStream = ofy().load().key(sub.getStream()).get();
			if (subStream == viewingStreamHandle.getStream()) {
				thisSub = sub;
				break;
			}
		}
		if (thisSub == null) {
			alertWarning(req, "You are not subscribed to that stream.");
			resp.sendRedirect(redirectURI);
			return;					
		}
		// Get the view link before we delete.
		
		Stream subStream = ofy().load().key(thisSub.getStream()).get();
		if (subStream == null) {
			alertWarning(req, "Internal error: cannot load stream from subscription.");
			resp.sendRedirect(redirectURI);
			return;								
		}
		
		if (req.getParameter("redir") == null) {
			redirectURI = subStream.getViewURI();
		}
		
		// Delete the sub.
		ofy().delete().entities(thisSub).now();
		
		alertWarning(req, "You have unsubscribed from the stream " + subStream.getName() + ".");
		resp.sendRedirect(redirectURI);
		return;		
	}
	
	public void doSubscribe(HttpServletRequest req, HttpServletResponse resp,
			StreamHandle viewingStreamHandle) throws IOException {
		List<Subscription> mySubs = Subscription.getSubscriptionsForUser(cuser);
		for (Subscription sub : mySubs) {
			Stream subStream = ofy().load().key(sub.getStream()).get();
			if (subStream == viewingStreamHandle.getStream()) {
				alertError(req, "You are already subscribed to that stream.");
				resp.sendRedirect(doneUri);
				return;
			}
		}

		System.err.println("Ready to create a subscription!");

		Subscription newSub = new Subscription(null, cuser.getKey(),
				viewingStreamHandle.getStream().getKey());
		ofy().save().entities(newSub).now();

		alertSuccess(req, "You are now subscribed to this stream.");
		resp.sendRedirect(viewingStreamHandle.getStream().getViewURI());

	}
}