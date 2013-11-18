package com.appspot.cee_me.servlet;

import static com.appspot.cee_me.OfyService.ofy;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLDecoder;

import javax.servlet.http.*;

import com.appspot.cee_me.CeeMeContext;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.model.*;
import com.appspot.cee_me.status.*;


public abstract class CeeMeServletBase extends HttpServlet {

    final static UserService userService = UserServiceFactory.getUserService();
    final static MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    private final static Logger log = Logger.getLogger(CeeMeServletBase.class.getName());

    static {
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    /**
     * Set up things that are common to all pages including the currently logged in user, if any.
     *
     * @param req servlet request
     */
    static CeeMeContext InitializeContext(HttpServletRequest req) {

        // The Google User object - MAY BE NULL! if not logged in
        User guser = userService.getCurrentUser();
        CUser cuser = null;    // The CeeMe User object - MAY BE NULL! if not logged in
        CeeMeContext context = new CeeMeContext(guser, cuser);

        if (guser == null) {
            context.setLoginURL(userService.createLoginURL(req.getRequestURI()));
        } else {
            String logoutURLCacheKey = guser.getUserId() + "_logoutURL";
            String logoutURL = (String) syncCache.get(logoutURLCacheKey);
            if (logoutURL == null) {
                logoutURL = userService.createLogoutURL(req.getRequestURI());
                syncCache.put(logoutURLCacheKey, logoutURL, Expiration.byDeltaSeconds(60 * 20));
            }
            context.setLogoutURL(logoutURL);
        }


        // Automatically create a CUser for any Google Users we recognize
        try {
            if (guser != null) {
                cuser = getOrCreateUserRecord(guser);
            }
        } catch (UserCreateException e) {
            log.severe("UserCreateException");
            e.printStackTrace(System.err);
            alertError(req, "You must provide an account name and a real name.");
        }

        context.setReq(req);
        context.setGuser(guser);
        context.setCuser(cuser);
        context.setCssThemeFile(getCSSTheme(req));
        req.setAttribute("Context", context);
        return context;
    }

    private static String getCSSTheme(HttpServletRequest req) {

        String defaultCssTheme = "/bootstrap/css/bootstrap.flatly.min.css";
        String cssTheme = defaultCssTheme;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ceeme-theme")) {
                    try {
                        cssTheme = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        log.warning("Cookie encoding error for ceeme-theme");
                        e.printStackTrace();
                    }
                }
            }
        }
        if (cssTheme == null || cssTheme.length() == 0) {
            cssTheme = defaultCssTheme;
        }
        return cssTheme;

    }

    public static CUser getOrCreateUserRecord(User guser) throws UserCreateException {
        CUser cuser = null;
        // Automatically create a CUser for any Google Users we recognize
        if (guser != null) {
            String normEmail = Config.norm(guser.getEmail());
            log.info("Searching for user " + normEmail);
            cuser = ofy().load().type(CUser.class)
                    .filter("accountName", normEmail).first().now();
            if (cuser == null) {
                log.info("Creating New User! " + guser + " guser ID: " + normEmail);
                cuser = createUser(guser);
                log.fine("New User cuser id: " + cuser.getId());
            }

        }

        return cuser;
    }

    static class UserCreateException extends RuntimeException {
        public UserCreateException(String message) {
            super(message);
        }
    }

    private static CUser createUser(User guser) throws UserCreateException {
        String accountName = Config.norm(guser.getEmail()); // needs to be lowercase
        String realName = guser.getNickname();

        if (accountName.length() == 0 || realName.length() == 0) {
            String msg = "You must provide an account name and a real name.";
            log.warning(msg + " --> guser: " + guser);
            throw new UserCreateException(msg);
        }

        CUser thisUser = new CUser(null, accountName, realName);
        thisUser.setGuser(guser);
        ofy().save().entities(thisUser).now();

        log.info("Created a new user account! " + accountName);

        return thisUser;
    }

    private static final long serialVersionUID = 7414103509881465189L;

    static void alertError(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.ERROR, msg);
    }

    static void alertSuccess(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.SUCCESS, msg);
    }

    static void alertInfo(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.INFO, msg);
    }

    static void alertWarning(HttpServletRequest req, String msg) {
        alertMessage(req, StatusMessageType.WARNING, msg);
    }

    private static void alertMessage(HttpServletRequest req, StatusMessageType msgType,
                                     String msg) {
        HttpSession session = req.getSession(true);
        StatusHandler.addStatus(session, new StatusMessage(msgType, msg));
    }

}
