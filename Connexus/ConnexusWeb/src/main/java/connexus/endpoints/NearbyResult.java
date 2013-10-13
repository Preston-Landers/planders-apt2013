package connexus.endpoints;

import java.io.Serializable;
import java.util.List;

/**
 * The return value of a "Nearby" location-based search.
 */
public class NearbyResult implements Serializable {
    double searchLatitude;
    double searchLongitude;
    Long myId;
    List<Media> mediaList;
    int queryLimit;
    int queryOffset;

    public double getSearchLatitude() {
        return searchLatitude;
    }

    public void setSearchLatitude(double searchLatitude) {
        this.searchLatitude = searchLatitude;
    }

    public double getSearchLongitude() {
        return searchLongitude;
    }

    public void setSearchLongitude(double searchLongitude) {
        this.searchLongitude = searchLongitude;
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
