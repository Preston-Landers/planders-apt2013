package connexus.model;

import java.util.Date;

import static connexus.OfyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import connexus.Config;

@Entity
@Cache
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

	public static Site load(Long _id) {
		if (_id == null)
			_id = Config.siteId;
        return ofy().load().type(Site.class).id(_id).get();
	}
	
	public Key<Site> getKey() {
		return Key.create(Site.class, id);
	}
	
	public String toString() {
		return "Site " + id + " " + name + " " + creationDate;
	}
	
	public Long getId() {
		return id;
	}

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