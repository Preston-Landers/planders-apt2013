package connexus.servlet;

import connexus.ConnexusContext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Social extends ConnexusServletBase {

	private static final long serialVersionUID = -2064455843440546658L;
	public static final String uri = "/social";
	public static final String dispatcher = "/WEB-INF/jsp/social.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
}