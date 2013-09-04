<%@ tag description="Connexus Menu bar" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<nav id="mainMenu" class="navbar navbar-default" role="navigation">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="/">Connexus</a>
	</div>

	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav">
			<t:menuTab label="Manage" triggerURIEnd="/manage.jsp" target="/manage"></t:menuTab>
			<t:menuTab label="Create" triggerURIEnd="/create.jsp" target="/create"></t:menuTab>
			<t:menuTab label="View" triggerURIEnd="/view.jsp" target="/view"></t:menuTab>
			<t:menuTab label="Search" triggerURIEnd="/search.jsp" target="/search"></t:menuTab>
			<t:menuTab label="Trending" triggerURIEnd="/trending.jsp" target="/trending"></t:menuTab>
			<t:menuTab label="Social" triggerURIEnd="/social.jsp" target="/social"></t:menuTab>
		</ul>
		<!--     <form class="navbar-form navbar-left" role="search"> -->
		<!--       <div class="form-group"> -->
		<!--         <input type="text" class="form-control" placeholder="Search"> -->
		<!--       </div> -->
		<!--       <button type="submit" class="btn btn-default">Submit</button> -->
		<!--     </form> -->
		<ul class="nav navbar-nav navbar-right">
			<t:menuTab label="Admin" triggerURIEnd="/admin.jsp" target="/admin"></t:menuTab>
			<t:menuTab label="Login" triggerURIEnd="/login.jsp" target="/login"></t:menuTab>
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</nav>

