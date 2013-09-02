package connexus;

import java.util.Date;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity

public class CUser implements Comparable<CUser> {
	@Id
	Long id;
	String realName; // full name
	String userName; // Displayed username
	User guser;      // google account
	String content;
	Date creationDate;

	private CUser() {
	}

	public CUser(User gUser, String content) {
		this.guser = gUser;
		this.content = content;
		creationDate = new Date();
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