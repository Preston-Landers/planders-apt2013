package com.appspot.cee_me.model;

import static com.appspot.cee_me.OfyService.ofy;


import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import org.joda.time.DateTime;


@Entity
@Cache
public class CUser implements Comparable<CUser> {
	@Id Long id;
	String accountName; // Displayed username
	String realName; // full name
	@Index User guser;      // google account  // Need getByGUser for this to be useful?
	String adminNotes;    // private / internal notes for this account.
	DateTime creationDate;

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

	
	public String toString() {
		return "CUser " + id + " " + accountName + " - " + guser;
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

}