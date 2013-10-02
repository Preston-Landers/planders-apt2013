<%@ tag description="Connexus Menu Tab item LI" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="triggerURIEnd" required="true" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="label" required="true" %>
<li class="${fn:endsWith(pageContext.request.requestURI, triggerURIEnd) ? 'active' : ''}">
	<a href="${target}">${label}</a>
</li>
