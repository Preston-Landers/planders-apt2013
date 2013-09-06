package connexus.model;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity

public class Media implements Comparable<Media> {
	@Id Long id;
	@Parent Key<Stream> stream;
	@Index String fileName; 
	
	String mimeType;
	Date creationDate; 
	Key<CUser> uploader; // in theory, could upload to other users streams?
	@Index Long views;
	byte[] blob; // the item itself

	@SuppressWarnings("unused")
	private Media() {
	}

	public Media(Long id, Key<Stream> stream, String fileName) {
		this.id = id;
		this.stream = stream;
		this.fileName = fileName;
		this.creationDate = new Date();
	}
	
	public Key<Media> getKey() {
		return Key.create(stream, Media.class, id);
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

	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
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