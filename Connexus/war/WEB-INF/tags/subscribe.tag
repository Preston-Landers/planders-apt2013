<%@ tag description="My Streams" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="stream" required="true" %>
<%@ attribute name="streamUser" required="true" %>

<%-- TODO: don't show if already subscribed... --%>
<form action="/subscribe" method="post" role="form">
	<%-- All the cool kids use single letter keys --%>
	<input type="hidden" name="v" value="${ stream }">
	<input type="hidden" name="vu" value="${ streamUser }">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">
				Subscribe to <strong>${ viewingStream.name }</strong> from 
				<em>${ viewingStreamUser.realName }</em>
			</H3>
		</div>
		<div class="panel-body">
			<div class="container">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-success" name="subscribe" type="submit">
							<span class="glyphicon glyphicon-check"></span>&nbsp;
							Subscribe
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
