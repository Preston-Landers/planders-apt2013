package connexus.servlet;

import static connexus.OfyService.ofy;
//import java.util.List;
//import connexus.CUser;




import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connexus.model.Leaderboard;
import connexus.model.Stream;
import connexus.status.*;

public class Trending extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5046653401016720555L;
	public static final String uri = "/trending";
	public static final String dispatcher = "/WEB-INF/jsp/trending.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization

		req.setAttribute("leaderBoardSize", Leaderboard.lbSize);
		
		// Get the leaderboard
		// Leaderboard lb = ofy().load().type(Leaderboard.class).parent(site).id(Leaderboard.lbId).get();
		Leaderboard lb = Leaderboard.load(null, site.getKey());
		
		// TODO: move to cron job!
		Leaderboard.generateLeaderBoard();
		
		List<Stream> leaderBoard = lb.getLeaderBoardList();
		req.setAttribute("leaderBoard", leaderBoard);
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
//	public void doPost(HttpServletRequest req, HttpServletResponse resp)
//			throws IOException, ServletException {
//		InitializeContext(req, resp); // Base site context initialization
//		alertInfo(req, "TODO: Not implemented yet.");
//		resp.sendRedirect(uri);
//	}
}