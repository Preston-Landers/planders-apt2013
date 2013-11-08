package com.appspot.cee_me.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.joda.time.DateTime;

/**
 * Represents a message sent through CeeMe.
 */
@Entity
@Cache
public class Message {
    @Id
    Long id;

    Key<Device> fromDevice;
    Key<CUser> fromUser;
    Key<CUser> toUser;
    Key<Device> toDevice;

    Key<Media> media;

    DateTime creationDate;
    DateTime lastRetrievalDate;

    private Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<CUser> getFromUser() {
        return fromUser;
    }

    public void setFromUser(Key<CUser> fromUser) {
        this.fromUser = fromUser;
    }

    public Key<CUser> getToUser() {
        return toUser;
    }

    public void setToUser(Key<CUser> toUser) {
        this.toUser = toUser;
    }

    public Key<Device> getToDevice() {
        return toDevice;
    }

    public void setToDevice(Key<Device> toDevice) {
        this.toDevice = toDevice;
    }

    public Key<Media> getMedia() {
        return media;
    }

    public void setMedia(Key<Media> media) {
        this.media = media;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getLastRetrievalDate() {
        return lastRetrievalDate;
    }

    public void setLastRetrievalDate(DateTime lastRetrievalDate) {
        this.lastRetrievalDate = lastRetrievalDate;
    }

    public Key<Device> getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(Key<Device> fromDevice) {
        this.fromDevice = fromDevice;
    }
}
