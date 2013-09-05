package connexus.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
/**
 * Ancestor entity for everything within a logical site
 */
public class Site implements Comparable<Site> {
	@Id Long id;
	String name;
	Date creationDate;

	@SuppressWarnings("unused")
	private Site() {
	}

	public Site(Long _id, String _name) {
		id = _id;
		name = _name;
		this.creationDate = new Date();
	}
	
	public String toString() {
		return "Site " + id + " " + name + " " + creationDate;
	}
	
	public Long getId() {
		return id;
	}

//	public void setId(Long id) {
//		this.id = id;
//	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public int compareTo(Site other) {
		if (creationDate.after(other.creationDate)) {
			return 1;
		} else if (creationDate.before(other.creationDate)) {
			return -1;
		}
		return 0;
	}
}