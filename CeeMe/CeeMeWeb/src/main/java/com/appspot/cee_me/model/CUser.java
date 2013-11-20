package com.appspot.cee_me.model;

import static com.appspot.cee_me.OfyService.ofy;


import com.appspot.cee_me.Config;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.*;
import org.joda.time.DateTime;

import java.util.logging.Level;
import java.util.logging.Logger;


@Entity
@Cache
public class CUser implements Comparable<CUser> {
    private
    @Id
    Long id;
    private
    @Index
    String accountName; // Displayed username
    private String realName; // full name
    private
    @Index
    User guser;      // google account  // Need getByGUser for this to be useful?
    private String adminNotes;    // private / internal notes for this account.
    private DateTime creationDate;

    @SuppressWarnings("unused")
    private CUser() {
    }

    public CUser(Long id, String accountName, String realName) {
        setId(id);
        setAccountName(accountName);
        setRealName(realName);
        setCreationDate(new DateTime());
        setAdminNotes(null);
    }

    public Key<CUser> getKey() {
        return Key.create(CUser.class, id);
    }

    public static CUser getById(Long userId) {
        return ofy().load().type(CUser.class).id(userId).now();
    }

    public static CUser getByKey(String objectIdStr) {
        Key<CUser> key = Key.create(objectIdStr);
        return getByKey(key);
    }

    public static CUser getByKey(Key<CUser> userKey) {
        return ofy().load().key(userKey).now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public User getGuser() {
        return guser;
    }

    public void setGuser(User guser) {
        this.guser = guser;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }


    public String getAdminNotes() {
        return adminNotes;
    }

    // Does compare by creation date even make sense?
    @Override
    public int compareTo(CUser other) {
        return getCreationDate().compareTo(other.getCreationDate());
    }

    @Override
    public String toString() {
        return "CUser{" +
                "id=" + getId() +
                ", accountName='" + getAccountName() + '\'' +
                ", realName='" + getRealName() + '\'' +
                ", guser=" + getGuser() +
                ", adminNotes='" + getAdminNotes() + '\'' +
                ", creationDate=" + getCreationDate() +
                '}';
    }

    public static boolean isUserAdmin() {
        try {
            UserService userService = UserServiceFactory.getUserService();
            if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
                return true;
            }
        } catch (Exception e) {
            final Logger log = Logger.getLogger(CUser.class.getName());
            log.log(Level.WARNING, "Unable to check if g-user is logged in!", e);
        }
        return false;
    }

    public static void fixAllUserAccountNames() {
        for (CUser user :
                ofy().load().type(CUser.class).list()) {
            user.setAccountName(
                    Config.norm(
                            user.getAccountName()
                    )
            );
            user.save(false);
        }
    }

    /**
     * Saves the entity to the data store.
     *
     * @param now set to true if the save should be finished before returning.
     */
    public void save(boolean now) {
        Result result = ofy().save().entity(this);
        if (now) {
            result.now();
        }
    }

}