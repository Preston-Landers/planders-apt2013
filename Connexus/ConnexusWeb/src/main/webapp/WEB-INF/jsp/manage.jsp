<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:connexus>

	<jsp:attribute name="head">
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.tablesorter/themes/blue/style.css">
	</jsp:attribute>
	
	<jsp:attribute name="tail">	
		<script src="${pageContext.request.contextPath}/js/jquery.tablesorter/jquery.tablesorter.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.tablesorter/jquery.metadata.js"></script>
		<script>
			$(document).ready(function() {
				$("#mySubsTable").tablesorter({ headers: { 4: { sorter: false}  } });
				$("#myStreamsTable").tablesorter({ headers: { 3: { sorter: false}  } });
			});
		</script>
	</jsp:attribute>

	<jsp:body>
		<h3>Stream Management</h3>
		<t:myStreams></t:myStreams>
		<t:mySubscriptions></t:mySubscriptions>
	</jsp:body>
</t:connexus>
