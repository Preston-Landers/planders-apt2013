<%@ tag description="Connexus User CRUD" language="java"
	pageEncoding="UTF-8"%>
<div id="userCrud">
	<H2>User Management</H2>
	<P>Hello, this is the user management page.</P>

	<form action="/usercrud" method="post"><fieldset>
		<p>
			<label for="userName">Account Name:</label> 
			<input name="userName" type="text" autofocus placeholder="Account Name"> 
			
			<label for="realName">Real Name:</label> 
			<input name="realName" type="text" autofocus placeholder="Real Name"> 
			
			<input type="submit" name="create" value="Create New User">
		</p>
		</fieldset>
	</form>
</div>
