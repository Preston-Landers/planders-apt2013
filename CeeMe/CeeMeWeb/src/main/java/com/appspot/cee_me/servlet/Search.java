package com.appspot.cee_me.servlet;

import com.appspot.cee_me.CeeMeContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Search extends CeeMeServletBase {
	private static final long serialVersionUID = 5623205007285996382L;
	
	// public static final String uri = "/search";
	private static final String dispatcher = "/WEB-INF/jsp/search.jsp";

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		@SuppressWarnings("unused")
        CeeMeContext context = InitializeContext(req); // Base site context initialization


		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}


}