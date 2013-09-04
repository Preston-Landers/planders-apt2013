package connexus.servlet;

//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;
//import com.google.appengine.api.users.User;
//import com.google.appengine.api.users.UserService;
//import com.google.appengine.api.users.UserServiceFactory;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connexus.status.*;
import connexus.CUser;

public class Admin extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6204953200057490592L;
	public static final String uri = "/admin";
	public static final String dispatcher = "/WEB-INF/jsp/admin.jsp";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		List<CUser> allUsersList = ofy().load().type(CUser.class).list();
		for (CUser userRec : allUsersList) {
			System.err.println("USER REC: " + userRec.toString());
		}
		req.setAttribute("userList", allUsersList);

		// throw new ServletException("Retrieving products failed!", e);
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();

		if (req.getParameter("create") != null) {
			createUser(req, resp);
		}


		resp.sendRedirect(uri);
	}
	
	private void createUser(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession(true);
		String accountName = req.getParameter("accountName");
		String realName = req.getParameter("realName");
		
		// Check for existing account of that name.
		CUser existingUser = ofy().load().type(CUser.class).filter("accountName", accountName).first().get();
		if (existingUser != null) {
			StatusHandler.addStatus(session, new StatusMessage(
					StatusMessageType.ERROR, accountName + " : An account of that name already exists."));
			return;
		}
		
		CUser thisUser = new CUser(accountName, realName);
		ofy().save().entities(thisUser).now();
		
		StatusHandler.addStatus(session, new StatusMessage(
				StatusMessageType.SUCCESS, "Created an account " + accountName + " for " + realName));

		
	}
}