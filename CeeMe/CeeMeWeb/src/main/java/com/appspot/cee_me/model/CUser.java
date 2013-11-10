package com.appspot.cee_me.model;

import static com.appspot.cee_me.OfyService.ofy;


import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import org.joda.time.DateTime;


@Entity
@Cache
public class CUser implements Comparable<CUser> {
	private @Id Long id;
    private String accountName; // Displayed username
    private String realName; // full name
    private @Index User guser;      // google account  // Need getByGUser for this to be useful?
    private String adminNotes;    // private / internal notes for this account.
    private DateTime creationDate;

	@SuppressWarnings("unused")
	private CUser() {
	}

	public CUser(Long id, String accountName, String realName) {
		this.id = id;
		this.accountName = accountName;
		this.realName = realName;
		this.creationDate = new DateTime();
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


	public User getGUser() {
		return guser;
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
                "id=" + id +
                ", accountName='" + accountName + '\'' +
                ", realName='" + realName + '\'' +
                ", guser=" + guser +
                ", adminNotes='" + adminNotes + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}