<%@ tag description="My Streams" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="stream" required="true" %>
<%@ attribute name="streamUser" required="true" %>
<%@ attribute name="redir" required="false" %>

<c:choose>
	<c:when test="${ mySubForStream != null }">
		<c:set var="subActionVerb" value="Unsubscribe from" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="subActionVerb" value="Subscribe to" scope="page" />
	</c:otherwise>
</c:choose>

<%-- TODO: don't show if already subscribed... --%>
<form action="/subscribe" method="post" role="form">
    <c:if test="${ redir != null }">
        <input type="hidden" name="redir" value="${ redir }">
    </c:if>

<%-- All the cool kids use single letter keys --%>
	<input type="hidden" name="v" value="${ viewingStream.objectURI }">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">
				<c:out value="${ subActionVerb }" />
				<strong>${ fn:escapeXml(viewingStream.name) }</strong> from 
				<em>${ fn:escapeXml(viewingStreamUser.realName) }</em>
			</H3>
		</div>
		<div class="panel-body">
			<div class="container">
				<div class="row">
					<div class="col-md-6">
					
						<c:choose>
							<c:when test="${ mySubForStream != null }">
								<button class="btn btn-warning" name="unsubscribe" type="submit">
									<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
									Unsubscribe
								</button>
							</c:when>
							<c:otherwise>
								<button class="btn btn-success" name="subscribe" type="submit">
									<span class="glyphicon glyphicon-star-empty"></span>&nbsp;
									Subscribe
								</button>
							</c:otherwise>
						</c:choose>					
					
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
