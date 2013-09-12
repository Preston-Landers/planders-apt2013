<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:connexus>
	<jsp:body>
		<div class="jumbotron">
			<h1><strong>Welcome to Connexus!</strong></h1>
			<p class="text-center"  style="margin-top: 30px">
			The world's best* photo sharing site!
			</p>
			
			<c:choose>
				<c:when test="${ guser ne null }">
					<p style="margin-top: 80px">
						<small><em>
							You are signed in with your Google Account.
						</em></small>
					</p>
					<div class="row welcome-buttons-row">
						<div class="col-xs-8 col-md-3">
							<a class="btn btn-large btn-success" href="/view">
								Browse Photo Streams
							</a>						
						</div>
						<div class="col-xs-8 col-md-3">
							<a class="btn btn-large btn-primary" href="/create" >
								Create a Photo Stream
							</a>
						</div>
						<div class="col-xs-8 col-md-3">
							<a class="btn btn-large btn-info" href="/manage" >
								Manage your streams
							</a>
						</div>
						<div class="col-xs-8 col-md-3">
							<a class="btn btn-large btn-danger" href="${ logoutURL }" >
								Sign Out of Google
							</a>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<p style="margin-top: 100px">
						<small><em>
							Connexus uses Google Accounts.
						</em></small>
					</p>
					<p style="margin-top: 30px">
						<a class="btn btn-large btn-success" href="${ welcomeLoginURL }">
							Please sign in with Google
						</a>
					</p>
				</c:otherwise>
			</c:choose>
			
		</div>
		
		<h6>
			<em><strong>*</strong>Limitations may apply</em> 
		</h6>
	</jsp:body>
</t:connexus>
