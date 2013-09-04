<%@ tag description="Connexus User CRUD" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<div id="userCrud" class="panel panel-default">
	<div class="panel-heading">User Management</div>

	<jsp:include page="/WEB-INF/jsp/userlist.jsp" />
</div>

<div id="userCrud" class="panel panel-default">
	<div class="panel-heading">Create User</div>
	<div class="panel-body">
		<form action="/admin" method="post">
			<fieldset>
				<label for="accountName">Create an account:</label> 
				
				<input name="accountName" type="text" autofocus placeholder="Account Name">
				<label for="realName">And your name:</label> <input name="realName"
					type="text" autofocus placeholder="Real Name"> 
					
				<button class="btn btn-primary btn-sm" type="submit" name="create">Create New User</button>
			</fieldset>
		</form>
	</div>
</div>