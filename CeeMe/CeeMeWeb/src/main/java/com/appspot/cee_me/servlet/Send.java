package com.appspot.cee_me.servlet;

import com.appspot.cee_me.CeeMeContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Send extends CeeMeServletBase {
	private static final long serialVersionUID = 5623205007285996379L;
	
	// public static final String uri = "/send";
	private static final String dispatcher = "/WEB-INF/jsp/send.jsp";

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		@SuppressWarnings("unused")
        CeeMeContext ceeMeContext = InitializeContext(req); // Base site context initialization

		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}


}