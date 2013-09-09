<!DOCTYPE html>
<%--  this is the main application layout --%>
<%@ tag description="Connexus Page template" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ tag import="connexus.Config"%>
<%
	request.setAttribute("productName", Config.productName);
%>

<html lang="en">
<head>
<title>${productName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"
	media="screen">

<link type="text/css" rel="stylesheet" href="css/Connexus.css">

</head>
<body>
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
	<div id="mainFooter">
		<div class="container">
			<div style="float: right;">
			<p class="text-muted credit">&copy; 2013 The Human Fund&trade; <em>Money... for people.</em></p>
			</div>		
		</div>
	</div>

	<script src="/js/jquery-1.10.2.min.js"></script>
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

</body>
</html>
