<%@ tag description="Connexus status message" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
  
<div id="statusArea" class="clearfix">
	<div id="statusAreaMsg"> 
		<c:if test="${sessionScope.statusMessage != \"\"}">
			Status msg: ${sessionScope.statusMessage}
			<c:set var="statusMessage" scope="session" value="" />
		</c:if>

	</div>
	<div id="statusAreaError"></div>
</div>
