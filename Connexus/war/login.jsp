<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Collections"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.googlecode.objectify.Objectify"%>
<%@ page import="com.googlecode.objectify.ObjectifyService"%>
<%@ page import="connexus.CUser"%>
<%@ page import="connexus.Utils"%>

<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
%>

<t:connexus>
	<jsp:body>
	This is the login page.
	</jsp:body>
</t:connexus>

<%
	ObjectifyService.register(CUser.class);
%>