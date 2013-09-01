package guestbook;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.googlecode.objectify.ObjectifyService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class OfySignGuestbookServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 294070389004039815L;

	static {
		ObjectifyService.register(Greeting.class);
	}
	static ExecutorService exec = Executors.newCachedThreadPool();

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String content = req.getParameter("content");

		Greeting greeting = new Greeting(user, content);
		ofy().save().entity(greeting).now();
		resp.sendRedirect("/ofyguestbook.jsp");
	}
}