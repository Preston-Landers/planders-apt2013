package connexus;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import connexus.model.CUser;
import connexus.model.Site;

/**
 * An object to represent the current servlet request context
 * things like the Connexus User (CUser), Google User (guser), etc.
 */
public class ConnexusContext {
    protected Ref<Site> site; // The "Site" entity (usually just one exists)
    protected User guser;     // The Google User object - MAY BE NULL! if not logged in
    protected CUser cuser;    // The Connexus User object - MAY BE NULL! if not logged in

    public ConnexusContext(Ref<Site> site, User guser, CUser cuser) {
        this.site = site;
        this.guser = guser;
        this.cuser = cuser;
    }

    public Ref<Site> getSite() {
        return site;
    }

    public void setSite(Ref<Site> site) {
        this.site = site;
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
