package com.appspot.cee_me.endpoints;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.endpoints.model.Message;
import com.appspot.cee_me.endpoints.model.MessageQuery;
import com.appspot.cee_me.model.CUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Endpoint for a device to sync its messages.
 */
@Api(name = "sync", version = "v1",
        description = "Synchronize messages from Cee.me (cee-me.appspot.com)",
        scopes = {Config.EMAIL_SCOPE},
        clientIds = {
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID,
                Config.ANDROID_CLIENT_ID,
                Config.WEB_CLIENT_ID
        },
        audiences = {Config.ANDROID_AUDIENCE})
public class Sync extends EndpointBase {
    private final static Logger log = Logger.getLogger(Sync.class.getName());

    /**
     * Retrieve a single Message.
     *
     * @param user   Google user
     * @param keyStr key of message to retrieve
     * @return the Message
     */
    @ApiMethod(name = "getMessage", httpMethod = "get")
    public Message getMessage(User user,
                              @Named("messageKey") String keyStr) {
        CUser cuser = getUser(user);
        com.appspot.cee_me.model.Message message = loadMessageByKey(keyStr);
        if (!message.canUserRead(Ref.create(cuser))) {
            log.severe("Tried to retrieve someone else's message: " + cuser + " " + message);
            throw new IllegalArgumentException(Config.MSG_NOT_ALLOWED);
        }
        log.fine("API message retrieval: " + message);
        return new Message(message);
    }

    /**
     * Permanently delete a message.
     *
     * @param user   your Google User
     * @param keyStr message key string
     */
    @ApiMethod(name = "deleteMessage", httpMethod = "post")
    public Message deleteMessage(User user,
                                 @Named("messageKey") String keyStr) {
        CUser cuser = getUser(user);
        com.appspot.cee_me.model.Message message = loadMessageByKey(keyStr);
        if (!message.canUserDelete(Ref.create(cuser))) {
            log.severe("Tried to delete someone else's message: " + cuser + " " + message);
            throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
        }
        log.warning("API device deletion: " + message);
        message.deleteMessage(true);
        return new Message(message);
    }

    /**
     * Sets a message as accepted.
     *
     * @param user   your Google User
     * @param keyStr message key string
     */
    @ApiMethod(name = "setMessageAccepted", httpMethod = "post")
    public Message setMessageAccepted(User user,
                                      @Named("messageKey") String keyStr) {
        CUser cuser = getUser(user);
        com.appspot.cee_me.model.Message message = loadMessageByKey(keyStr);
        if (!message.canUserRead(Ref.create(cuser))) {
            log.severe("Tried to set accepted on someone else's message: " + cuser + " " + message);
            throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
        }
        log.warning("API updating message accepted flag: " + message);
        message.setAccepted(true);
        message.save(false);
        return new Message(message);
    }

    /**
     * Retrieve messages for a device.
     *
     * @param user   Google user
     * @param keyStr key of device to get messages for
     * @param since  start date of message search (defaults to 1 day ago)
     * @param limit  limit number of results
     * @param offset offset into result set
     * @return
     */
    @ApiMethod(name = "getMessages", httpMethod = "get")
    public MessageQuery getMessages(
            User user,
            @Named("deviceKey") String keyStr,
            @Named("since") @Nullable Date since,
            @Named("limit") @Nullable Integer limit,
            @Named("offset") @Nullable Integer offset) {
        com.appspot.cee_me.model.Device device = loadDeviceByKey(keyStr);

        DateTime sinceJoda = new DateTime(since);
        if (sinceJoda == null) {
            sinceJoda = new DateTime().minusDays(1);
        }
        if (limit == null) {
            limit = new Integer(0);
        }
        if (offset == null) {
            offset = new Integer(0);
        }

        return MessageQuery.fetchRecentMessages(
                device,
                sinceJoda,
                limit,
                offset);
    }
}
