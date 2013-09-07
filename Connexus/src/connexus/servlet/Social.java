package connexus.servlet;

//import static connexus.OfyService.ofy;
//import java.util.List;
//import connexus.CUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Social extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2064455843440546658L;
	public static final String uri = "/social";
	public static final String dispatcher = "/WEB-INF/jsp/social.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization
		
//		List<CUser> allUsersList = ofy().load().type(CUser.class).list();
//		for (CUser userRec : allUsersList) {
//			System.err.println("USER REC: " + userRec.toString());
//		}
//		req.setAttribute("userList", allUsersList);

		// throw new ServletException("Retrieving products failed!", e);
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization
		alertInfo(req, "TODO: Not implemented yet.");
		resp.sendRedirect(uri);
	}
}