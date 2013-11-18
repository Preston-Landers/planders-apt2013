package com.appspot.cee_me.servlet;

import com.appspot.cee_me.CeeMeContext;
import com.appspot.cee_me.Config;
import com.appspot.cee_me.model.CUser;
import com.appspot.cee_me.model.Device;
import com.appspot.cee_me.model.Message;
import com.appspot.cee_me.model.MessageQuery;
import com.googlecode.objectify.Ref;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;


public class Send extends CeeMeServletBase {
    private static final long serialVersionUID = 5623205007285996379L;

    private static final String uri = "/send";
    private static final String dispatcher = "/WEB-INF/jsp/send.jsp";
    private final static Logger log = Logger.getLogger(Send.class.getName());

    static final String paramToDevice = "to";
    static final String paramSendButton = "send";
    static final String paramMessageText = "messageText";
    static final String paramMessageURL = "messageURL";
    static final String paramDeleteMessageButton = "deleteMessages";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        @SuppressWarnings("unused")
        CeeMeContext ceeMeContext = InitializeContext(req); // Base site context initialization

        fetchRecentMessages(req);

        // Forward to JSP page to display them in a HTML table.
        req.getRequestDispatcher(dispatcher).forward(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        CeeMeContext ceeMeContext = InitializeContext(req);

        if (req.getParameter(paramSendButton) != null) {
            doSendMessage(ceeMeContext, req, resp);
        }
        else if (req.getParameter(paramDeleteMessageButton) != null) {
            doDeleteMessages(ceeMeContext, req, resp);
        }
        else {
            alertInfo(req, "Unknown operation; consult owner's manual.");
            resp.sendRedirect(uri);
        }

    }

    private void fetchRecentMessages(HttpServletRequest req) {
        String toDeviceKey = req.getParameter(paramToDevice);
        if (toDeviceKey != null) {
            Device toDevice = Device.getByKey(toDeviceKey);
            if (toDevice == null) {
                alertWarning(req, Config.MSG_UNKNOWN_DEVICE);
                return;
            }
            // Only fetch messages from the 7 days. (TODO)
            DateTime since = new DateTime().minusDays(7);
            MessageQuery messageQuery = MessageQuery.fetchRecentMessages(toDevice.getKey(), since, 0, 0);
            req.setAttribute("messageQuery", messageQuery);
        }
    }

    private void doDeleteMessages(CeeMeContext context, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        for (String objectIdStr : req.getParameterValues("delete")) {
            if (objectIdStr == null || objectIdStr.length() < 1) {
                continue;
            }
            Message message = Message.getByKey(objectIdStr);
            if (message == null) {
                String errMsg = "Message does not exist.";
                log.severe(errMsg + " " + objectIdStr);
                alertError(req, errMsg);
                return;
            }

            if (!message.canUserDelete(Ref.create(context.getCuser()))) {
                String errMsg = Config.MSG_NOT_ALLOWED;
                log.severe(errMsg + " on delete: " + objectIdStr);
                alertError(req, errMsg);
                return;
            }

            message.deleteMessage(true);
            alertSuccess(req, "Message was deleted: " + message.getText());

        }

        String destinationDeviceKey = req.getParameter(paramToDevice);
        if (destinationDeviceKey == null) {
            resp.sendRedirect(uri);
            return;
        }
        Device toDevice = Device.getByKey(destinationDeviceKey);
        if (toDevice == null) {
            resp.sendRedirect(uri);
            return;
        }
        // alertSuccess(req, "Messages were deleted!");

        resp.sendRedirect(toDevice.getSendUri());

    }

    private void doSendMessage(CeeMeContext context, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        CUser fromUser = context.getCuser();
        String messageText = req.getParameter(paramMessageText);
        if (messageText == null || messageText.trim().length() == 0) {
            alertError(req, "Must give a message.");
            return;
        }
        messageText = messageText.trim();

        String urlData = req.getParameter(paramMessageURL);
        if (urlData == null || urlData.trim().length() == 0) {
            urlData = ""; // not really sure why I'm doing this
        }

        String destinationDeviceKey = req.getParameter(paramToDevice);
        if (destinationDeviceKey == null) {
            alertError(req, Config.MSG_UNKNOWN_DEVICE);
            return;
        }
        Device toDevice = Device.getByKey(destinationDeviceKey);
        if (toDevice == null) {
            alertError(req, Config.MSG_UNKNOWN_DEVICE);
            return;
        }
        CUser toUser = toDevice.getOwner();

        log.info(fromUser + " is sending message to: " + toDevice + " message text: " + messageText);
        Message message = Message.createMessage(
                null,  // fromDevice
                Ref.create(fromUser),
                Ref.create(toUser),
                Ref.create(toDevice),
                null,  // media
                urlData,
                messageText);
        message.send();

        alertSuccess(req, "Message was sent!");

        resp.sendRedirect(toDevice.getSendUri());
    }

}