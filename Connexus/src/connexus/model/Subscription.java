package connexus.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static connexus.OfyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import connexus.StreamHandle;

@Entity
/**
 * A user's subscription to a stream which could potentially belong to another user.
 */
public class Subscription implements Comparable<Subscription> {
	@Id Long id;
	@Parent Key<CUser> owner;
	@Index Key<Stream> stream;
	@Index Date creationDate;

	@SuppressWarnings("unused")
	private Subscription() {
	}

	public Subscription(Long _id, Key<CUser> _owner, Key<Stream> _stream) {
		id = _id;
		owner = _owner;
		stream = _stream;
		creationDate = new Date();
	}
	
	public static List<Subscription> getSubscriptionsForUser(CUser user) {
		if (user == null) {
			return new ArrayList<Subscription>();
		}
		//List<Subscription> mySubs = new ArrayList<Subscription>();
		//return mySubs;
		return ofy().load().type(Subscription.class).ancestor(user).list();
	}
	
	/**
	 * Load the given user's subscription for this stream. If none exists, returns null.
	 */
	public static Subscription getUserSubscriptionFromStreamHandle(CUser user, StreamHandle streamHandle) {
		return ofy().load().type(Subscription.class).ancestor(user)
				.filter("stream ==", streamHandle.getStream().getKey()).first()
				.get();
		
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
	
	public Stream getStreamLoaded() {
		return ofy().load().key(getStream()).get();
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