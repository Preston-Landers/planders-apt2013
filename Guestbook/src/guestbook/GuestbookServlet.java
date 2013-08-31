package guestbook;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class GuestbookServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7759593256585062849L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<HTML><BODY><P>Hello, " + user.getNickname() + "</P>");
			out.println("<P><A HREF=\""
					+ userService.createLogoutURL("/guestbook")
					+ "\">Logout of Google Account</A></P>");
			out.println("</BODY></HTML>");
		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
	}
}