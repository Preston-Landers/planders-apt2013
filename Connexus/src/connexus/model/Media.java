package connexus.model;

import static connexus.OfyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
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
		this.views = (long) 0;
	}
	
	public Key<Media> getKey() {
		return Key.create(stream, Media.class, id);
	}
	
	public static Media getById(Long objectId, Stream stream) {	
		return ofy().load().type(Media.class).parent(stream).id(objectId).get();
	}


	public String toString() {
		return "Media " + id + " : " + mimeType + " : " + fileName;
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

	public void setUploader(Key<CUser> uploader) {
		this.uploader = uploader;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
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
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		return imagesService.getServingUrl(getBlobKey()).replace("0.0.0.0", "127.0.0.1");
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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