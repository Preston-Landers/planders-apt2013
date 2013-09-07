package connexus.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;

public class Error extends ConnexusServletBase {

	private static final long serialVersionUID = 5728163889277892752L;
	public static final String dispatcher = "/WEB-INF/jsp/error.jsp";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Analyze the servlet exception
		Throwable throwable = (Throwable) req
				.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) req
				.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) req
				.getAttribute("javax.servlet.error.servlet_name");
		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) req
				.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}
		
		req.setAttribute("errorMessage", throwable.getMessage());
		req.setAttribute("stackTrace", Throwables.getStackTraceAsString(throwable));
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
}