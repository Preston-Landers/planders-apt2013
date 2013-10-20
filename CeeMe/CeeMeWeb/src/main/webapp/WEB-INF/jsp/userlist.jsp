<%@ include file="/WEB-INF/jsp/include.jsp"%>
<c:choose>
	<c:when test="${ editItem != null }">
		<P>EDITING ITEM <c:out value="${ editItem }"></c:out></P>
	</c:when>
	<c:when test="${ not empty userList }">
		<TABLE class="table">
			<thead>
				<tr>
					<TH>${productName} Account Name</TH>
					<TH>Real Name</TH>
					<TH>Action</TH>
				</tr>
			</thead>
			<c:forEach var='user' items='${ userList }'>
				<TR>
					<TD>${user.accountName}</TD>
					<TD>${user.realName}</TD>
					<TD>
						<form action="/admin" method="post">
							<input type="hidden" name="id" value="${user.id}" />
							<A href="/admin?edit=${user.id}">
								<button type="button" class="btn btn-default btn-sm">
									<span class="glyphicon glyphicon-edit"></span>&nbsp;Edit
								</button>
							</A>
							<button name="delete" type="submit"
								class="btn btn-default btn-sm">
								<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;Delete
							</button>
						</form>
					</TD>
				</TR>
			</c:forEach>
		</TABLE>
	</c:when>
	<c:otherwise>
		<div class="panel-body">
			There are no users in the system. Create some!
		</div>
	</c:otherwise>
</c:choose>
