package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Ref;
import connexus.ConnexusContext;
import connexus.model.CUser;
import connexus.model.Site;

public class Admin extends ConnexusServletBase {

	private static final long serialVersionUID = 6204953200057490592L;
	public static final String uri = "/admin";
	public static final String dispatcher = "/WEB-INF/jsp/admin.jsp";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		ConnexusContext connexusContext = InitializeContext(req, resp); // Base site context initialization
		
		if (req.getParameter("edit") != null) {
			doShowEditScreen(connexusContext, req, resp);
		} else {
			List<CUser> allUsersList = ofy().load().type(CUser.class)
					.ancestor(connexusContext.getSite()).list();
			for (CUser userRec : allUsersList) {
				System.err.println("USER REC: " + userRec.toString());
			}
			req.setAttribute("userList", allUsersList);
		}
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		ConnexusContext connexusContext = InitializeContext(req, resp); // Base site context initialization

		if (req.getParameter("create") != null) {
			createUser(connexusContext, req, resp);
		}
		else if (req.getParameter("delete") != null) {
			deleteUser(connexusContext, req, resp);
		} else {
			alertWarning(req, "Command not implemented yet.");
		}

		resp.sendRedirect(uri);
	}

	private void doShowEditScreen(ConnexusContext connexusContext, HttpServletRequest req, HttpServletResponse resp) {
		Long editId = Long.parseLong(req.getParameter("edit"));
		req.setAttribute("editItem", editId);
		
		CUser theUser = CUser.getById(editId, connexusContext.getSite().getKey());
		if (theUser == null){
			alertError(req, "User does not exist.");
			return;
		}

	}
	
	private void deleteUser(ConnexusContext connexusContext, HttpServletRequest req, HttpServletResponse resp) {
        Ref<Site> site = connexusContext.getSite();
		Long objectId = Long.parseLong(req.getParameter("id"));
		
		CUser theUser = CUser.getById(objectId, site.getKey());
		if (theUser == null){
			alertError(req, "User does not exist.");
			return;
		}
		
		// Nuke it
		ofy().delete().type(CUser.class).parent(site).id(objectId).now();

		alertSuccess(req, "User deleted.");
	}
	
	private CUser getUserByAccountName(ConnexusContext connexusContext, String accountName) {
		CUser existingUser = ofy().load().type(CUser.class).ancestor(connexusContext.getSite()).
				filter("accountName", accountName).first().get();
		return existingUser;
	}


	private void createUser(ConnexusContext connexusContext, HttpServletRequest req, HttpServletResponse resp) {
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

		CUser existingUser = getUserByAccountName(connexusContext, accountName);
		if (existingUser != null) {
			alertError(req, "I'm sorry, but an account of that name already exists.");
			return;
		}

        Ref<Site> site = connexusContext.getSite();
		CUser thisUser = new CUser(null, site.getKey(), accountName, realName);
		ofy().save().entities(thisUser).now();

		alertSuccess(req, "Created an account for " + realName + " : "
				+ accountName);
		
	}
}