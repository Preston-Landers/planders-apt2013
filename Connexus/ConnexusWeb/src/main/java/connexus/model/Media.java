package connexus.model;

import static connexus.OfyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

import com.javadocmd.simplelatlng.LatLng;
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

    /**
     * Returns true if this image has geo-coordinates
     * and they're not the 0,0 special value
     * @return
     */
    public boolean hasValidCoordinates() {
        Double zero = new Double(0.0);
        Double latitude = getLatitude();
        if (latitude == null || latitude.equals(zero)) {
            return false;
        }
        Double longitude = getLongitude();
        if (longitude == null || longitude.equals(zero)) {
            return false;
        }
        return true;
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

    public Long getStreamOwnerId() {
        Key<Stream> streamKey = getStream();
        Stream stream = ofy().load().key(streamKey).get();
        if (stream != null) {
            CUser streamOwner = ofy().load().key(stream.getOwner()).get();
            if (streamOwner != null) {
                return streamOwner.getId();
            }
        }
        return null;
    }

    /**
     * Perform a search of all media (images) sorted by proximity to a given location
     * Ignores media that doesn't have a location set.
     * @param limit
     * @param offset
     * @param latitude
     * @param longitude
     * @return
     */
    public static List<LocMedia> searchByLocation(int limit, int offset, double latitude, double longitude) {
        List<LocMedia> returnList = new ArrayList<LocMedia>();
        Site site = Site.load(null);
        if (limit == 0) {
            limit = 9;
        }

        LatLng searchPoint = new LatLng(latitude, longitude);

        List<LocMedia> locMedias = new ArrayList<LocMedia>();
        List<Media> allMedia = ofy().load().type(Media.class).ancestor(site).list();
        for (Media media : allMedia) {
            // System.err.println("Location search on media: " + media.getId());
            Double mLat = media.getLatitude();
            Double mLong = media.getLongitude();
            if (mLat == null || mLong == null) {
                continue;
            }
            if (mLat.equals(0.0) && mLong.equals(0.0)) {
                // System.err.println("Location search excluding 0/0 coords: " + media.getId());
                continue;
            }
            // System.err.println(" -> media: " + media.getId() + " has coords Lat: " + mLat + " long: " + mLong + " comment: " + media.getComments());
            LatLng mediaPoint = new LatLng(mLat, mLong);
            LocMedia locMedia = new LocMedia(mediaPoint, searchPoint, media);
            locMedias.add(locMedia);
        }
        Collections.sort(locMedias);
        int i = 0;
        int added = 0;
        for (LocMedia locMedia : locMedias) {
            // System.err.println("Loc search i: " + i + " media: " + locMedia.getMedia().getId() + " comment: " + locMedia.getMedia().getComments());
            if (i>=offset) {
                returnList.add(locMedia);
                added++;
            }
            i++;
            if (added >= limit) {
                break;
            }
        }
        System.err.println("Returning nearby search results length: " + returnList.size());
        return returnList;
    }
}
