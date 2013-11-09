<%@ tag description="CeeMe Page template" language="java" pageEncoding="UTF-8"%>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ attribute name="head" required="false" fragment="true"%>
<%@ attribute name="bodyHead" required="false" fragment="true"%>
<%@ attribute name="tail" required="false" fragment="true"%>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${Context.productName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link id="bootswatch-theme" href="${ Context.cssThemeFile != null ? Context.cssThemeFile :  '/bootstrap/css/bootstrap.flatly.min.css' }" rel="stylesheet" media="screen">

<%-- Inlining the small, un-minified CSS files --%>
<style>
 
<%@ include file="/css/CeeMe.css"%>
<%@ include file="/css/colorbox.css"%>

</style>
 

<link rel="apple-touch-icon" href="${pageContext.request.contextPath}/apple-touch-icon.png">

<%-- allow users of this tag to include stuff in the head --%>
<jsp:invoke fragment="head" />

</head>
<body>
	<div id="fb-root"></div>

	<jsp:invoke fragment="bodyHead" />
	<div id="mainContent">
		<div class="container">
			<%-- 			<div id="pageLogo">${productName}</div> --%>

			<t:menu />

			<div class="container">
				<t:status />
				<jsp:doBody />
			</div>
		</div>
	</div>
	
	<%-- FOOTER --%>
	<div id="mainFooter">
		<div class="container">

			<div style="float: right; margin-top: 15px;">

				<t:about />
				
			</div>
		</div>
	</div>

<!--  Congratulations. You have just discovered the secret message. -->
<!--  Please send your answer to Old Pink, care of the Funny Farm, Chalfontay -->

<script src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
<!-- <script src="/js/jquery.cookie.js"></script> -->
<script>
<%@ include file="/js/jquery.cookie.js"%>
</script>

<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>


<!-- <script src="/js/ceeme.js"> -->
<script>
<%@ include file="/js/ceeme.js"%>
</script>

<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

    ga('create', 'UA-45548686-1', 'cee-me.appspot.com');
    ga('send', 'pageview');

</script>

<%-- allow users of this tag to include stuff at the bottom --%>
<jsp:invoke fragment="tail" />

</body>
</html>
