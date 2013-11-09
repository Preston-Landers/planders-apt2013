package com.appspot.cee_me;

import com.appspot.cee_me.model.CUser;
import com.google.appengine.api.users.User;

/**
 * An object to represent the current servlet request context
 * things like the CeeMe User (CUser), Google User (guser), etc.
 */
public class CeeMeContext {
    private User guser;     // The Google User object - MAY BE NULL! if not logged in
    private CUser cuser;    // The CeeMe User object - MAY BE NULL! if not logged in
    private String cssThemeFile;
    private String loginURL;
    private String logoutURL;

    public CeeMeContext(User guser, CUser cuser) {
        this.guser = guser;
        this.cuser = cuser;
    }

    public User getGuser() {
        return guser;
    }

    public void setGuser(User guser) {
        this.guser = guser;
    }

    public CUser getCuser() {
        return cuser;
    }

    public void setCuser(CUser cuser) {
        this.cuser = cuser;
    }

    public String getCssThemeFile() {
        return cssThemeFile;
    }

    public void setCssThemeFile(String cssThemeFile) {
        this.cssThemeFile = cssThemeFile;
    }

    public String getProductName() {
        return Config.productName;
    }

    public String getProductURL() {
        return Config.productURL;
    }

    public String getLoginURL() {
        return loginURL;
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    public String getLogoutURL() {
        return logoutURL;
    }

    public void setLogoutURL(String logoutURL) {
        this.logoutURL = logoutURL;
    }
}
