package com.appspot.cee_me.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;

public class Error extends CeeMeServletBase {

	private static final long serialVersionUID = 5728163889277892752L;
	private static final String dispatcher = "/WEB-INF/jsp/error.jsp";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

        InitializeContext(req); // Base site context initialization

        // Analyze the servlet exception
		Throwable throwable = (Throwable) req
				.getAttribute("javax.servlet.error.exception");
//		Integer statusCode = (Integer) req
//				.getAttribute("javax.servlet.error.status_code");

		if (throwable != null) {
			req.setAttribute("errorMessage", throwable.getMessage());
			req.setAttribute("stackTrace", Throwables.getStackTraceAsString(throwable));
		} else {
			req.setAttribute("errorMessage", "There was a problem handling your request.");
			req.setAttribute("stackTrace", "(None available)");
		}
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}
}