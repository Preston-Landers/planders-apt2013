<%@ tag description="Connexus Page template" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ tag import="connexus.Utils"%>
<%!String productName = Utils.productName;%>

<html>
<head>
<title><%=productName%></title>

<!-- <link rel="stylesheet" -->
<!-- 	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" /> -->
<!-- <script src="http://code.jquery.com/jquery-1.9.1.js"></script> -->
<!-- <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script> -->

<!-- <link type="text/css" rel="stylesheet" href="jqbase.css"> -->
<link type="text/css" rel="stylesheet" href="Connexus.css">

</head>
<body>
	<div id="pageHeader">
		<div id="pageLogo"><%=productName%></div>
		<!-- replace with logo? -->

		<t:menu></t:menu> 		
	</div>
	<div id="pageBody">
		<jsp:doBody />
	</div>
	<div id="pageFooter">&copy; 2013 APT Industries, Inc.</div>
</body>
</html>
