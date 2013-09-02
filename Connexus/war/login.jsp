<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collections"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.googlecode.objectify.Objectify"%>
<%@ page import="com.googlecode.objectify.ObjectifyService"%>
<%@ page import="connexus.CUser"%>
<%@ page import="connexus.Utils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%!
	String productName = Utils.productName;
%>

<html>
<head>
<title><%=productName%></title>
</head>
<body>
	<%
		String guestbookName = request.getParameter("guestbookName");
		if (guestbookName == null) {
			guestbookName = "default";
		}
		pageContext.setAttribute("guestbookName", guestbookName);
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			pageContext.setAttribute("user", user);
	%>
	<p>
		Hello, ${fn:escapeXml(user.nickname)}! You are already logged in. (You can <a
			href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
			out</a>.)
	</p>
	<%
		} else {
	%>
	<p>
		Hello! <a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
			in</a> to include your name with greetings you post.
	</p>
	<%
		}
	%>
	<%
		ObjectifyService.register(CUser.class);
	%>

</body>
</html>