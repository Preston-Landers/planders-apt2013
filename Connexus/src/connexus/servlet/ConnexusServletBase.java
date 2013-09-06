package connexus.servlet;

import static connexus.OfyService.ofy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Ref;

import connexus.Config;
import connexus.model.CUser;
import connexus.model.Site;
import connexus.status.*;

public abstract class ConnexusServletBase extends HttpServlet {
	
	protected User guser;
	protected CUser cuser;
	protected Ref<Site> site; 
	
	protected void InitializeContext (HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = UserServiceFactory.getUserService();
        guser = userService.getCurrentUser();
        req.setAttribute("guser", guser);
        req.setAttribute("loginURL", userService.createLoginURL(req.getRequestURI()));
        req.setAttribute("logoutURL", userService.createLogoutURL(req.getRequestURI()));
        // Parent entity for the entire site
        site = ofy().load().type(Site.class).id(Config.siteId);
        if (site == null) {
        	// Should probably be logging something somewhere...
        	alertError(req, "Internal error: site entity was not initialized.");
        	return;
        }        
        
        // Automatically create a CUser for any Google Users we recognize
        if (guser != null) {
			cuser = ofy().load().type(CUser.class).ancestor(site)
					.filter("guser", guser).first().get();
			if (cuser == null) {
				createUser(req, resp);
			}
    	
        }
	}
	
	private void createUser(HttpServletRequest req, HttpServletResponse resp) {
		String accountName = guser.getEmail(); 
		String realName = guser.getNickname();
		
		if (accountName.length() == 0 || realName.length() == 0) {
			alertError(req, "You must provide an account name and a real name.");
			return;			
		}
		
		CUser thisUser = new CUser(null, site.getKey(), accountName, realName);
		thisUser.setGuser(guser);
		ofy().save().entities(thisUser).now();

//		alertSuccess(req, "Created an account for " + realName + " : "
//				+ accountName);
		
	}
	
	private static final long serialVersionUID = 7414103509881465189L;

	public void alertError(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.ERROR, msg);
	}
	public void alertSuccess(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.SUCCESS, msg);
	}
	public void alertInfo(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.INFO, msg);
	}
	public void alertWarning(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.WARNING, msg);
	}

	public void alertMessage(HttpServletRequest req, StatusMessageType msgType,
			String msg) {
		HttpSession session = req.getSession(true);
		StatusHandler.addStatus(session, new StatusMessage(msgType, msg));
	}

}
