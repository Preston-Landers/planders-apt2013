package connexus.model;

import static connexus.OfyService.ofy;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.Utils;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotNull;

import connexus.Config;

@Entity

public class Stream implements Comparable<Stream> {
	@Id Long id;
	@Parent Key<CUser> owner;
	@Index({IfNotNull.class}) String name; 
	
	String coverURL;
	@Index({IfNotNull.class}) List<String> tags;
	@Index Date creationDate;

	@Index Long views;
	
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


	public String getViewURI() {
		// Use URIBuilder?
		List<String[]> params = new ArrayList<String[]>();
		params.add(new String[] {"v", getObjectURI()});
		return Config.getURIWithParams("/view", params);
	}
	
	public String getObjectURI() {
		return owner.getId() + ":" + id;  
	}
	
	// Doesn't really belong here...
	public static String getSearchURI(String term) {
		try {
			return "/search?q=" + java.net.URLEncoder.encode(term, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Good luck!
			return "/search?q=" + term;
		}
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

	
	public String getOwnerName() {
		return ofy().load().key(owner).get().getRealName();
	}

	public Date getLastNewMediaDate() {
		List<Media> ml = getMedia(0, 1);
		if (ml == null || ml.isEmpty()) {
			return null;
		}
		Media lastMedia = ml.get(0);
		return lastMedia.getCreationDate();
	}
	
	
	public String getLastNewMedia() {
		Date lnmDate = getLastNewMediaDate();
		if (lnmDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return df.format(lnmDate);
	}
	
	public int getNumberOfMedia() {
		return ofy().load().type(Media.class).ancestor(this).count();
	}
	
	public boolean deleteStream() {
		// TODO: logging and TRANSACTION...
		System.err.println("Deleting stream and all its media: " + this);
		
		// TODO Delete all Media in this stream
		for (Media media : getMedia(0, 0)) {
			media.deleteMedia();
		}
		
		// Nuke it
		ofy().delete().entities(this).now();
		return true;
	}
	
	/**
	 * Returns ALL streams available in the system, sorted by most recently updated first.
	 */
	public static List<Stream> getAllStreams(Ref<Site> site) {
		// order by getLastNewMedia()
		List<Stream> rv = ofy().load().type(Stream.class).ancestor(site).list();
		Collections.sort(rv);
		Collections.reverse(rv);
		return rv;	
	}

	/*
	 * Get a list of media for this stream sorted by creation date
	 */
	public List<Media> getMedia(int offset, int limit) {
		return ofy().load().type(Media.class).ancestor(this).order("-creationDate")
				.offset(offset).limit(limit).list();
	}
	
	
	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}
	
	public Long getAndIncrementViews() {
		Long views = getViews();
		if (views == null) {
			views = new Long(0);
		}
		views++;
		setViews(views);
		save();
		return views;
	}

	public void save() {
		ofy().save().entities(this).now();
	}
	
	@Override
	public int compareTo(Stream other) {
		if (getLastNewMediaDate().after(other.getLastNewMediaDate())) {
			return 1;
		} else if (getLastNewMediaDate().before(other.getLastNewMediaDate())) {
			return -1;
		}
		return 0;
	}

}