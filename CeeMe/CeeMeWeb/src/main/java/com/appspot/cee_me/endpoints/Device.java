package com.appspot.cee_me.endpoints;

import org.joda.time.DateTime;

import java.io.Serializable;

// import java.util.Date;

/**
 * Represents a Device for endpoint JSON responses
 */
public class Device implements Serializable {
    String key;  // ID field - look it up by this key in several messages.

    String publicId; // goes into public URL

    String name; // Displayed user-assignable name
    String comment; // user-assignable extra comment field
    String hardwareDescription; // something to identify the actual hardware (user-visible)

    String ownerKey;

    String gcmRegistrationId; // Google Cloud Messaging registration ID for this device.

    DateTime creationDate;        // when was the device registered?
    DateTime lastIncomingMessageDate; // when was this device named a target of a message?
    DateTime lastOutgoingMessageDate; // when did this device last originate a message?
//    Date creationDate;        // when was the device registered?
//    Date lastIncomingMessageDate; // when was this device named a target of a message?
//    Date lastOutgoingMessageDate; // when did this device last originate a message?

    public Device() {}

    public Device(com.appspot.cee_me.model.Device device) {
        key = device.getKey().getString();
        publicId = device.getPublicId();
        name = device.getName();
        comment = device.getComment();
        hardwareDescription = device.getHardwareDescription();
        ownerKey = device.getOwner().getString();
        gcmRegistrationId = device.getGcmRegistrationId();
        creationDate = device.getCreationDate();
        lastIncomingMessageDate = device.getLastIncomingMessageDate();
        lastOutgoingMessageDate = device.getLastOutgoingMessageDate();
//        creationDate = device.getCreationDate().toDate();
//        lastIncomingMessageDate = device.getLastIncomingMessageDate().toDate();
//        lastOutgoingMessageDate = device.getLastOutgoingMessageDate().toDate();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(String ownerKey) {
        this.ownerKey = ownerKey;
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
}
