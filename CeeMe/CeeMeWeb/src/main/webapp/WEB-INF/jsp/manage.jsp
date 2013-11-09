<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:ceeme>

	<jsp:attribute name="head">
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.tablesorter/themes/blue/style.css">
	</jsp:attribute>
	
	<jsp:attribute name="tail">	
		<script src="${pageContext.request.contextPath}/js/jquery.tablesorter/jquery.tablesorter.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.tablesorter/jquery.metadata.js"></script>
		<script>
			$(document).ready(function() {
				$("#myDevicesTable").tablesorter({ headers: { 5: { sorter: false}  } });
			});
		</script>
	</jsp:attribute>

	<jsp:body>
		<h3>Manage My Devices</h3>
		<t:myDevices />
	</jsp:body>
</t:ceeme>
