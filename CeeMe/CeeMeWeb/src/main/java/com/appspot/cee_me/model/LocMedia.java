package com.appspot.cee_me.model;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * Join together a Media object and its Location (LatLng) so we can sort
 * these by distance to the point of interest (Origin)
 *
 */
public class LocMedia implements Comparable<LocMedia> {
    private LatLng location;
    private LatLng origin;
    private Media media;
    private double distanceToOrigin;

    public LocMedia(LatLng location, LatLng origin, Media media) {
        this.location = location;
        this.origin = origin;
        this.media = media;
        distanceToOrigin = LatLngTool.distance(location, origin, LengthUnit.METER);
    }

    /**
     * Sorts in REVERSE order (smallest distance to origin first)
     * @param other The other point to compare to.
     * @return comparison integer: -1 if this is less than other, 0 if same, 1 if this is greater than other
     */
    @Override
    public int compareTo(LocMedia other) {
        double thisDistance = getDistanceToOrigin();
        double otherDistance = other.getDistanceToOrigin();
//        if (thisDistance > otherDistance) {
//            return -1;
//        } else if (thisDistance < otherDistance) {
//            return 1;
//        }
        if (thisDistance > otherDistance) {
            return 1;
        } else if (thisDistance < otherDistance) {
            return -1;
        }
        return 0;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public double getDistanceToOrigin() {
        return distanceToOrigin;
    }

    public void setDistanceToOrigin(double distanceToOrigin) {
        this.distanceToOrigin = distanceToOrigin;
    }
}
