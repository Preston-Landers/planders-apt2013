package connexus.endpoints;

import java.io.Serializable;

/**
 * Contains a one-time-use upload URL for uploading images.
 */
public class UploadUrl implements Serializable {
    String uploadUrl;
    Long streamId;
    Long streamOwnerId;

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
}
