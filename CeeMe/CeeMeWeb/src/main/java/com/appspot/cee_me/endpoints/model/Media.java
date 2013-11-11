package com.appspot.cee_me.endpoints.model;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Represents model.Media in the API endpoint
 */
public class Media implements Serializable {
    private String mediaKey;
    private byte[] sha256;
    private String fileName;
    private String mimeType;
    private long size;
    private String comments;
    private DateTime creationDate;
    private User uploader;
    private long views;
    private double latitude;
    private double longitude;

    // TODO: need URL to access or blobkey or sumpin

    public Media() {
    }

    public Media(com.appspot.cee_me.model.Media media) {
        if (media != null) {
            setMediaKey(media.getKey().getString());
            setSha256(media.getSha256().getBytes());
            setFileName(media.getFileName());
            setMimeType(media.getMimeType());
            setSize(media.getSize());
            setComments(media.getComments());
            setCreationDate(media.getCreationDate());
            setUploader(new User(media.getUploaderNow()));
            setViews(media.getViews());
            setLatitude(media.getLatitude());
            setLongitude(media.getLongitude());
        }
    }

    public String getMediaKey() {
        return mediaKey;
    }

    public void setMediaKey(String mediaKey) {
        this.mediaKey = mediaKey;
    }

    public byte[] getSha256() {
        return sha256;
    }

    public void setSha256(byte[] sha256) {
        this.sha256 = sha256;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
