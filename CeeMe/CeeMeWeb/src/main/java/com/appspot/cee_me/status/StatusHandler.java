package com.appspot.cee_me.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * Functions to get, set, and clear status messages. The formatting of the
 * message containers is handled in WEB-INF/tags/status.tag (and in CSS)
 * 
 */
public class StatusHandler {
	public final static String propName = "StatusList"; // name of property within session
	public static void addStatus(HttpSession session, StatusMessage msg) {
		@SuppressWarnings("unchecked")
		List<StatusMessage> messageList = (List<StatusMessage>) session
				.getAttribute(propName);
		if (messageList == null) {
			messageList = new ArrayList<StatusMessage>();
		}
		messageList.add(msg);
		session.setAttribute(propName, messageList);
	}

	public static List<StatusMessage> getAppStatus(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<StatusMessage> messageList = (List<StatusMessage>) session
				.getAttribute(propName);
		return messageList;
	}

	public static void clearStatus(HttpSession session) {
		session.removeAttribute(propName);
	}

	public static List<StatusMessage> getAndClearAppStatus(HttpSession session) {
		List<StatusMessage> messageList = getAppStatus(session);
		clearStatus(session);
		return messageList;
	}
}
