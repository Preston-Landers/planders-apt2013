package connexus.model;

import static connexus.OfyService.ofy;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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

import connexus.Config;

@Entity
@Cache
public class Media implements Comparable<Media> {
	@Id Long id;
	@Parent Key<Stream> stream;
	@Index({IfNotNull.class}) String fileName; 
	@Index BlobKey blobKey;
	@Index String blobKeyString;
	String mimeType;
	Long size;
	@Index({IfNotNull.class}) String comments;
	@Index Date creationDate; 
	Key<CUser> uploader; // in theory, could upload to other users streams?
	@Index Long views;

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

	public Media(Long id, Key<Stream> stream, BlobKey blobKey, String blobKeyString, Key<CUser> cuser) {
		this.id = id;
		this.stream = stream;
		this.blobKey = blobKey;
		this.blobKeyString = blobKeyString;
		this.uploader = cuser;
		this.creationDate = new Date();
		this.views = new Long(0);
	}
	
	public Key<Media> getKey() {
		return Key.create(stream, Media.class, id);
	}
	
	public static Media getById(Long objectId, Stream stream) {	
		return ofy().load().type(Media.class).parent(stream).id(objectId).get();
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


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
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

	public Key<Stream> getStream() {
		return stream;
	}

	public void setStream(Key<Stream> stream) {
		this.stream = stream;
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
		save();
		return views;		
	}
	
	public void save() {
		ofy().save().entities(this).now();
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
		return getMediaURLHelper(getBlobKey(), 768);
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
		// TODO: logging
		System.err.println("Deleting media: " + this);
		
		// return ofy().load().type(Media.class).parent(stream).id(objectId).get();
		Stream myStream = ofy().load().key(getStream()).get();
		if (myStream.getCoverURL().equals(getThumbURL())) {
			// String newCoverURL = myStream.getMedia(0, 1);
			String newCoverURL = null;
			myStream.setCoverURL(newCoverURL);
		}
		myStream.decNumberOfMedia();
		myStream.save();
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
		if (creationDate.after(other.creationDate)) {
			return 1;
		} else if (creationDate.before(other.creationDate)) {
			return -1;
		}
		return 0;
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
}