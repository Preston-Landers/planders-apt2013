package connexus.endpoints;

import java.util.Date;

/**
 * Endpoint model to represent a single image or other media object.
 */
public class Media {
    long id;
    long streamOwnerId;
    long streamId;
    String url;
    String thumbUrl;
    String fileName;
    String mimeType;
    long size;
    String comments;
    Date creationDate;
    String uploader;
    long views;

    int queryOffset;
    int queryLimit;
    int queryIndex;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStreamOwnerId() {
        return streamOwnerId;
    }

    public void setStreamOwnerId(long ownerId) {
        this.streamOwnerId = ownerId;
    }

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public int getQueryOffset() {
        return queryOffset;
    }

    public void setQueryOffset(int queryOffset) {
        this.queryOffset = queryOffset;
    }

    public int getQueryLimit() {
        return queryLimit;
    }

    public void setQueryLimit(int queryLimit) {
        this.queryLimit = queryLimit;
    }

    public int getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(int queryIndex) {
        this.queryIndex = queryIndex;
    }
}
