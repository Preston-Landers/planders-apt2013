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
	for (int i=0; i<cookies.length; i++) {
		if (cookies[i].getName() == "css") {
			cssTheme = cookies[i].getValue();
		}
	}
	request.setAttribute("bootswatch-theme", cssTheme);
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${productName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link id="bootswatch-theme" href="${ bootswatch-theme }" rel="stylesheet" media="screen">
 
<link type="text/css" rel="stylesheet" href="/css/Connexus.css">
<link type="text/css" rel="stylesheet" href="/css/colorbox.css">

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

				<div class="thumb-tb" style="float: left;" rel="popover"
					data-toggle="popover" data-placement="top" data-html="true"
					data-trigger="hover" data-content="<t:about></t:about>">
					<button type="button" onClick="return false;"
						class="btn btn-large btn-info">
						<span class="glyphicon glyphicon-info-sign"></span>
						Tools Used
					</button>
				</div>


				<div class="thumb-tb" style="float: left;  margin-left: 25px" rel="popover"
					data-toggle="popover" data-placement="top" data-html="true"
					data-trigger="hover" data-content="${aboutText }">
					<button type="button" onClick="return false;"
						class="btn btn-large btn-warning">
						<span class="glyphicon glyphicon-info-sign"></span>
						About This Site
					</button>
				</div>
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
