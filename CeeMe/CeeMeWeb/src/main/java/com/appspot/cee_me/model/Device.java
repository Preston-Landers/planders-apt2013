package com.appspot.cee_me.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

import java.util.List;
import java.util.logging.Logger;

import static com.appspot.cee_me.OfyService.ofy;

/**
 * Represents a unique install on an Android device.
 * These are the targets of Messages.
 */
@Entity
@Cache
public class Device {
    private @Id
    Long id;

    private @Index
    String publicId; // goes into public URL

    private String name; // Displayed user-assignable name
    private String comment; // user-assignable extra comment field
    private String hardwareDescription; // something to identify the actual hardware (user-visible)

    private @Index
    Key<CUser> owner;

    private String gcmRegistrationId; // Google Cloud Messaging registration ID for this device.

    private DateTime creationDate;        // when was the device registered?
    private DateTime lastIncomingMessageDate; // when was this device named a target of a message?
    private DateTime lastOutgoingMessageDate; // when did this device last originate a message?

    @SuppressWarnings("unused")
    private Device() {
    }

    public Device(String name, String hardwareDescription, Key<CUser> owner, String gcmRegistrationId) {
        setName(name);
        setPublicId(null); /// XXX TODO
        setHardwareDescription(hardwareDescription);
        setOwner(owner);
        setGcmRegistrationId(gcmRegistrationId);
        setCreationDate(new DateTime());
        setLastIncomingMessageDate(null);
        setLastOutgoingMessageDate(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHardwareDescription() {
        return hardwareDescription;
    }

    public void setHardwareDescription(String hardwareDescription) {
        this.hardwareDescription = hardwareDescription;
    }

    public Key<CUser> getOwner() {
        return owner;
    }

    public void setOwner(Key<CUser> owner) {
        this.owner = owner;
    }

    public String getGcmRegistrationId() {
        return gcmRegistrationId;
    }

    public void setGcmRegistrationId(String gcmRegistrationId) {
        this.gcmRegistrationId = gcmRegistrationId;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getLastIncomingMessageDate() {
        return lastIncomingMessageDate;
    }

    public void setLastIncomingMessageDate(DateTime lastIncomingMessageDate) {
        this.lastIncomingMessageDate = lastIncomingMessageDate;
    }

    public DateTime getLastOutgoingMessageDate() {
        return lastOutgoingMessageDate;
    }

    public void setLastOutgoingMessageDate(DateTime lastOutgoingMessageDate) {
        this.lastOutgoingMessageDate = lastOutgoingMessageDate;
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
     * The interface to register a new device.
     *
     * @param owner               Device owner
     * @param name                Owner-assigned named (e.g. "My TV")
     * @param hardwareDescription Device should describe its model (e.g. "Samsung Galaxy S4")
     * @param gcmRegistrationId   Device should have already obtained the Google Cloud Messaging registration ID
     * @param comment             Any additional comment string
     * @return Device description
     */
    public static Device registerDevice(Key<CUser> owner, String name, String hardwareDescription,
                                        String gcmRegistrationId, String comment) {

        Logger log = Logger.getLogger(Device.class.getName() + " registerDevice");

        // Do some basic validation for duplicates
        List<Device> existingDevices = getAllUserDevices(owner);
        if (existingDevices != null && existingDevices.size() > 0) {
            for (Device otherDevice : existingDevices) {
                if (otherDevice.getName().equals(name)) {
                    String msg = "You already have a device with this name: " + name;
                    log.warning(owner + " " + msg);
                    throw new IllegalArgumentException(msg);
                }
                if (otherDevice.getGcmRegistrationId().equals(gcmRegistrationId)) {
                    String msg = "You already have a device with the same Google Cloud Messenger ID." +
                            " This may be an internal error";
                    log.severe(owner + " " + msg);
                    throw new IllegalArgumentException(msg);
                }
            }
        }

        // TODO: more validation here... what other things should we check for?

        Device device = new Device(name, hardwareDescription, owner, gcmRegistrationId);
        device.setComment(comment);

        log.info(String.format("Registering new device: %s  HW: <%s> GCM: <%s> %s",
                name, hardwareDescription, gcmRegistrationId, comment));

        device.save(true); // now
        return device;
    }

    public static List<Device> getAllUserDevices(Key<CUser> owner) {
        return ofy().load().type(Device.class).filter("owner =", owner).list();
    }

    /**
     * Load a device by its key string
     *
     * @param objectIdStr should be result of getKey().getString()
     * @return the Device
     */
    public static Device loadByKey(String objectIdStr) {
        Key<Device> key = Key.create(objectIdStr);
        return ofy().load().key(key).now();
    }

    /**
     * Permanently deletes a device registration. Note that references to the deleted
     * entity may be retained in memory. Does not do any validity checks!
     *
     * @param now if true, blocks until deletion is complete
     * @return true for success, false for failure
     */
    public boolean deleteDevice(boolean now) {
        // TODO: would like a more permanent record than this log message.
        Logger log = Logger.getLogger(Device.class.getName());
        log.severe("Deleting device registration: " + toString());

        Result result = ofy().delete().entities(this);
        if (now) {
            result.now();
        }
        return true;
    }

    public Key<Device> getKey() {
        return Key.create(Device.class, getId());
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", publicId='" + publicId + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", hardwareDescription='" + hardwareDescription + '\'' +
                ", owner=" + owner +
                '}';
    }

    /**
     * Return true if the given user key is owner of this device.
     *
     * @param userKey a user key to check
     * @return true if userKey is the owner of this device
     */
    public boolean cUserIsOwner(Key<CUser> userKey) {
        return getOwner().equals(userKey);
    }

    public boolean cUserIsNotOwner(Key<CUser> userKey) {
        return ! cUserIsOwner(userKey);
    }
}
