<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%--  this is the main application layout --%>
<%@ tag description="Connexus Page template" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ tag import="connexus.Config"%>
<%
	request.setAttribute("productName", Config.productName);
	request.setAttribute("productURL", Config.productURL);
%>

<html lang="en">
<head>
	<title>${productName}</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"
		media="screen">
	
	<link type="text/css" rel="stylesheet" href="css/Connexus.css">
	
	<c:if test="${ viewingStream ne null }">
		<%-- TODO: hostname --%>
		<meta property="og:url"             content="http://localhost${ viewingStream.viewURI }" /> 
		<meta property="og:title"           content="${ viewingStream.name }" /> 
		<meta property="og:image"           content="${ viewingStream.coverURL }" /> 
	</c:if>
	
</head>
<body>

	<div id="fb-root"></div>
	<script>
	  window.fbAsyncInit = function() {
	    // init the FB JS SDK
	    FB.init({
	      appId      : '194168720763795', 
	      channelUrl : '//connexus-apt.appspot.com/channel.html', // Channel file for x-domain comms
	      status     : true,                                 // Check Facebook Login status
	      xfbml      : true                                  // Look for social plugins on the page
	    });
	
	    // Additional initialization code such as adding Event Listeners goes here
	  };
	
	  // Load the SDK asynchronously
	  (function(d, s, id){
	     var js, fjs = d.getElementsByTagName(s)[0];
	     if (d.getElementById(id)) {return;}
	     js = d.createElement(s); js.id = id;
	     js.src = "//connect.facebook.net/en_US/all.js";
	     fjs.parentNode.insertBefore(js, fjs);
	   }(document, 'script', 'facebook-jssdk'));
	</script>

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
