package connexus.model;

import static connexus.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotNull;

@Entity

public class Stream implements Comparable<Stream> {
	@Id Long id;
	@Parent Key<CUser> owner;
	@Index({IfNotNull.class}) String name; 
	
	String coverURL;
	@Index({IfNotNull.class}) List<String> tags;
	Date creationDate;

	@SuppressWarnings("unused")
	private Stream() {
	}

	public Stream(Long id, Key<CUser> owner, String name) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.creationDate = new Date();
	}
	
	public Key<Stream> getKey() {
		return Key.create(owner, Stream.class, id);
	}
	
	public static Stream getById(Long objectId, CUser cuser) {	
		return ofy().load().type(Stream.class).parent(cuser).id(objectId).get();
	}


	public String toString() {
		return "Stream " + id + " : " + name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Key<CUser> getOwner() {
		return owner;
	}

	public void setOwner(Key<CUser> owner) {
		this.owner = owner;
	}

	public String getCoverURL() {
		return coverURL;
	}

	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getLastNewMedia() {
		return "LAST NEW MEDIA";
	}
	
	public String getNumberOfMedia() {
		return "NUM OF ITEMS";
	}
	
	@Override
	public int compareTo(Stream other) {
		if (creationDate.after(other.creationDate)) {
			return 1;
		} else if (creationDate.before(other.creationDate)) {
			return -1;
		}
		return 0;
	}
}