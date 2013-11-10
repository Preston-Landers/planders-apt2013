package com.appspot.cee_me.model;

import java.io.Serializable;
import java.util.List;

import static com.appspot.cee_me.OfyService.ofy;

/**
 * Choose a device to send a message.
 */
public class DeviceSelector implements Serializable {
    public DeviceSelector() {}

    public List<Device> getDeviceSelectorList() {
        // TODO: sort by name or something
        return ofy().load().type(Device.class).list();

    }
}
