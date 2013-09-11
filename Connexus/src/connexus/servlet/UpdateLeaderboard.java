package connexus.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.model.Leaderboard;

public class UpdateLeaderboard extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4152585588958400842L;

	/**
	 * Runs the cron job to update the leaderboard.  (/cron/updatelb is the URL)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		
        Leaderboard LB = Leaderboard.load(null, null); // uses defaults
        
		Leaderboard.generateLeaderBoard();

        // clean up job will do this periodically
        // LB.fixAllStreamMediaCounts();
		LB.maybeRunStreamViewCleanup();
			
		// Run the leaderboard report email if necessary
		LB.maybeRunTrendingReport();
		
		pw.println("Done.");
		pw.flush();
	}
}
