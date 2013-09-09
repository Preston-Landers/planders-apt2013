package connexus.servlet;

import static connexus.OfyService.ofy;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import connexus.Config;
import connexus.model.Leaderboard;
import connexus.model.Site;
import connexus.model.StreamView;

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
		
        Site site = ofy().load().type(Site.class).id(Config.siteId).get();
        if (site == null) {
        	// Should probably be logging something somewhere...
        	pw.println("Internal error: site entity was not initialized.");
        	return;
        }        
		
        Leaderboard LB = Leaderboard.load(null, null); // uses defaults
        
		Leaderboard.generateLeaderBoard();
		
		StreamView.cleanupStreamViews();
			
		// Run the leaderboard report email if necessary
		LB.maybeRunTrendingReport();
		
		pw.println("Done.");
		pw.flush();
	}
}
