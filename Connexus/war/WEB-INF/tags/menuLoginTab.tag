<%@ tag description="Connexus Menu Tab item LI" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<c:choose>
	<c:when test="${ guser ne null }">
		<li class="dropdown">
			<a data-toggle="dropdown" href="#"><strong>${ guser.email }</strong></a>
			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="#">Logged in as ${ guser.nickname } </a></li>
				<li role="presentation"><a role="menuitem" tabindex="-1"
					href="${ logoutURL }">Logout</a></li>
			</ul>
		</li>
	</c:when>
	<c:otherwise>
		<li><a href="${ loginURL }">Login</a></li>
	</c:otherwise>
</c:choose>
