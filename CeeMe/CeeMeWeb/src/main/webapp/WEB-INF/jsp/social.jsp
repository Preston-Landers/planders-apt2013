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
		
		<%-- NOTE: this is adjustable in connexus.model.Leaderboard --%>
		<small><em>Share your photos with the world!</em></small>
	</div>
	
	<div class="row">
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
							<img width=128 src="/images/facebook-logo.png">
						</div>
						<div class="col-md-8">
							<div id="fbNotLoggedIn" style="">
								<div class="social-buttons" style="height: 60px; padding-top: 20px">
									<fb:login-button data-onlogin="FBLoginStatusCallback" size="large" show-faces="true" width="400" max-rows="1"></fb:login-button>
								</div>
								<h4 style="padding-top:20px">
									Login with Facebook&trade; to share streams with your friends and groups.
								</h4>
							</div>
							<div id="fbLoggedIn" style="display: none;">
								<h2>
									You have successfully logged in with Facebook&trade;.
								</h2>
								<A class="btn btn-success" HREF="/view">Post streams as status updates.</A> 
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	

</jsp:body>
</t:ceeme>
