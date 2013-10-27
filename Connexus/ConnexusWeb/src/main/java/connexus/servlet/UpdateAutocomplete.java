package connexus.servlet;

import connexus.model.AutocompleteIndex;
import connexus.model.Leaderboard;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UpdateAutocomplete extends HttpServlet {

    private static final long serialVersionUID = 4152585588958400843L;

	/**
	 * Runs the cron job to update the autocomplete.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
        doGenerateAutocompleteIndex();

		pw.println("Index regenerated.");
		pw.flush();
	}

    public static void doGenerateAutocompleteIndex() {
        System.out.println("Rebuilding autocomplete index.");
        AutocompleteIndex.generateAutocompleteIndex();
    }
}
