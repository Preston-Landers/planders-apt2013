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

    @ApiMethod(name="listMyDevices", httpMethod = "get")
    public List<Device> listMyDevices(User user) {
        CUser owner = getUser(user);
        List<Device> returnList = new ArrayList<Device>();
        for (com.appspot.cee_me.model.Device device:
                com.appspot.cee_me.model.Device.getAllUserDevices(owner.getKey())) {
            returnList.add(new Device(device));
        }
        return returnList;
    }
}
