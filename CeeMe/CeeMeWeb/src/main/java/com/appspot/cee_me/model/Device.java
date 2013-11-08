package com.appspot.cee_me.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

import java.util.ArrayList;
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
    @Id
    Long id;

    @Index String publicId; // goes into public URL

    String name; // Displayed user-assignable name
    String comment; // user-assignable extra comment field
    String hardwareDescription; // something to identify the actual hardware (user-visible)

    @Index Key<CUser> owner;

    String gcmRegistrationId; // Google Cloud Messaging registration ID for this device.

    DateTime creationDate;        // when was the device registered?
    DateTime lastIncomingMessageDate; // when was this device named a target of a message?
    DateTime lastOutgoingMessageDate; // when did this device last originate a message?

    private Device() {}

    public Device(String name, String hardwareDescription, Key<CUser> owner, String gcmRegistrationId) {
        this.name = name;
        this.hardwareDescription = hardwareDescription;
        this.owner = owner;
        this.gcmRegistrationId = gcmRegistrationId;
        this.creationDate = new DateTime();
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
     * @param async set to true if the save should be finished before returning.
     */
    public void save(boolean async) {
        Result result = ofy().save().entity(this);
        if (!async) {
           result.now();
        }
    }

    /**
     * The interface to register a new device.
     * @param owner Device owner
     * @param name Owner-assigned named (e.g. "My TV")
     * @param hardwareDescription Device should describe its model (e.g. "Samsung Galaxy S4")
     * @param gcmRegistrationId Device should have already obtained the Google Cloud Messaging registration ID
     * @param comment Any additional comment string
     * @return
     */
    public static Device registerDevice(Key<CUser> owner, String name, String hardwareDescription,
                                        String gcmRegistrationId, String comment) {

        Logger log = Logger.getLogger(Device.class.getName());

        Device device = new Device(name, hardwareDescription, owner, gcmRegistrationId);
        device.setComment(comment);

        log.info(String.format("Registering new device: %s  HW: <%s> GCM: <%s> %s",
                name, hardwareDescription, gcmRegistrationId, comment));

        device.save(true);
        return device;
    }

    public static List<Device> getAllUserDevices(Key<CUser> owner) {
        return ofy().load().type(Device.class).filter("owner =", owner).list();
    }
}
