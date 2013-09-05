package connexus.servlet;

//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
//import com.google.appengine.api.users.User;
//import com.google.appengine.api.users.UserService;
//import com.google.appengine.api.users.UserServiceFactory;


import com.googlecode.objectify.Ref;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connexus.Config;
import connexus.model.CUser;
import connexus.model.Site;
import connexus.status.*;

public class Admin extends ConnexusServletBase {

	private static final long serialVersionUID = 6204953200057490592L;
	public static final String uri = "/admin";
	public static final String dispatcher = "/WEB-INF/jsp/admin.jsp";
	
	private Ref<Site> site; 
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		site = ofy().load().type(Site.class).id(Config.siteId);
		
		if (req.getParameter("edit") != null) {
			doShowEditScreen(req, resp);
		} else {
			// order("realName").
			List<CUser> allUsersList = ofy().load().type(CUser.class)
					.ancestor(site).list();
			for (CUser userRec : allUsersList) {
				System.err.println("USER REC: " + userRec.toString());
			}
			req.setAttribute("userList", allUsersList);
		}
		// throw new ServletException("Retrieving products failed!", e);
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();

        site = ofy().load().type(Site.class).id(Config.siteId);
        if (site == null) {
        	// let's be as cryptic as possible...
        	alertError(req, "An unfortunate occurence has occured.");
        	return;
        }

		if (req.getParameter("create") != null) {
			createUser(req, resp);
		}
		else if (req.getParameter("delete") != null) {
			deleteUser(req, resp);
		} else {
			alertWarning(req, "Command not implemented yet.");
		}

		resp.sendRedirect(uri);
	}

	private void doShowEditScreen(HttpServletRequest req, HttpServletResponse resp) {
		Long editId = Long.parseLong(req.getParameter("edit"));
		req.setAttribute("editItem", editId);
		
		CUser theUser = getUserById(editId);
		if (theUser == null){
			alertError(req, "User does not exist.");
			return;
		}

	}
	
	private void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
		Long objectId = Long.parseLong(req.getParameter("id"));
		
		CUser theUser = getUserById(objectId);
		if (theUser == null){
			alertError(req, "User does not exist.");
			return;
		}
		
		// Nuke it
		ofy().delete().type(CUser.class).parent(site).id(objectId).now();

		alertSuccess(req, "User deleted.");
	}
	
	private CUser getUserByAccountName(String accountName) {		
		CUser existingUser = ofy().load().type(CUser.class).ancestor(site).
				filter("accountName", accountName).first().get();
		return existingUser;
	}

	private CUser getUserById(Long userId) {		
		return ofy().load().type(CUser.class).parent(site).id(userId).get();
	}

	private void createUser(HttpServletRequest req, HttpServletResponse resp) {
		String accountName = req.getParameter("accountName");
		String realName = req.getParameter("realName");
		
		if (accountName.length() == 0 || realName.length() == 0) {
			alertError(req, "You must provide an account name and a real name.");
			return;			
		}

		if (accountName.length() > 30) {
			alertError(req, "Account name is too long.");
			return;			
		}
		if (realName.length() > 200) {
			alertError(req, "Real name is too long.");
			return;			
		}

		CUser existingUser = getUserByAccountName(accountName);
		if (existingUser != null) {
			alertError(req, "An account of that name already exists.");
			return;
		}
		
		CUser thisUser = new CUser(null, site.getKey(), accountName, realName);
		ofy().save().entities(thisUser).now();

		alertSuccess(req, "Created an account for " + realName + " : "
				+ accountName);
		
	}
}