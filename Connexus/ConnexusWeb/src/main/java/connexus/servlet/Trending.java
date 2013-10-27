package connexus.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.ConnexusContext;
import connexus.model.Leaderboard;
import connexus.model.Stream;

public class Trending extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5046653401016720555L;
	public static final String uri = "/trending";
	public static final String dispatcher = "/WEB-INF/jsp/trending.jsp";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization

		req.setAttribute("leaderBoardSize", Leaderboard.lbSize);
		
		// Get the leaderboard
		Leaderboard lb = Leaderboard.load(null, cContext.getSite().getKey());
		
		// Now in cron job.
		// Leaderboard.generateLeaderBoard();
		
		Long reportFreq = lb.getReportFrequencySec();
		if (reportFreq.equals(Leaderboard.FREQ_NONE)) {
			req.setAttribute("FREQ_NONE_CHECKED", true);
		} else if (reportFreq.equals(Leaderboard.FREQ_5MIN)) {
			req.setAttribute("FREQ_5MIN_CHECKED", true);
		} else if (reportFreq.equals(Leaderboard.FREQ_HOUR)) {
			req.setAttribute("FREQ_HOUR_CHECKED", true);
		} else if (reportFreq.equals(Leaderboard.FREQ_DAY)) {
			req.setAttribute("FREQ_DAY_CHECKED", true);
		} else {
			req.setAttribute("FREQ_NONE_CHECKED", true);
		}
		
		List<Stream> leaderBoard = lb.getLeaderBoardList();
		req.setAttribute("leaderBoard", leaderBoard);
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
        ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization
		
		if (req.getParameter("updateRate") != null) {
			String newFreqStr = req.getParameter("reportFreq");
			Long newFreq;
			if (newFreqStr.equals("FREQ_NONE")) {
				newFreq = Leaderboard.FREQ_NONE;
			} else if (newFreqStr.equals("FREQ_5MIN")) {
				newFreq = Leaderboard.FREQ_5MIN;
			} else if (newFreqStr.equals("FREQ_HOUR")) {
				newFreq = Leaderboard.FREQ_HOUR;
			} else if (newFreqStr.equals("FREQ_DAY")) {
				newFreq = Leaderboard.FREQ_DAY;
			} else {
				alertError(req, "Invalid report frequency.");
				resp.sendRedirect(uri);
				return;
			}
			Leaderboard LB = Leaderboard.load(null, cContext.getSite().getKey());
			LB.setReportFrequencySec(newFreq);
			LB.save();
			alertSuccess(req, "Updated report frequency.");			
		} else {
			alertInfo(req, "Unknown request.");
		}
		resp.sendRedirect(uri);
	}
}