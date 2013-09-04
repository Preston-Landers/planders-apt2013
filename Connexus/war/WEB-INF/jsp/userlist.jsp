<%@ include file="/WEB-INF/jsp/include.jsp"%>

<c:if test="${ not empty userList }">
	<TABLE class="table">
		<thead><tr>
			<TH>${productName} Account Name</TH>
			<TH>Real Name</TH>
		</tr></thead>
		<c:forEach var='rec' items='${ userList }'>
			<TR>
				<TD>${rec.accountName}</TD>
				<TD>${rec.realName}</TD>
			</TR>
		</c:forEach>
	</TABLE>
</c:if>
<c:if test="${ empty userList }">
<div class="panel-body">There are no users in the system. Create some!</div>
</c:if>