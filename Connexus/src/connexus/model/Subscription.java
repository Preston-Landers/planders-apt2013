package connexus.model;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
/**
 * A user's subscription to a stream which could potentially belong to another user.
 */
public class Subscription implements Comparable<Subscription> {
	@Id Long id;
	@Parent Key<CUser> owner;
	Key<Stream> stream;
	@Index Date creationDate;

	@SuppressWarnings("unused")
	private Subscription() {
	}

	public Subscription(Long _id, Key<CUser> _owner) {
		id = _id;
		owner = _owner;
		creationDate = new Date();
	}
	
	public Key<Subscription> getKey() {
		return Key.create(owner, Subscription.class, id);
	}

	public String toString() {
		return "Subscription " + id + " " + owner + " " +  stream;
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

	public Key<CUser> getOwner() {
		return owner;
	}

	public void setOwner(Key<CUser> owner) {
		this.owner = owner;
	}

	public Key<Stream> getStream() {
		return stream;
	}

	public void setStream(Key<Stream> stream) {
		this.stream = stream;
	}

	@Override
	public int compareTo(Subscription other) {
		if (creationDate.after(other.creationDate)) {
			return 1;
		} else if (creationDate.before(other.creationDate)) {
			return -1;
		}
		return 0;
	}
}