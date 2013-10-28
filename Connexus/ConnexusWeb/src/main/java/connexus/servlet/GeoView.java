package connexus.servlet;

import connexus.ViewContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Geographic view of a Stream.
 */
public class GeoView extends HttpServlet {
    public static final String uri = "/geo";
    public static final String dispatcher = "/WEB-INF/jsp/geoview.jsp";


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ViewContext viewContext =  View.InitializeViewContext(req, resp);

        // Forward to JSP page to display them in a HTML table.
        req.getRequestDispatcher(dispatcher).forward(req, resp);
    }


}
