package com.appspot.cee_me.model;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.LatLong;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static com.appspot.cee_me.OfyService.ofy;

/**
 * Represents a unique install on an Android device.
 * These are the targets of Messages.
 */
@Entity
@Cache
public class Device implements Comparable<Device> {
    private
    @Id
    Long id;

    private
    @Index
    String publicId; // goes into public URL

    private String name; // Displayed user-assignable name
    private String comment; // user-assignable extra comment field
    private String hardwareDescription; // something to identify the actual hardware (user-visible)

    private
    @Index
    @Load
    Ref<CUser> owner;

    private String gcmRegistrationId; // Google Cloud Messaging registration ID for this device.

    private DateTime creationDate;        // when was the device registered?
    private DateTime lastIncomingMessageDate; // when was this device named a target of a message?
    private DateTime lastOutgoingMessageDate; // when did this device last originate a message?

    @SuppressWarnings("unused")
    private Device() {
    }

    private static final char[] publicIdChars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

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

    public void setOwner(Ref<CUser> owner) {
        this.owner = owner;
    }

    public CUser getOwner() {
        return owner == null ? null : owner.get();
    }

    public void setOwner(CUser owner) {
        setOwner(owner.getKey());
    }

    public void setOwner(Key<CUser> ownerKey) {
        setOwner(Ref.create(ownerKey));
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
     * Compare first by owner account name and then by publicId.
     * @param other other device to compare to
     * @return -1 if this is less than other, 0 is equal, otherwise 1
     */
    @Override
    public int compareTo(Device other) {
        String myAccountName = getOwner().getAccountName();
        String otherAccountName = other.getOwner().getAccountName();

        if (myAccountName.equals(otherAccountName)) {
            return getPublicId().compareTo(other.getPublicId());
        } else {
            return myAccountName.compareTo(otherAccountName);
        }
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
     * Generates a new, random device ID that is not already in use.
     * @return the generated device ID
     */
    public static String generatePublicId() {
        Logger log = Logger.getLogger(Device.class.getName());
        Random rand = new Random();
        char[] result = new char[Config.sizePublicDeviceId];
        for (int i=0; i<result.length; i++) {
            result[i] = publicIdChars[rand.nextInt(publicIdChars.length)];
        }
        String publicId = new String(result);
        if (isPublicIdInUse(publicId)) {
            log.warning("Generated a public device ID that is already in use: " + publicId);
            return generatePublicId();
        }
        log.info("Generated a public device ID: " + publicId);
        return publicId;
    }

    public static boolean isPublicIdInUse(String publicId) {
        int count = ofy().load().type(Device.class).filter("publicId =", publicId).count();
        if (count > 0) {
            return true;
        }
        return false;
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
        device.setPublicId(generatePublicId());

        log.info(String.format("Registering new device: %s - %s  HW: <%s> GCM: <%s> %s",
                device.getPublicId(), name, hardwareDescription, gcmRegistrationId, comment));

        device.save(true); // now
        return device;
    }

    public static List<Device> getAllUserDevices(Key<CUser> owner) {
        return ofy().load().type(Device.class).filter("owner =", owner).list();
    }

    public static Device getByPublicId(String publicId) {
        return ofy().load().type(Device.class).filter("publicId =", publicId.toLowerCase()).first().now();
    }

    /**
     * Load a device by its key string
     *
     * @param objectIdStr should be result of getKey().getString()
     * @return the Device
     */
    public static Device getByKey(String objectIdStr) {
        Key<Device> key = Key.create(objectIdStr);
        return getByKey(key);
    }

    /**
     * Load a Device by its datastore Key
     *
     * @param deviceKey device key to load
     * @return the Device
     */
    public static Device getByKey(Key<Device> deviceKey) {
        return ofy().load().key(deviceKey).now();
    }

    /**
     * Permanently deletes a device registration. Note that references to the deleted
     * entity may be retained in memory. Does not do any validity checks!
     *
     * @param now if true, blocks until deletion is complete
     * @return true for success, false for failure
     */
    public boolean deleteDevice(boolean now) {
        Logger log = Logger.getLogger(Device.class.getName());
        if (!canUserDelete(null)) {
            log.severe("User is not allowed to delete this device: " + toString());
        }

        // TODO: would like a more permanent record than this log message.
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

    public boolean canUserDelete(Ref<CUser> cUserRef) {
        if (CUser.isUserAdmin()) {
            return true;
        }
        if (cUserRef != null && getOwner().getKey().equals(cUserRef.getKey())) {
            return true;
        }
        return false;
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
        return getOwner().getKey().equals(userKey);
    }

    public boolean cUserIsNotOwner(Key<CUser> userKey) {
        return !cUserIsOwner(userKey);
    }

    public String getSendUri() {
        return "/send?to=" + getKey().getString();
    }

    /**
     * Determine if this device matches a search term. Assumes the search string has been trimmed and lowercased
     *
     * @param term a search term
     * @return true if the term is empty, or if the term matches any of our search-enabled strings
     */
    public boolean matchesSearchTerm(String term) {
        if (term == null || term.equals("")) {
            return true;
        }
        CUser owner = getOwner();
        if (owner.getAccountName().contains(term) ||
                owner.getRealName().contains(term) ||
                getName().contains(term) ||
                getPublicId().contains(term) ||
                getComment().contains(term)) {
            return true;
        }

        return false;
    }

    /**
     * Perform a device directory search. This is a good candidate for a unit test.
     *
     * @param limit       limit results, 0 == unlimited
     * @param offset      offset within results
     * @param queryString optional search string
     * @param latLng      optional search coordinates (find results near this) TODO: not yet implemented
     * @return
     */
    public static List<Device> getDirectorySearch(int limit, int offset, String queryString, LatLong latLng) {
        List<Device> returnList = new ArrayList<Device>();
        List<Device> allDevices = ofy().load().type(Device.class).list();
        int included = 0;
        int usedOffset = 0;
        int i = -1;
        if (queryString != null) {
            queryString = queryString.trim().toLowerCase();
        }

        // TODO: if latLng is set, order the devices by distance to that point.

        for (Device device : allDevices) {
            i++;
            if ((limit > 0) && (included > limit)) {
                break;
            }
            if (device.matchesSearchTerm(queryString)) {
                if ((offset > 0) && (usedOffset < offset)) {
                    usedOffset++;
                    continue;
                }
                included++;
                returnList.add(device);
            }
        }
        Collections.sort(returnList);
        return returnList;
    }
}
