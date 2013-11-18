package com.appspot.cee_me.servlet;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.model.Device;
//import com.appspot.cee_me.model.Message;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.android.gcm.server.Message;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

/**
 * Servlet to handle sending notifications to android devices with GCM.
 */
public class SendNotification extends HttpServlet {

    /**
     * Name of the outgoing GCM notification task queue defined in queue.xml
     */
    private static final String GCM_QUEUE_NAME = "gcm-notifications";
    private static final String GCM_QUEUE_HANDLER = "/task/gcm";
    private static final String MESSAGE_KEY = "messageKey";
    private final static Logger log = Logger.getLogger(SendNotification.class.getName());

    /**
     * GCM sets maximum size of keys + values in message payload
     */
    private static final int GCM_MAX_PAYLOAD = 4096;

    /**
     * Max num of times to retry the GCM notification send
     */
    private static final int NUM_SEND_RETRIES = 5;

    private Sender sender;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sender = newSender();
    }

    /**
     * Creates the {@link Sender} based on the servlet settings.
     */
    private static Sender newSender() {
        return new Sender(Config.GCM_SENDER_KEY);
    }

    /**
     * Send a notification to the registered Android device for a particular Message.
     * Adds the notification to the task queue and returns immediately.
     * The actual notification will be sent later by the task worker.
     *
     * @param message the complete model.Message object
     */
    public static void sendMessageNotification(com.appspot.cee_me.model.Message message) {
        log.info("Sending message notification for: " + message.toString());
        Queue gcmQueue = QueueFactory.getQueue(GCM_QUEUE_NAME);
        gcmQueue.add(
                withUrl(GCM_QUEUE_HANDLER)
                        .param(MESSAGE_KEY, message.getKey().getString())
                        .method(Method.GET)
        );
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String messageKey = req.getParameter(MESSAGE_KEY);
        if (messageKey == null) {
            handleError(GCM_QUEUE_NAME + " worker got request with no " + MESSAGE_KEY, resp);
            return;
        }
        com.appspot.cee_me.model.Message message = com.appspot.cee_me.model.Message.getByKey(messageKey);
        if (message == null) {
            handleError(GCM_QUEUE_NAME + " worker can't load message key: " + messageKey, resp);
            return;
        }
        Device toDevice = message.getToDevice();
        if (toDevice == null) {
            handleError(GCM_QUEUE_NAME + " worker can't send notify (null device) on message: " + messageKey, resp);
            return;
        }

        doGCMServerSend(message, toDevice);

        resp.setStatus(200);
    }

    private void doGCMServerSend(com.appspot.cee_me.model.Message modelMessage, Device toDevice) throws IOException {
        String registrationId = toDevice.getGcmRegistrationId();
        Message.Builder builder = buildMessage(modelMessage);
        Message message = builder.build();
        try {
            Result result = sender.send(message, registrationId, NUM_SEND_RETRIES);
            log.info("Sent notification to one device: " + result);
        } catch (InvalidRequestException e) {
            log.log(Level.SEVERE, "Unable to send notification for message " + modelMessage, e);
        }
    }

    private static Message.Builder buildMessage(com.appspot.cee_me.model.Message modelMessage) {
        Message.Builder builder = new Message.Builder();

        int spaceLeft = GCM_MAX_PAYLOAD;
        final String MSG_KEY = "mk";
        final String URL_KEY = "url";
        final String TEXT_KEY = "t";

        String msgKey = modelMessage.getKey().getString();
        builder.addData(MSG_KEY, msgKey);
        spaceLeft -= MSG_KEY.length();
        spaceLeft -= msgKey.length();

        String msgUrl = modelMessage.getUrlData();
        if ( msgUrl != null && !msgUrl.equals("") ) {
            int spaceForUrl = URL_KEY.length() + msgUrl.length();
            if (spaceLeft >= spaceForUrl) {
                builder.addData(URL_KEY, msgUrl);
                spaceLeft -= spaceForUrl;
            } else {
                log.warning("Out of space to include URL in notification");
            }
        }

        String msgText = modelMessage.getText();
        if ( msgText != null && !msgText.equals("") ) {
            int spaceForText = TEXT_KEY.length() + msgText.length();
            if (spaceLeft >= spaceForText) {
                builder.addData(TEXT_KEY, msgText);
                spaceLeft -= spaceForText;
            } else {
                log.warning("Out of space to include text in notification");
            }
        }

        return builder;
    }

    private static void handleError(String errMsg, HttpServletResponse resp) {
        log.severe(errMsg);
        // Returning 200 OK here tells the task queue that we're done with this, don't retry it
        resp.setStatus(200);
    }

}
