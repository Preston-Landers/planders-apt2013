package com.appspot.cee_me.servlet;

import com.appspot.cee_me.CeeMeContext;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Welcome extends CeeMeServletBase {
	private static final long serialVersionUID = 5623205007285996372L;
	
	public static final String uri = "/welcome";
	public static final String dispatcher = "/WEB-INF/jsp/welcome.jsp";
	public static final String welcomeLoginDestination = "/manage";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		CeeMeContext ceeMeContext = InitializeContext(req, resp); // Base site context initialization

		// The Welcome screen needs a special login URL that takes you somewhere else.
		String welcomeLoginURL = userService.createLoginURL(welcomeLoginDestination);
		req.setAttribute("welcomeLoginURL", welcomeLoginURL);
		
		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp); 
	}


}