package com.appspot.cee_me.endpoints;

import com.appspot.cee_me.model.CUser;
import com.appspot.cee_me.model.Device;
import com.appspot.cee_me.model.Message;
import com.appspot.cee_me.servlet.CeeMeServletBase;
import com.google.appengine.api.users.User;

import java.util.logging.Logger;

/**
 * Base class for endpoint APIs.
 */
public class EndpointBase {

    private final static Logger log = Logger.getLogger(EndpointBase.class.getName());

    protected static CUser getUser(User user) {
        CUser cuser = CeeMeServletBase.getOrCreateUserRecord(user);
        if (cuser == null) {
            throw new IllegalArgumentException("Can't find your user record; try registering on the website.");
        }
        return cuser;
    }

    protected static Device loadDeviceByKey(String keyStr) {
        Device device = Device.getByKey(keyStr);
        if (device == null) {
            String msg = "Can't find device with key: " + keyStr;
            log.severe("API device lookup fail: " + msg);
            throw new IllegalArgumentException(msg);
        }
        return device;
    }

    protected static Message loadMessageByKey(String keyStr) {
        Message message = Message.getByKey(keyStr);
        if (message == null) {
            String msg = "Can't find message with key: " + keyStr;
            log.severe("API message lookup fail: " + msg);
            throw new IllegalArgumentException(msg);
        }
        return message;
    }

}
