package com.appspot.cee_me.endpoints.model;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Represents a Message in the endpoint API.
 */
public class Message implements Serializable {
    private String messageKey;
    private Device fromDevice;
    private Device toDevice;
    private User fromUser;
    private User toUser;
    private Media media;
    private String text;
    private DateTime creationDate;
    private DateTime lastRetrievalDate;
    private boolean accepted;
    private String openWithApp;
    private String openWithAppName;

    public Message() {
    }

    public Message(String messageKey) {
        this.messageKey = messageKey;
    }

    public Message(com.appspot.cee_me.model.Message message) {
        if (message != null) {
            setMessageKey(message.getKey().getString());
            com.appspot.cee_me.model.Device fromDevice = message.getFromDevice();
            setFromDevice(fromDevice == null ? null : new Device(fromDevice));
            setToDevice(new Device(message.getToDevice()));
            setToUser(new User(message.getToUser()));
            setFromUser(new User(message.getFromUser()));

            com.appspot.cee_me.model.Media media = message.getMedia();
            setMedia(media == null ? null : new Media(media));

            setText(message.getText());

            setCreationDate(message.getCreationDate());

            setLastRetrievalDate(message.getLastRetrievalDate());

            setAccepted(message.getAccepted());

            setOpenWithApp(message.getOpenWithApp());
            setOpenWithAppName(message.getOpenWithAppName());
        }
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Device getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(Device fromDevice) {
        this.fromDevice = fromDevice;
    }

    public Device getToDevice() {
        return toDevice;
    }

    public void setToDevice(Device toDevice) {
        this.toDevice = toDevice;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getLastRetrievalDate() {
        return lastRetrievalDate;
    }

    public void setLastRetrievalDate(DateTime lastRetrievalDate) {
        this.lastRetrievalDate = lastRetrievalDate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getOpenWithApp() {
        return openWithApp;
    }

    public void setOpenWithApp(String openWithApp) {
        this.openWithApp = openWithApp;
    }

    public String getOpenWithAppName() {
        return openWithAppName;
    }

    public void setOpenWithAppName(String openWithAppName) {
        this.openWithAppName = openWithAppName;
    }
}
