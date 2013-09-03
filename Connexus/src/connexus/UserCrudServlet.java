package connexus;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connexus.status.*;

public class UserCrudServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6204953200057490592L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		HttpSession session = req.getSession(true);

		// Test some status messages.
		// StatusHandler.addStatus(session, new
		// StatusMessage(StatusMessageType.INFO,
		// "This is your informational msg."));
		// StatusHandler.addStatus(session, new
		// StatusMessage(StatusMessageType.WARNING,
		// "This is your warning msg."));
		// StatusHandler.addStatus(session, new
		// StatusMessage(StatusMessageType.ERROR, "This is your error msg."));
		// StatusHandler.addStatus(session, new
		// StatusMessage(StatusMessageType.SUCCESS,
		// "This is your success msg."));

		if (req.getParameter("create") != null) {
			StatusHandler.addStatus(session, new StatusMessage(
					StatusMessageType.SUCCESS, "Created a user account."));

		}

		// We have one entity group per Guestbook with all Greetings residing
		// in the same entity group as the Guestbook to which they belong.
		// This lets us run a transactional ancestor query to retrieve all
		// Greetings for a given Guestbook. However, the write rate to each
		// Guestbook should be limited to ~1/second.
		// String guestbookName = req.getParameter("guestbookName");
		// Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
		// String content = req.getParameter("content");
		// Date date = new Date();
		// Entity greeting = new Entity("Greeting", guestbookKey);
		// greeting.setProperty("user", user);
		// greeting.setProperty("date", date);
		// greeting.setProperty("content", content);
		// DatastoreService datastore = DatastoreServiceFactory
		// .getDatastoreService();
		// datastore.put(greeting);

		resp.sendRedirect("/admin.jsp");
	}
}