package connexus.servlet;

import com.google.gson.Gson;
import connexus.Config;
import connexus.ViewContext;
import connexus.endpoints.GeoStream;
import connexus.endpoints.Media;
import connexus.model.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Geographic view of a Stream.
 */
public class GeoView extends HttpServlet {
    public static final String uri = "/geo";
    public static final String dispatcher = "/WEB-INF/jsp/geoview.jsp";


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Sets up the request attributes needed by the JSP
        ViewContext viewContext = View.InitializeViewContext(req, resp);

        if (req.getParameter("jsonget") != null) {
            doJsonStreamGeoView(viewContext, req, resp);
            return;
        }

        // Forward to JSP page to display them in a HTML table.
        req.getRequestDispatcher(dispatcher).forward(req, resp);
    }

    /**
     * Show a JSON view of the images & coordinates w/in a stream.
     */
    public void doJsonStreamGeoView(ViewContext viewContext, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        Date beginDate = null;
        Date endDate = null;
        String beginDateStr = req.getParameter("begin");
        if (beginDateStr != null) {
            beginDate = Config.paramToDate(beginDateStr);
        }
        if (beginDate == null) {
            jsonError("Missing 'begin' date.'", req, resp);
            return;
        }
        String endDateStr = req.getParameter("end");
        if (endDateStr != null) {
            endDate = Config.paramToDate(endDateStr);
        }
        if (endDate == null) {
            jsonError("Missing 'end' date.'", req, resp);
            return;
        }

        Stream stream = viewContext.getViewingStream();
        List<Media> apiMediaList = new ArrayList<Media>();
        for (connexus.model.Media media : stream.getMediaByDateRange(beginDate, endDate)) {
            apiMediaList.add(Media.convertMediaToAPI(media));
        }
        GeoStream geoStream = GeoStream.convertStreamToGeoAPI(stream);
        connexus.endpoints.Stream apiStream = geoStream.getStream();
        apiStream.setMediaList(apiMediaList);
        geoStream.setBeginDate(beginDate);
        geoStream.setEndDate(endDate);

        // Return a JSON string describing the geo-view of the stream
        Gson gson = new Gson();
        resp.setContentType("text/json");
        PrintWriter outpw = resp.getWriter();
        String outJson = gson.toJson(geoStream);
        outpw.println(outJson);

    }

    private void jsonError(String msg, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        resp.sendError(500, msg);
    }

}
