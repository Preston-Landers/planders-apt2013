package com.appspot.cee_me.servlet;

import com.appspot.cee_me.CeeMeContext;
import com.appspot.cee_me.Config;
import com.appspot.cee_me.model.CUser;
import com.appspot.cee_me.model.Device;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class Manage extends CeeMeServletBase {
	private static final long serialVersionUID = 1609921843328426049L;
	
	private static final String uri = "/manage";
	private static final String dispatcher = "/WEB-INF/jsp/manage.jsp";
    private final static Logger log = Logger.getLogger(Manage.class.getName());

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		CeeMeContext ceeMeContext = InitializeContext(req); // Base site context initialization

        CUser cuser = ceeMeContext.getCuser();
        // Force user to login screen before showing manage
        if (cuser == null) {
            resp.sendRedirect(ceeMeContext.getLoginURL());
            return;
        }

        List<Device> deviceList = Device.getAllUserDevices(cuser.getKey());
        req.setAttribute("myDeviceList", deviceList);


		// Forward to JSP page to display device management screen.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        CeeMeContext cContext = InitializeContext(req); // Base site context initialization
        CUser cuser = cContext.getCuser();
        // Force user to login screen before showing manage
        if (cuser == null) {
            String msg = "You must be signed in to delete a device registration.";
            alertError(req, msg);
            log.warning(msg);
            resp.sendRedirect(uri);
            return;
        }

        if (req.getParameter("doDelete") != null) {
            if (req.getParameter("delete") == null) {
                alertError(req, "You must select a device to delete first.");
                log.fine("Tried to delete without selecting a device.");
                resp.sendRedirect(uri);
                return;
            }
            deleteDevice(cContext, req);
        } else {
            alertWarning(req, Config.MSG_UNKNOWN_COMMAND);
        }

        resp.sendRedirect(uri);
    }

    private static void deleteDevice(CeeMeContext cContext, HttpServletRequest req) {
        // Do we want to use a transaction here?
        CUser cuser = cContext.getCuser();
        for (String objectIdStr : req.getParameterValues("delete")) {
            if (objectIdStr == null || objectIdStr.length() < 1) {
                continue;
            }
            Device device = Device.getByKey(objectIdStr);
            if (device == null) {
                log.warning("Error deleting device: could not load device by key: " + objectIdStr);
                continue;
            }
            if (!device.getOwner().equals(cuser.getKey())) {
                String delMsg = "Error: This device does not belong to you.";
                log.severe("Tried to delete someone else's registration: " + cuser + " " + device);
                alertError(req, delMsg);
                // throw new IllegalArgumentException(Config.MSG_NOT_DEVICE_OWNER);
                continue;
            }
            String deviceName = device.getName();
            if (device.deleteDevice(true)) {
                String delMsg = "Device registration was deleted for " + deviceName;
                log.info(delMsg + " " + device.toString());
                alertSuccess(req, delMsg);
            } else {
                String delMsg = "Error: Device registration could not be deleted for " + deviceName;
                log.info(delMsg + " " + device.toString());
                alertError(req, delMsg);
            }

        }

    }

}