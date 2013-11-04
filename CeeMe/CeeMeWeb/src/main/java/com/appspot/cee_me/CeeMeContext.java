package com.appspot.cee_me;

import com.appspot.cee_me.model.CUser;
import com.google.appengine.api.users.User;

/**
 * An object to represent the current servlet request context
 * things like the CeeMe User (CUser), Google User (guser), etc.
 */
public class CeeMeContext {
    protected User guser;     // The Google User object - MAY BE NULL! if not logged in
    protected CUser cuser;    // The CeeMe User object - MAY BE NULL! if not logged in

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
}
