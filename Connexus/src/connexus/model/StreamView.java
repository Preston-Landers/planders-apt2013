package connexus.model;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/**
 * Represents a page view for a stream.
 * Used for the trending reports.
 */
public class StreamView implements Comparable<StreamView> {
	@Id Long id;
	@Index Key<Stream> stream;
	@Index Date date;

	@SuppressWarnings("unused")
	private StreamView() {
	}

	public StreamView(Long id, Key<Stream> stream) {
		this.id = id;
		this.stream = stream;
		this.date= new Date();
	}

	public Key<StreamView> getKey() {
		return Key.create(StreamView.class, id);
	}
	
	public String toString() {
		return "StreamView " + id + " " + date;
	}
	
	public Long getId() {
		return id;
	}

//	public void setId(Long id) {
//		this.id = id;
//	}


	public Date getDate() {
		return date;
	}


	@Override
	public int compareTo(StreamView other) {
		if (date.after(other.getDate())) {
			return 1;
		} else if (date.before(other.getDate())) {
			return -1;
		}
		return 0;
	}
}