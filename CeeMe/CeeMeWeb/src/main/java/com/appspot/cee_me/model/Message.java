package com.appspot.cee_me.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.cmd.Query;
import org.joda.time.DateTime;

import java.util.List;
import java.util.logging.Logger;

import static com.appspot.cee_me.OfyService.ofy;

/**
 * Represents a message sent through CeeMe.
 */
@Entity
@Cache
public class Message {
    private
    @Id
    Long id;

    private
    @Load
    Ref<Device> fromDevice;

    private
    @Load
    Ref<CUser> fromUser;

    private
    @Index
    @Load
    Ref<CUser> toUser;

    private
    @Index
    @Load
    Ref<Device> toDevice;

    // File attachment
    private
    @Load
    Ref<Media> media;

    // String message payload
    // NOT indexed!! Can be up to 1 MB strings
    private String text;

    private
    @Index
    DateTime creationDate;
    private DateTime lastRetrievalDate;
    private Boolean accepted; // has been read / opened / accepted

    private String openWithApp;
    private String openWithAppName;

    private Message() {
        setCreationDate(new DateTime());
        setAccepted(Boolean.FALSE);
        setLastRetrievalDate(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CUser getFromUser() {
        return fromUser == null ? null : fromUser.get();
    }

    public void setFromUser(Ref<CUser> fromUser) {
        this.fromUser = fromUser;
    }

    public CUser getToUser() {
        return toUser == null ? null : toUser.get();
    }

    public void setToUser(Ref<CUser> toUser) {
        this.toUser = toUser;
    }

    public Device getToDevice() {
        return toDevice == null ? null : toDevice.get();
    }

    public void setToDevice(Ref<Device> toDevice) {
        this.toDevice = toDevice;
    }

    public Media getMedia() {
        return media == null ? null : media.get();
    }

    public void setMedia(Ref<Media> media) {
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

    public Device getFromDevice() {
        return fromDevice == null ? null : fromDevice.get();
    }

    public void setFromDevice(Ref<Device> fromDevice) {
        this.fromDevice = fromDevice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getOpenWithApp() {
        return openWithApp;
    }

    public void setOpenWithApp(String openWithApp) {
        this.openWithApp = openWithApp;
    }

    public String getOpenWithAppName() {
        return openWithAppName;
    }

    public void setOpenWithAppName(String openWithAppName) {
        this.openWithAppName = openWithAppName;
    }

    public Key<Message> getKey() {
        return Key.create(Message.class, id);
    }

    /**
     * Load a message by its key string
     *
     * @param objectIdStr should be result of getKey().getString()
     * @return the Message
     */
    public static Message getByKey(String objectIdStr) {
        Key<Message> key = Key.create(objectIdStr);
        return getByKey(key);
    }

    /**
     * Load a Message by its datastore Key
     *
     * @param key message key to load
     * @return the Message
     */
    public static Message getByKey(Key<Message> key) {
        return ofy().load().key(key).now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "text=<" + getText() +
                ">, id=" + getId() +
                ", fromDevice=" + getFromDevice() +
                ", fromUser=" + getFromUser() +
                ", toUser=" + getToUser() +
                ", toDevice=" + getToDevice() +
                ", media=" + getMedia() +
                ", creationDate=" + getCreationDate() +
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
     *
     * @param fromDevice source device (if any)
     * @param fromUser   source user
     * @param toUser     destination user
     * @param toDevice   destination device
     * @param media      media attachment
     * @param text       text payload / message
     * @return the saved Message object
     */
    public static Message createMessage(
            Ref<Device> fromDevice,
            Ref<CUser> fromUser,
            Ref<CUser> toUser,
            Ref<Device> toDevice,
            Ref<Media> media,

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
        message.setText(text.trim());

        message.save(true);

        return message;
    }

    public void send() {
        // TODO: validate 'n send
    }

    /**
     * Is the given user allowed to read/retrieve this message?
     * @param cUserRef user reference
     * @return whether he can read this message
     */
    public boolean canUserRead(Ref<CUser> cUserRef) {
        return canUserDelete(cUserRef);
    }

    public boolean canUserDelete(Ref<CUser> cUserRef) {
        if (CUser.isUserAdmin()) {
            return true;
        }
        if (cUserRef != null) {
            if (getToUser().getKey().equals(cUserRef.getKey()) ||
                    getFromUser().getKey().equals(cUserRef.getKey())) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteMessage(boolean now) {
        Logger log = Logger.getLogger(getClass().getName());

        if (!canUserDelete(null)) {
            log.severe("Can't delete message");
            return false;
        }

        log.info("Deleting message: " + toString());

        Result result = ofy().delete().entities(this);
        if (now) {
            result.now();
        }
        return true;
    }

    /**
     * Retrieve a list of all messages for the given device.
     *
     * @param deviceKey the key of the device to check
     * @param since     only retrieve messages sent after this time
     * @param limit     limit number of results (0 == all)
     * @param offset    index into results with this offset
     * @return list of available messages
     */
    public static List<Message> getMessagesForDevice(Key<Device> deviceKey, DateTime since, int limit, int offset) {
        Query<Message> query = ofy().load().type(Message.class).filter("toDevice =", deviceKey);
        if (since != null) {
            query.filter("creationDate >=", since);
        }
        return query.limit(limit).offset(offset).order("creationDate").list();
    }

}
