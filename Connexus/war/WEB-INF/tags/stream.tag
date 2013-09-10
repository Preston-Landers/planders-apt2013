<%@ tag description="Connexus Menu Tab item LI" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="stream" required="true" %>

<%-- A reusable representation of the stream --%>
<li class="${fn:endsWith(pageContext.request.requestURI, triggerURIEnd) ? 'active' : ''}">
	<a href="${target}">${label}</a>
</li>
