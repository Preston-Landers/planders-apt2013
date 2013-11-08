package com.appspot.cee_me.model;

import static com.appspot.cee_me.OfyService.ofy;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotNull;

import com.appspot.cee_me.Config;
import org.joda.time.DateTime;

@Entity
@Cache
public class Media implements Comparable<Media> {

    @Id Long id;

    ShortBlob sha256;

    String fileName;

    @Index BlobKey blobKey;

    String mimeType;

    Long size;

    String comments;

    @Index
    DateTime creationDate;

    Key<CUser> uploader; // in theory, could upload to other users streams?

    Long views;

    Double latitude;
    Double longitude;

	private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
	protected final static MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	
	static {
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}
	
	// memcache key for image URL
	private static class CacheKeyImageURL implements Serializable {
		private static final long serialVersionUID = 5690465575728397584L;
		@SuppressWarnings("unused")
		private final BlobKey blobKey;
		@SuppressWarnings("unused")
		private final int size;
		public CacheKeyImageURL(BlobKey blobKey, int size) {
			this.blobKey = blobKey;
			this.size = size;
		}
	}
	
	@SuppressWarnings("unused")
	private Media() {
	}

	public Media(Long id, BlobKey blobKey, Key<CUser> cuser) {
		this.id = id;
		this.blobKey = blobKey;
		this.uploader = cuser;
		this.creationDate = new DateTime();
		this.views = new Long(0);
	}
	
	public Key<Media> getKey() {
		return Key.create(Media.class, id);
	}
	
	public static Media getById(Long objectId) {
		return ofy().load().type(Media.class).id(objectId).get();
	}


	public String toString() {
		return "Media " + id + " U: " + uploader.getId() + " : " + mimeType + " : " + fileName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}


	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getThumbnailDescription() {
		// TODO: get rid of yucky HTML
		StringBuilder rv = new StringBuilder();
		rv.append("<h4>");
		rv.append(Config.escapeHTML(getComments()));
		rv.append("</h4>");
		
		rv.append("<h6>");
		rv.append("<em>Uploaded on:</em><BR>" + creationDate);
		rv.append("<BR><BR><em>Uploaded by:</em><BR>");
		rv.append(Config.escapeHTML(getUploaderNow().getRealName()));
		rv.append("</h6>" );
		return rv.toString();
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Key<CUser> getUploader() {
		return uploader;
	}
	public CUser getUploaderNow() {
		return ofy().load().key(getUploader()).get();
	}

	public void setUploader(Key<CUser> uploader) {
		this.uploader = uploader;
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
		save(false); // async
		return views;		
	}
	
	public void save() {
        save(true);
	}

    public void save(boolean now) {
        if (now) {
            ofy().save().entities(this).now();
        } else {
            ofy().save().entities(this);
        }
    }

	public BlobKey getBlobKey() {
		return blobKey;
	}

	public void setBlobKey(BlobKey blobKey) {
		this.blobKey = blobKey;
	}

	public Long getSize() {
		return size;
	}
	
	public void setSize(Long size) {
		this.size = size;
	}
	
	
	/**
	 * Return a list of image serving URLs for the given media 
	 * @WARNING: does not check whether the media is actually an image! 
	 */
	@SuppressWarnings("deprecation")
	public String getMediaURLHelper(BlobKey blobKey, int size) {
		CacheKeyImageURL cacheKeyImageURL = new CacheKeyImageURL(blobKey, size);
		// syncCache
		String cachedURL = (String) syncCache.get(cacheKeyImageURL);
		if (cachedURL != null) {
			// System.err.println("Cache hit on image " + cachedURL);
			return cachedURL;
		}
		try {
			String rv = imagesService.getServingUrl(blobKey, size, false);
			// System.err.println("Cache miss on image " + rv);
			syncCache.put(cacheKeyImageURL, rv);
			return rv;
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			return "";
		}
			
	}

	public String getMediaServingURLRaw() {
		return getMediaURLHelper(getBlobKey(), 0);
	}

	public String getMediaServingURL() {
		return getMediaURLHelper(getBlobKey(), 1024); // XXX TODO - don't hardcode
	}

	public String getThumbURL() {
		return getMediaURLHelper(getBlobKey(), 300);
		
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean deleteMedia() {
        Logger log = Logger.getLogger(getClass().getName());
        log.warning("Deleting media: " + this);

		// ignoring error result here...
		deleteBlob();
		ofy().delete().entities(this).now();
		return true;
	}

	private boolean deleteBlob() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		try {
			blobstoreService.delete(getBlobKey());
		} catch (BlobstoreFailureException e) {
			e.printStackTrace(System.err);
			return false;
		}
		
		return true;
	}
	
	@Override
	public int compareTo(Media other) {
        return getCreationDate().compareTo(other.getCreationDate());
	}

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ShortBlob getSha256() {
        return sha256;
    }

    public void setSha256(ShortBlob sha256) {
        this.sha256 = sha256;
    }
}
