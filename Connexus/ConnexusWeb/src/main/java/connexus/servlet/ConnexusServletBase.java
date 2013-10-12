package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import connexus.Config;
import connexus.model.CUser;
import connexus.model.Site;
import connexus.status.*;

public abstract class ConnexusServletBase extends HttpServlet {
	
	protected Ref<Site> site; // The "Site" entity (usually just one exists)
	protected User guser;     // The Google User object - MAY BE NULL! if not logged in
	protected CUser cuser;    // The Connexus User object - MAY BE NULL! if not logged in
	protected final static UserService userService = UserServiceFactory.getUserService();
	protected final static MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	
	static {
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}
	
	/**
	 * Set up things that are common to all pages including the currently logged in user, if any.
	 * @param req
	 * @param resp
	 */
	protected void InitializeContext(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		cuser = null;
		guser = null;
		
        guser = userService.getCurrentUser();
        req.setAttribute("guser", guser);
        if (guser == null) {
        	req.setAttribute("loginURL", userService.createLoginURL(req.getRequestURI()));
        } else {
        	String logoutURLCacheKey = guser.getUserId() + "_logoutURL";
        	String logoutURL = (String) syncCache.get(logoutURLCacheKey);
        	if (logoutURL == null) {
        		logoutURL = userService.createLogoutURL(req.getRequestURI());
        		syncCache.put(logoutURLCacheKey, logoutURL, Expiration.byDeltaSeconds(60 * 20));
        	}
        	req.setAttribute("logoutURL", logoutURL);	
        }        
        
        // Parent entity for the entire site
        site = ofy().load().type(Site.class).id(Config.siteId);
        if (site == null) {
        	// Should probably be logging something somewhere...
        	String errorMsg = "Internal error: site entity was not initialized.";
        	System.err.println(errorMsg);
        	alertError(req, errorMsg);
        	return;
        }        
        
        // Automatically create a CUser for any Google Users we recognize
        try {
            if (guser != null) {
                cuser = getOrCreateUserRecord(guser, site.getKey());
                if (cuser != null) {
                    req.setAttribute("cuser", cuser);
                }
            }
        } catch (UserCreateException e) {
            e.printStackTrace(System.err);
            alertError(req, "You must provide an account name and a real name.");
        }
    }

    public static CUser getOrCreateUserRecord(User guser, Key<Site> site) throws UserCreateException {
        CUser cuser = null;
        // Automatically create a CUser for any Google Users we recognize
        if (guser != null) {
            // Try memcache before doing a query
            boolean doSetMemcache = false;
            String cuserIdCacheKey = guser.getUserId() + "_cuserId";
            Long cuserId = (Long) syncCache.get(cuserIdCacheKey);
            if (cuserId == null) {
                cuser = ofy().load().type(CUser.class).ancestor(site)
                        .filter("guser", guser).first().get();
                doSetMemcache = true;
            } else {
                // System.err.println("user id cache hit : " + guser + " : " + cuserId);
                Key<CUser>  cuserKey = com.googlecode.objectify.Key.create(site, CUser.class, cuserId);
                cuser = ofy().load().key(cuserKey).get();
            }
            if (cuser == null) {
                cuser = createUser(guser, site);
            }
            if (cuser != null) {
                if (doSetMemcache) {
                    // System.err.println("Setting user id into cache: " + guser + " : " + cuser.getId());
                    syncCache.put(cuserIdCacheKey, cuser.getId());
                }
            }

        }

        return cuser;
    }

    public static class UserCreateException extends RuntimeException {
        public UserCreateException(String message) {
            super(message);
        }
    }

    private static CUser createUser(User guser, Key<Site> site) throws UserCreateException {
        String accountName = guser.getEmail();
		String realName = guser.getNickname();
		
		if (accountName.length() == 0 || realName.length() == 0) {
            throw new UserCreateException("You must provide an account name and a real name.");
//			alertError(req, "You must provide an account name and a real name.");
//			return null;
		}
		
		CUser thisUser = new CUser(null, site, accountName, realName);
		thisUser.setGuser(guser);
		ofy().save().entities(thisUser).now();

//		alertSuccess(req, "Created an account for " + realName + " : "
//				+ accountName);
		return thisUser;
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
