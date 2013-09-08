package connexus.servlet;

//import static connexus.OfyService.ofy;
//import java.util.List;
//import connexus.CUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.StreamHandle;

public class Subscribe extends ConnexusServletBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4578267704775630816L;
	public static final String uri = "/subscribe";
	public static final String dispatcher = "/WEB-INF/jsp/view.jsp";
	
	
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
		
		StreamHandle viewingStreamHandle;
		try {
			viewingStreamHandle = StreamHandle.getStreamHandleFromRequest(req, site);
//			viewingStream = viewingStreamHandle.getStream();
//			viewingStreamUser = viewingStreamHandle.getCuser();
//			req.setAttribute("viewingStream", viewingStream);
//			req.setAttribute("viewingStreamUser", viewingStreamUser);
		} catch (RuntimeException e) {
			viewingStreamHandle = new StreamHandle();
		}
		
		if (viewingStreamHandle.getStream() == null) {
			alertError(req, "Stream not found.");
			resp.sendRedirect(uri);
			return;
			
		}
		
		alertInfo(req, "TODO: Not implemented yet.");
		resp.sendRedirect(uri);
	}
}