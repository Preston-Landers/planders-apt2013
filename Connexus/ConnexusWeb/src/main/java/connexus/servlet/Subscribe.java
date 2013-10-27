package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.ConnexusContext;
import connexus.StreamHandle;
import connexus.model.Stream;
import connexus.model.Subscription;

public class Subscribe extends ConnexusServletBase {

	private static final long serialVersionUID = -4578267704775630816L;
	public static final String uri = "/subscribe";
	public static final String dispatcher = "/WEB-INF/jsp/view.jsp";
	public static final String doneUri = View.uri;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization

		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
        ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization

		StreamHandle viewingStreamHandle;
		try {
			viewingStreamHandle = StreamHandle.getStreamHandleFromRequest(req,
					cContext.getSite());
		} catch (RuntimeException e) {
			viewingStreamHandle = new StreamHandle();
		}

		// Validate that we have a stream to deal with at this point
		if (viewingStreamHandle.getStream() == null) {
			String redirUri = doneUri;
			if (req.getParameter("unsubscribe") != null) {
				// See if we want to be redirected anywhere after this.
				if (req.getParameter("redir") != null) {
					redirUri = req.getParameter("redir");
				}
				alertError(req, "You must select a stream to unsubscribe from.");
			} else {
				alertError(req, "Stream not found.");
			}
			resp.sendRedirect(redirUri);
			return;

		}

		if (cContext.getCuser() == null) {
			String redir = userService.createLoginURL(viewingStreamHandle.getStream().getViewURI());
			alertError(req, "<A HREF=\"" + redir + "\">Please log in to subscribe to streams.</A>");
			resp.sendRedirect(doneUri);
			return;
		}
		
		
		// Handle the request
		if (req.getParameter("subscribe") != null) {
			doSubscribe(cContext, req, resp, viewingStreamHandle);
		} else if (req.getParameter("unsubscribe") != null) {
			doUnsubscribe(cContext, req, resp, viewingStreamHandle);
		} else {
			alertError(req, "Internal error: unknown command.");
			resp.sendRedirect(doneUri);
			return;
		}

	}

	public void doUnsubscribe(ConnexusContext cContext, HttpServletRequest req, HttpServletResponse resp,
			StreamHandle viewingStreamHandle) throws IOException {

		// See if we want to be redirected anywhere after this.
		String redirectURI = doneUri;
		if (req.getParameter("redir") != null) {
			redirectURI = req.getParameter("redir");
		}
		
		Subscription thisSub = null;
		List<Subscription> mySubs = Subscription.getSubscriptionsForUser(cContext.getCuser().getKey());
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
		Subscription.clearCacheForSubscription(cContext.getCuser(), viewingStreamHandle);
		
		alertWarning(req, "You have unsubscribed from the stream " + subStream.getName() + ".");
		resp.sendRedirect(redirectURI);
		return;		
	}
	
	public void doSubscribe(ConnexusContext cContext, HttpServletRequest req, HttpServletResponse resp,
			StreamHandle viewingStreamHandle) throws IOException {
		List<Subscription> mySubs = Subscription.getSubscriptionsForUser(cContext.getCuser().getKey());
		for (Subscription sub : mySubs) {
			Stream subStream = ofy().load().key(sub.getStream()).get();
			if (subStream == viewingStreamHandle.getStream()) {
				alertError(req, "You are already subscribed to that stream.");
				resp.sendRedirect(doneUri);
				return;
			}
		}

		Subscription newSub = new Subscription(null, cContext.getCuser().getKey(),
				viewingStreamHandle.getStream().getKey());
		ofy().save().entities(newSub).now();
		Subscription.clearCacheForSubscription(cContext.getCuser(), viewingStreamHandle);

		alertSuccess(req, "You are now subscribed to this stream.");
		resp.sendRedirect(viewingStreamHandle.getStream().getViewURI());

	}
}