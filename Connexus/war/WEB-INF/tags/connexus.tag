<!DOCTYPE html>
<!--  this is the main application layout -->

<%@ tag description="Connexus Page template" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ tag import="connexus.Utils"%>
<%!String productName = Utils.productName;%>

<html>
<head>
<title><%=productName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">

<link type="text/css" rel="stylesheet" href="Connexus.css">

</head>
<body>
	<div id="pageHeader">
		<div id="pageLogo"><%=productName%></div>
		<!-- replace with logo? -->

		<t:menu></t:menu> 		
	</div>
	<div id="pageBody">
		<t:status></t:status>
		<jsp:doBody />
	</div>
	<div id="pageFooter">&copy; 2013 The Human Fund&tm; <i>Money... for people.</i></div>

<script src="js/jquery-1.10.2.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
