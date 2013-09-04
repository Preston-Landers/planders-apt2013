<!DOCTYPE html>
<!--  this is the main application layout -->

<%@ tag description="Connexus Page template" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ tag import="connexus.Utils"%>
<%
	request.setAttribute("productName", Utils.productName);
%>

<html lang="en">
<head>
<title>${productName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"
	media="screen">

<link type="text/css" rel="stylesheet" href="Connexus.css">

</head>
<body>
	<div id="pageHeader">
		<div id="pageLogo">${productName}</div>
		<!-- replace with logo? -->

		<t:menu></t:menu>
	</div>
	<div id="pageBody">
		<t:status></t:status>
		<jsp:doBody />
	</div>
	<div id="pageFooter">
		<div style="float: right;">
			&copy; 2013 The Human Fund&trade; <i>Money... for people.</i>
		</div>
	</div>

	<script src="js/jquery-1.10.2.js"></script>
	<script src="bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
