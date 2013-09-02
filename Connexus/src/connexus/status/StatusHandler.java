package connexus.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

public class StatusHandler {
	public static void addStatus(HttpSession session, StatusMessage msg) {
		@SuppressWarnings("unchecked")
		List<StatusMessage> messageList = (List<StatusMessage>)session.getAttribute("StatusList");
		if (messageList == null) {
			messageList = new ArrayList<StatusMessage>();
		}
		messageList.add(msg);
		session.setAttribute("StatusList", messageList);
	}
	
	public static List<StatusMessage> getStatus(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<StatusMessage> messageList = (List<StatusMessage>)session.getAttribute("StatusList");
		return messageList;		
	}
}
