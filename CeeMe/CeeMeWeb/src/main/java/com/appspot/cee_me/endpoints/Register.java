package com.appspot.cee_me.endpoints;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.model.CUser;

import com.appspot.cee_me.servlet.CeeMeServletBase;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

/**
 * Endpoint to register a device.
 */
@Api(name = "register", version = "v1",
        description = "Register devices on Cee.me (cee-me.appspot.com)",
        scopes = {Config.EMAIL_SCOPE},
        clientIds = {
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID,
                Config.ANDROID_CLIENT_ID,
                Config.WEB_CLIENT_ID
        },
        audiences = {Config.ANDROID_AUDIENCE})
/**
 *
 */
public class Register {
    private final static Logger log = Logger.getLogger(Register.class.getName());

    private static CUser getUser(User user) {
        CUser cuser = CeeMeServletBase.getOrCreateUserRecord(user);
        if (cuser == null) {
            throw new IllegalArgumentException("Can't find your user record; try registering on the website.");
        }
        return cuser;
    }

    /**
     * Register a new hardware device in the CeeMe system.
     * @param user Device owner
     * @param name User-assigned short name (e.g. "My TV")
     * @param hardwareDescription Client-assigned hardware description (e.g. "Samsung Galaxy S4")
     * @param gcmRegistrationId Registration ID obtained from GCM service
     * @param comment User-assigned additional comment
     * @return New device description
     */
    @ApiMethod(name = "registerDevice", httpMethod = "post")
    public Device registerDevice(User user,
                                 @Named("name") String name,
                                 @Named("hardwareDescription") String hardwareDescription,
                                 @Named("gcmRegistrationId") String gcmRegistrationId,
                                 @Named("comment") @Nullable String comment
    ) {

        log.info(String.format("Endpoint: registering new device: %s  HW: <%s> GCM: <%s> %s",
                name, hardwareDescription, gcmRegistrationId, comment));

        CUser owner = getUser(user);
        com.appspot.cee_me.model.Device modelDevice = com.appspot.cee_me.model.Device.registerDevice(
                owner.getKey(), name, hardwareDescription, gcmRegistrationId, comment);
        Device device = new Device(modelDevice);
        return device;

    }

    /**
     * Updates the information for an existing registration. Any null field is ignored.
     * @param user Device owner
     * @param keyStr Device to modify.
     * @param name New name
     * @param hardwareDescription New hardware description
     * @param gcmRegistrationId New GCM messaging registration ID
     * @param comment New device comment
     * @return
     */
    @ApiMethod(name = "updateDevice", httpMethod = "post")
    public Device updateDevice(User user,
                               @Named("key") String keyStr,
                               @Named("name") @Nullable String name,
                               @Named("hardwareDescription") @Nullable String hardwareDescription,
                               @Named("gcmRegistrationId") @Nullable String gcmRegistrationId,
                               @Named("comment") @Nullable String comment
    ) {

        CUser owner = getUser(user);
        com.appspot.cee_me.model.Device device = loadByKey(keyStr);
        if (!device.cUserIsOwner(owner.getKey())) {
            log.severe("Tried to update someone else's registration: " + owner + " " + device);
            throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
        }
        log.info(String.format("Endpoint: updating device registration: %s  HW: <%s> GCM: <%s> %s",
                name, hardwareDescription, gcmRegistrationId, comment));
        boolean didUpdate = false;
        if (name != null) {
            device.setName(name);
            didUpdate = true;
            log.fine("Updated device name");
        }
        if (hardwareDescription != null) {
            device.setHardwareDescription(hardwareDescription);
            didUpdate = true;
            log.fine("Updated hardware description.");
        }
        if (gcmRegistrationId != null && gcmRegistrationId.length() > 1) {
            device.setGcmRegistrationId(gcmRegistrationId);
            didUpdate = true;
            log.fine("Updated GCM registration ID.");
        }
        if (comment != null) {
            device.setComment(comment);
            didUpdate = true;
            log.fine("Updated comment.");
        }

        if (didUpdate) {
            device.save(false);  // blocking
        }

        // Return the API version of the modified Device
        return new Device(device);

    }

    /**
     * Retrieve a device description by its key.
     * @param user your Google User
     * @param keyStr the device key string obtain via a call to listMyDevices or registerDevice
     * @return
     */
    @ApiMethod(name = "getDevice", httpMethod = "get")
    public Device getDevice(User user, @Named("key") String keyStr) {
        CUser cuser = getUser(user);
        com.appspot.cee_me.model.Device device = loadByKey(keyStr);
        if (!device.cUserIsOwner(cuser.getKey())) {
            log.severe("Tried to view someone else's registration: " + cuser + " " + device);
            throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
        }
        log.info(String.format("Endpoint: getDevice: User:%s  Key: %s  Device: %s", user, keyStr, device));
        return new Device(device);
    }

    /**
     * Retreives a list of all registered devices that belong to you.
     * @param user your Google User
     * @return a list of your registered devices
     */
    @ApiMethod(name = "listMyDevices", httpMethod = "get")
    public List<Device> listMyDevices(User user) {
        CUser cuser = getUser(user);
        List<Device> returnList = new ArrayList<Device>();
        for (com.appspot.cee_me.model.Device device :
                com.appspot.cee_me.model.Device.getAllUserDevices(cuser.getKey())) {
            returnList.add(new Device(device));
        }
        return returnList;
    }

    /**
     * Delete (de-register) a device.
     * @param user your Google User
     * @param keyStr device key string
     */
    @ApiMethod(name = "deleteRegistration", httpMethod = "post")
    public void deleteRegistration(User user,
                                   @Named("key") String keyStr) {
        CUser cuser = getUser(user);
        com.appspot.cee_me.model.Device device = loadByKey(keyStr);
        if (!device.cUserIsOwner(cuser.getKey())) {
            log.severe("Tried to delete someone else's registration: " + cuser + " " + device);
            throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
        }
        log.warning("API device deletion: " + device);
        device.deleteDevice(false);
    }


    private static com.appspot.cee_me.model.Device loadByKey(String keyStr) {
        com.appspot.cee_me.model.Device device = com.appspot.cee_me.model.Device.loadByKey(keyStr);
        if (device == null) {
            String msg = "Can't find device with key: " + keyStr;
            log.severe("API device lookup fail: " + msg);
            throw new IllegalArgumentException(msg);
        }
        return device;
    }

}
