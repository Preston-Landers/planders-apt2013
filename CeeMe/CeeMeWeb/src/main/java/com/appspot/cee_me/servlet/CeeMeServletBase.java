package com.appspot.cee_me.servlet;

import static com.appspot.cee_me.OfyService.ofy;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.appspot.cee_me.CeeMeContext;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.model.*;
import com.appspot.cee_me.status.*;


public abstract class CeeMeServletBase extends HttpServlet {

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
    public static CeeMeContext InitializeContext(HttpServletRequest req,
                                                    HttpServletResponse resp) throws IOException, ServletException {

        // The Google User object - MAY BE NULL! if not logged in
        User guser = userService.getCurrentUser();
        CUser cuser = null;    // The CeeMe User object - MAY BE NULL! if not logged in

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


        // Automatically create a CUser for any Google Users we recognize
        try {
            if (guser != null) {
                cuser = getOrCreateUserRecord(guser);
                if (cuser != null) {
                    req.setAttribute("cuser", cuser);
                    req.setAttribute("currentUserId", cuser.getId());
                }
            }
        } catch (UserCreateException e) {
            e.printStackTrace(System.err);
            alertError(req, "You must provide an account name and a real name.");
        }

        return new CeeMeContext(guser, cuser);
    }

    public static CUser getOrCreateUserRecord(User guser) throws UserCreateException {
        CUser cuser = null;
        // Automatically create a CUser for any Google Users we recognize
        if (guser != null) {
            String normEmail = Config.norm(guser.getEmail());
            // System.err.println("Searching for user " + normEmail);
            cuser = ofy().load().type(CUser.class)
                    .filter("accountName", normEmail).first().get();
            if (cuser == null) {
                System.err.println("Creating New User! " + guser + " guser ID: " + normEmail);
                cuser = createUser(guser);
                System.err.println("New User cuser id: " + cuser.getId());
            }

        }

        return cuser;
    }

    public static class UserCreateException extends RuntimeException {
        public UserCreateException(String message) {
            super(message);
        }
    }

    private static CUser createUser(User guser) throws UserCreateException {
        String accountName = Config.norm(guser.getEmail()); // needs to be lowercase
        String realName = guser.getNickname();

        if (accountName.length() == 0 || realName.length() == 0) {
            throw new UserCreateException("You must provide an account name and a real name.");
//			alertError(req, "You must provide an account name and a real name.");
//			return null;
        }

        CUser thisUser = new CUser(null, accountName, realName);
        thisUser.setGuser(guser);
        ofy().save().entities(thisUser).now();

//		alertSuccess(req, "Created an account for " + realName + " : "
//				+ accountName);
        return thisUser;
    }

    private static final long serialVersionUID = 7414103509881465189L;

    public static void alertError(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.ERROR, msg);
    }
    public static void alertSuccess(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.SUCCESS, msg);
    }
    public static void alertInfo(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.INFO, msg);
    }
    public static void alertWarning(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.WARNING, msg);
    }

    public static void alertMessage(HttpServletRequest req, StatusMessageType msgType,
                                    String msg) {
        HttpSession session = req.getSession(true);
        StatusHandler.addStatus(session, new StatusMessage(msgType, msg));
    }

}
