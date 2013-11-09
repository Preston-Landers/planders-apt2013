<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:ceeme>

 <jsp:attribute name="head">
	<script>
		var USE_FB = true;	
	</script>			
 </jsp:attribute>

	<jsp:attribute name="tail">
<script>
window.cx.SocialPageFBReady = function SocialPageFBReady () {
	if (cx.FBUSERID || cx.FBNOTAUTH) {
		$("#fbNotLoggedIn").hide();
		$("#fbLoggedIn").show();
	} else {		
		$("#fbNotLoggedIn").show();
		$("#fbLoggedIn").hide();
	}
}
</script>
</jsp:attribute>	
	
<jsp:body>
	<div class="page-header">
		<h2>Social</h2>
		
		<small><em>Share your photos with the world!</em></small>
	</div>
	
	<div class="row" xmlns:fb="http://www.facebook.com/2008/fbml">
		<div class="col-md-12">
			<div id="socialPanel" class="panel panel-default">
				<div class="panel-heading">
					<H3 class="panel-title">
						Facebook Login  
					</H3>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-2">
							<img style="width: 128px" src="${pageContext.request.contextPath}/images/facebook-logo.png">
						</div>
						<div class="col-md-8">
							<div id="fbNotLoggedIn" style="">
								<div class="social-buttons" style="height: 60px; padding-top: 20px">
									<fb:login-button data-onlogin="FBLoginStatusCallback" size="large" show-faces="true" width="400" max-rows="1" />
								</div>
								<h4 style="padding-top:20px">
									Login with Facebook&trade; to share streams with your friends and groups.
								</h4>
							</div>
							<div id="fbLoggedIn" style="display: none;">
								<h2>
									You have successfully logged in with Facebook&trade;.
								</h2>
								<A class="btn btn-success" HREF="${pageContext.request.contextPath}/manage">Manage your devices.</A>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	

</jsp:body>
</t:ceeme>
