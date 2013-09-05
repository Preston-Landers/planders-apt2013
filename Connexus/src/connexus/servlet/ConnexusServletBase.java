package connexus.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connexus.status.*;

public class ConnexusServletBase extends HttpServlet {

	private static final long serialVersionUID = 7414103509881465189L;

	public void alertError(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.ERROR, msg);
	}
	public void alertSuccess(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.SUCCESS, msg);
	}
	public void alertInfo(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.INFO, msg);
	}
	public void alertWarning(HttpServletRequest req, String msg) {
		alertMessage(req, StatusMessageType.WARNING, msg);
	}

	public void alertMessage(HttpServletRequest req, StatusMessageType msgType,
			String msg) {
		HttpSession session = req.getSession(true);
		StatusHandler.addStatus(session, new StatusMessage(msgType, msg));
	}

}
