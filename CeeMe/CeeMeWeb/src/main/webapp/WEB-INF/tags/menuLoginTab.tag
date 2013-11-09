<%@ tag description="CeeMe Menu Tab item LI" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>

<c:choose>
	<c:when test="${ Context.guser ne null }">
		<li class="dropdown">
			<a data-toggle="dropdown" href="#"><strong>${ fn:escapeXml(Context.guser.email) }</strong></a>
			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
				<li role="presentation">
					<div class="container">
<!-- 					<a role="menuitem" tabindex="-1" href="#"> -->
						<P>Signed in as <strong>${ fn:escapeXml(Context.guser.nickname) }</strong></P>
<!-- 					</a> -->
					</div>
				</li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="${ Context.logoutURL }">Sign out of Google</a></li>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<li><a href="${ Context.loginURL }">Login</a></li>
	</c:otherwise>
</c:choose>
