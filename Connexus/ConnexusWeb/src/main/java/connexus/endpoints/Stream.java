package connexus.endpoints;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Endpoint model to represent a single Stream, which is itself a collection of Media.
 */
public class Stream implements Serializable {

    long id;
    String name;
    long ownerId;
    String ownerEmail;
    String coverURL;
    List<String> tags;
    Date creationDate;
    Date lastNewMedia;
    long numberOfMedia;
    long views;
    long trendingViews;

    int queryLimit;
    int queryOffset;
    int queryIndex;
    int mediaLimit;
    int mediaOffset;
    List<Media> mediaList;

    public Stream() {}

    public Stream(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getNumberOfMedia() {
        return numberOfMedia;
    }

    public void setNumberOfMedia(long numberOfMedia) {
        this.numberOfMedia = numberOfMedia;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getTrendingViews() {
        return trendingViews;
    }

    public void setTrendingViews(long trendingViews) {
        this.trendingViews = trendingViews;
    }

    public Date getLastNewMedia() {
        return lastNewMedia;
    }

    public void setLastNewMedia(Date lastNewMedia) {
        this.lastNewMedia = lastNewMedia;
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

    public int getQueryOffset() {
        return queryOffset;
    }

    public void setQueryOffset(int queryOffset) {
        this.queryOffset = queryOffset;
    }

    public int getMediaLimit() {
        return mediaLimit;
    }

    public void setMediaLimit(int mediaLimit) {
        this.mediaLimit = mediaLimit;
    }

    public int getMediaOffset() {
        return mediaOffset;
    }

    public void setMediaOffset(int mediaOffset) {
        this.mediaOffset = mediaOffset;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}