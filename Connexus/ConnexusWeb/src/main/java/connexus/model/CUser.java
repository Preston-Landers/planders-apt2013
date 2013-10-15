package connexus.model;

import static connexus.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import connexus.Config;

@Entity
@Cache
public class CUser implements Comparable<CUser> {
	@Id Long id;
	@Parent Key<Site> site;
	@Index String accountName; // Displayed username
	String realName; // full name
	@Index User guser;      // google account
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
	
	public Key<CUser> getKey() {
		return Key.create(site, CUser.class, id);
	}
	
	public static CUser getById(Long userId, Key<Site> site) {
		return ofy().load().type(CUser.class).parent(site).id(userId).get();
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

    public static void normalizeAllAccountNames(Key<Site> siteKey) {
        if (siteKey == null) {
            siteKey = Site.load(null).getKey();
        }
        List<CUser> allUsers = ofy().load().type(CUser.class).ancestor(siteKey).list();
        for (CUser cUser : allUsers) {
            cUser.setAccountName(Config.norm(cUser.getAccountName()));
            ofy().save().entities(cUser);
        }

    }

}