package com.appspot.cee_me.model;

import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * A bean for a list of messages that match certain criteria
 */
public class MessageQuery implements Serializable {
    private List<Message> messageList;
    private DateTime sinceDateTime;
    private int limit;
    private int offset;
    private Device toDevice;

    public MessageQuery() {
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public DateTime getSinceDateTime() {
        return sinceDateTime;
    }

    public void setSinceDateTime(DateTime sinceDateTime) {
        this.sinceDateTime = sinceDateTime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Device getToDevice() {
        return toDevice;
    }

    public void setToDevice(Device toDevice) {
        this.toDevice = toDevice;
    }

    public static MessageQuery fetchRecentMessages(Key<Device> toDevice, DateTime since, int limit, int offset) {
        List<Message> messageList = Message.getMessagesForDevice(toDevice, since, limit, offset);
        MessageQuery messageQuery = new MessageQuery();
        messageQuery.setLimit(limit);
        messageQuery.setOffset(offset);
        messageQuery.setToDevice(Device.getByKey(toDevice));
        messageQuery.setSinceDateTime(since);
        messageQuery.setMessageList(messageList);
        return messageQuery;
    }

}
