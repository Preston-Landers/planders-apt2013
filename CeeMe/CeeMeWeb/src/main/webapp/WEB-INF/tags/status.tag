<%@ tag description="CeeMe status message" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<!-- UI for status/error messages generated by POST handlers -->

<%-- <c:if test="${fn:length(func:getAppStatus(pageContext.session)) > 0}"> --%>

	<div id="statusArea" class="clearfix">
		<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>
		<c:forEach var="statusMessage"
			items="${Context.appStatusListWithClear}">
			<div
				class="alert alert-dismissable fade in ${statusMessage.CSSClass}">
				<span class="${statusMessage.iconClass}" style="padding-right: 5px"></span>
				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
				<%-- shouldn't be user controlled, so don't need to escape Xml --%>
				${statusMessage.message}
			</div>
		</c:forEach>
	</div>
<%-- </c:if> --%>
