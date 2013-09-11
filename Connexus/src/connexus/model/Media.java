package connexus.model;

import static connexus.OfyService.ofy;

import java.util.Date;
// import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotNull;

@Entity

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

	private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();

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
		rv.append("<h6>");
		rv.append("<em>Uploaded on:</em><BR>" + creationDate);
		rv.append("<BR><BR><em>Uploaded by:</em><BR>");
		rv.append(getUploaderNow().getRealName());
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
	public String getMediaServingURL() {
		// TODO: check if not image and use the other service
		// return imagesService.getServingUrl(getBlobKey()).replace("0.0.0.0", "192.168.1.99");
		try {
			return imagesService.getServingUrl(getBlobKey());
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			return "";
		}
		
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
			// TODO: don't know if it's just the development environment 
			// but often the blobstore delete fails... I get this:
			//
			// WARNING: Could not delete blob: <BlobKey: PEyjT0AA8BOvzTqVuWoCvw>
			// java.io.IOException: Could not delete: D:\APTWorkspace\preston_landers\Connexus\war\WEB-INF\appengine-generated\PEyjT0AA8BOvzTqVuWoCvw
			e.printStackTrace(System.err);
			return false;
		}
		
		//ImagesService imagesService = ImagesServiceFactory.getImagesService();
		//imagesService.deleteServingUrl(getBlobKey());
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
}