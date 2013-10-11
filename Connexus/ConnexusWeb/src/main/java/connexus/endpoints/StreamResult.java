package connexus.endpoints;

import java.io.Serializable;
import java.util.List;

/**
 * Contains images for a single stream, and an upload url if you are able to upload.
 */
public class StreamResult implements Serializable {
    Long streamId;
    Long streamOwnerId;
    Long myId;
    List<Media> mediaList;
    int resultSize;
    boolean canUpload;
    String uploadUrl;
    int queryLimit;
    int queryOffset;


    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public Long getStreamOwnerId() {
        return streamOwnerId;
    }

    public void setStreamOwnerId(Long streamOwnerId) {
        this.streamOwnerId = streamOwnerId;
    }

    public Long getMyId() {
        return myId;
    }

    public void setMyId(Long myId) {
        this.myId = myId;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public boolean isCanUpload() {
        return canUpload;
    }

    public void setCanUpload(boolean canUpload) {
        this.canUpload = canUpload;
    }

    public int getResultSize() {
        return resultSize;
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public int getQueryLimit() {
        return queryLimit;
    }

    public void setQueryLimit(int queryLimit) {
        this.queryLimit = queryLimit;
    }

    public int getQueryOffset() {
        return queryOffset;
    }

    public void setQueryOffset(int queryOffset) {
        this.queryOffset = queryOffset;
    }
}
