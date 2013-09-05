package connexus.model;

import java.util.Date;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity

public class CUser implements Comparable<CUser> {
	@Id Long id;
	@Parent Key<Site> site;
	String accountName; // Displayed username
	@Index String realName; // full name
	User guser;      // google account
	String content;
	Date creationDate;

	@SuppressWarnings("unused")
	private CUser() {
	}

	public CUser(Long id, Key<Site> site, String accountName, String realName) {
		this.id = id;
		this.site = site;
		this.accountName = accountName;
		this.realName = realName;
		this.creationDate = new Date();
	}
	
	public String toString() {
		return "CUser " + accountName + " " + realName;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public User getGUser() {
		return guser;
	}

	public String getContent() {
		return content;
	}

	@Override
	public int compareTo(CUser other) {
		if (creationDate.after(other.creationDate)) {
			return 1;
		} else if (creationDate.before(other.creationDate)) {
			return -1;
		}
		return 0;
	}
}