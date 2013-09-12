<%@tag import="java.net.URLDecoder"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--  this is the main application layout --%>
<%@ tag description="Connexus Page template" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ tag import="connexus.Config"%>
<%@ attribute name="head" required="false" fragment="true"%>
<%@ attribute name="bodyHead" required="false" fragment="true"%>
<%@ attribute name="tail" required="false" fragment="true"%>
<%@ tag trimDirectiveWhitespaces="true" %>
<%
	request.setAttribute("productName", Config.productName);
	request.setAttribute("productURL", Config.productURL);
	request.setAttribute("aboutText", "<center><h4>Preston Landers</h4><H5>Advanced Programming Tools 2013</H5>" + 
							"<H6>The University of Texas at Austin</H6>" +
							"<h5><small>Department of Computer and<BR>Electrical Engineering</small></H5></center>");

	
	String cssTheme = "/bootstrap/css/bootstrap.spacelab.min.css";
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (int i=0; i<cookies.length; i++) {
			if (cookies[i].getName().equals("css")) {
				cssTheme = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
			}
		}
		if (cssTheme != null && cssTheme.length()>0 ) {
			request.setAttribute("bootswatchTheme", cssTheme);
		}
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${productName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link id="bootswatch-theme" href="${ bootswatchTheme != null ? bootswatchTheme :  '/bootstrap/css/bootstrap.spacelab.min.css' }" rel="stylesheet" media="screen">

<%-- Inlining the small, un-minified CSS files --%>
<style>
 
<%@ include file="/css/Connexus.css"%>
<%@ include file="/css/colorbox.css"%>

</style>
 

<link rel="apple-touch-icon" href="apple-touch-icon.png">

<%-- allow users of this tag to include stuff in the head --%>
<jsp:invoke fragment="head" />

</head>
<body>
	<div id="fb-root"></div>

	<jsp:invoke fragment="bodyHead" />
	<div id="mainContent">
		<div class="container">
			<%-- 			<div id="pageLogo">${productName}</div> --%>

			<t:menu></t:menu>

			<div class="container">
				<t:status></t:status>
				<jsp:doBody />
			</div>
		</div>
	</div>
	
	<%-- FOOTER --%>
	<div id="mainFooter">
		<div class="container">

			<div style="float: right; margin-top: 15px;">

				<t:about></t:about>
				
			</div>
		</div>
	</div>

<!--  Congratulations. You have just discovered the secret message. -->
<!--  Please send your answer to Old Pink, care of the Funny Farm, Chalfontay -->

<script src="/js/jquery-1.10.2.min.js"></script>
<script src="/js/jquery.cookie.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>
<script src="/js/connexus.js"></script>

<script>
 (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
 (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
 m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
 })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
 ga('create', 'UA-43808480-1', 'connexus-apt.appspot.com');
 ga('send', 'pageview');
</script>

<%-- allow users of this tag to include stuff at the bottom --%>
<jsp:invoke fragment="tail" />

</body>
</html>
