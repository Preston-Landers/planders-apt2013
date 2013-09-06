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
	
			<t:status></t:status>

			<div class="container">
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

	<script src="js/jquery-1.10.2.min.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
