package com.appspot.cee_me.endpoints.model;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Represents a Device for endpoint JSON responses
 */
public class Device implements Serializable {
    private String key;  // ID field - look it up by this key in several messages.

    private String publicId; // goes into public URL

    private String name; // Displayed user-assignable name
    private String comment; // user-assignable extra comment field
    private String hardwareDescription; // something to identify the actual hardware (user-visible)

    private String ownerKey;

    private String gcmRegistrationId; // Google Cloud Messaging registration ID for this device.

    private DateTime creationDate;        // when was the device registered?
    private DateTime lastIncomingMessageDate; // when was this device named a target of a message?
    private DateTime lastOutgoingMessageDate; // when did this device last originate a message?

    public Device() {}

    public Device(com.appspot.cee_me.model.Device device) {
        if (device != null) {
            setKey(device.getKey().getString());
            setPublicId(device.getPublicId());
            setName(device.getName());
            setComment(device.getComment());
            setHardwareDescription(device.getHardwareDescription());
            setOwnerKey(device.getOwner().getKey().getString());
            setGcmRegistrationId(device.getGcmRegistrationId());
            setCreationDate(device.getCreationDate());
            setLastIncomingMessageDate(device.getLastIncomingMessageDate());
            setLastOutgoingMessageDate(device.getLastOutgoingMessageDate());
        }
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
