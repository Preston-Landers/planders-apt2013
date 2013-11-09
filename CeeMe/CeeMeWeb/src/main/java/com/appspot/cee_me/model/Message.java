package com.appspot.cee_me.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.joda.time.DateTime;

import static com.appspot.cee_me.OfyService.ofy;

/**
 * Represents a message sent through CeeMe.
 */
@Entity
@Cache
public class Message {
    private @Id
    Long id;

    private Key<Device> fromDevice;
    private Key<CUser> fromUser;
    private Key<CUser> toUser;
    private Key<Device> toDevice;

    // File attachment
    private Key<Media> media;

    // String message payload
    // NOT indexed!! Can be up to 1 MB strings
    private String text;

    private DateTime creationDate;
    private DateTime lastRetrievalDate;
    private Boolean accepted;

    private Message() {
        creationDate = new DateTime();
        accepted = Boolean.FALSE;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text=<" + text +
                ">, id=" + id +
                ", fromDevice=" + fromDevice +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", toDevice=" + toDevice +
                ", media=" + media +
                ", creationDate=" + creationDate +
                '}';
    }

    /**
     * Saves the entity to the data store.
     *
     * @param now set to true if the save should be finished before returning.
     */
    public void save(boolean now) {
        Result result = ofy().save().entity(this);
        if (now) {
            result.now();
        }
    }

    /**
     * Creates a Message entity (saves it to datastore)
     * but does NOT initiate delivery
     * @param fromDevice source device (if any)
     * @param fromUser source user
     * @param toUser destination user
     * @param toDevice destination device
     * @param media media attachment
     * @param text text payload / message
     * @return the saved Message object
     */
    public static Message createMessage(
            Key<Device> fromDevice,
            Key<CUser> fromUser,
            Key<CUser> toUser,
            Key<Device> toDevice,
            Key<Media> media,

            // String message payload
            // NOT indexed!! Can be up to 1 MB strings
            String text
    ) {
        Message message = new Message();
        message.setFromDevice(fromDevice);
        message.setToDevice(toDevice);
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setMedia(media);
        message.setText(text);

        message.save(true);

        return message;
    }
}
