package com.appspot.cee_me.endpoints;

import com.appspot.cee_me.Config;
import com.appspot.cee_me.endpoints.model.Media;
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
import java.util.Date;
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

    /**
     * Send a new message to another device. One of these 3 options must be set:
     * text, urlData, mediaKey (or any combination of those)
     *
     * @param user          Google User account
     * @param fromDeviceKey senders device key
     * @param toDeviceKey   recipient's device key
     * @param mediaKey      if including a media attachment, its key (optional)
     * @param urlData       URL to include with message (optional)
     * @param text          message text (optional)
     * @return
     */
    @ApiMethod(name = "sendMessage", httpMethod = "post", path="sendMessage/{fromDevice}/{toDevice}")
    public Message sendMessage(
            User user,
            @Named("fromDevice") String fromDeviceKey,
            @Named("toDevice") String toDeviceKey,
            @Named("mediaKey") @Nullable String mediaKey,
            @Named("urlData") @Nullable String urlData,
            @Named("text") @Nullable String text
    ) {
        CUser fromUser = getUser(user);
        com.appspot.cee_me.model.Device fromDevice = loadDeviceByKey(fromDeviceKey);
        com.appspot.cee_me.model.Device toDevice = loadDeviceByKey(toDeviceKey);
        if (!fromDevice.getOwner().equals(fromUser)) {
            String errMsg = fromUser + " tried to send message from device that wasn't his: " + fromDevice + " message: " + text + " url: " + urlData;
            log.severe(errMsg);
            throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
        }
        CUser toUser = toDevice.getOwner();
        com.appspot.cee_me.model.Media modelMedia = null;
        if (mediaKey != null && !mediaKey.equals("")) {
            modelMedia = loadMediaByKey(mediaKey);
        }

        // Prevent null messages
        if ((mediaKey == null || (mediaKey.equals(""))) &&
                (urlData == null || (urlData.equals(""))) &&
                (text == null || (text.equals("")))
                ) {
            log.warning("Endpoint: this user tried to send a null message: " + fromUser);
            throw new IllegalArgumentException("You must set at least one of these parameters: mediaKey, urlData, text");
        }

        // Creates and saves the message.
        com.appspot.cee_me.model.Message message = com.appspot.cee_me.model.Message.createMessage(
                Ref.create(fromDevice),
                Ref.create(fromUser),
                Ref.create(toUser),
                Ref.create(toDevice),
                modelMedia == null ? null : Ref.create(modelMedia),
                text,
                urlData
        );
        log.fine("Endpoint: about to send new message: " + message);
        message.sendNotification();

        return new Message(message);
    }

    /**
     * Create a media entry corresponding to a file in Google Cloud Storage.
     * The GCS bucket is not a parameter, it's set internally by the server.
     * This assumes you have already transfered the file to GCS under the given gcsFilename.
     *
     * @param user        Google user
     * @param gcsFilename the GCS filename
     * @param fileName    the original name of the file to be preserved
     * @param mimeType    the file's mime type
     * @param size        size in bytes
     * @param comments    optional - comment string
     * @param sha256      optional - the SHA-256 digest
     * @param latitude    optional - media latitude point
     * @param longitude   optional - media longitude point
     * @return
     */
    // @ApiMethod(name = "createMedia", httpMethod = "post", path="createMedia/{gcsFilename}")
    @ApiMethod(name = "createMedia", httpMethod = "post", path="createMedia")
    public Media createMedia(
            User user,
            @Named("gcsFilename") String gcsFilename,
            @Named("fileName") String fileName,
            @Named("mimeType") String mimeType,
            @Named("size") Long size,
            @Named("comments") @Nullable String comments,
            @Named("sha256") @Nullable String sha256,
            @Named("latitude") @Nullable Double latitude,
            @Named("longitude") @Nullable Double longitude
    ) {
        CUser fromUser = getUser(user);
        String gcsBucket = Config.GCS_UPLOAD_BUCKET;
        com.appspot.cee_me.model.Media media = com.appspot.cee_me.model.Media.createMediaFromGCS(
                fromUser.getKey(),
                gcsBucket,
                gcsFilename,
                fileName,
                mimeType,
                size,
                comments,
                sha256,
                latitude,
                longitude
        );
        log.fine("Endpoint: created media attachment: " + media);

        return new Media(media);
    }

    /**
     * Test method, please ignore.
     * @param user user
     * @param text a string to test
     * @return a dummy message that is not sent
     */
    @ApiMethod(name = "echoMessage", httpMethod = "get")
    public EchoResult echoMessage(User user,
                               @Named("text") String text) {
        EchoResult result = new EchoResult();
        result.setText(text);
        return result;

    }

    public static class EchoResult {
        String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
