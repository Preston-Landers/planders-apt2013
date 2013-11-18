package com.appspot.cee_me;

import java.io.IOException;
import java.util.List;

import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.android.gcm.server.Message;
/**
 * Implements the application server side of our Google Cloud Messaging (GCM) notifications to Android devices.
 */
public class GCMServer {

    /**
     * Send a message to a list of devices. There are length limitations...?
     * @param devices List of device GCM registration ID strings
     * @param messageText Message text body
     * @param messageKey key for full Message model object
     * @param url additional URL content for message
     * @return the result object
     */
    public static MulticastResult sendMessageToDevice(List<String> devices,
                                                String messageText, String messageKey, String url) {
        Sender sender = new Sender(Config.GOOGLE_PROJECT_ID);
        Message gcmMessage = new Message.Builder()
                .addData("messageText", messageText)
                .addData("messageKey", messageKey)
                .addData("url", url).build();
        MulticastResult result = null;
        try {
            result = sender.send(gcmMessage, devices, 5);
        } catch (IOException e) {
            System.err.println("Error sending GCM message to " + devices.toString() + " text: " + messageText + " Url: " + url);
            e.printStackTrace();
        }
        return result;
    }
}
