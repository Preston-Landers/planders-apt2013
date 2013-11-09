<%@ tag description="CeeMe Menu bar" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>

<nav id="mainMenu" class="navbar navbar-default" role="navigation">
	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-ex1-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
        <%-- TODO: logo here would be cool --%>
		<a class="navbar-brand" href="${pageContext.request.contextPath}/" title="${Context.productName}">
            <img class="navbar-brand-img" src="${pageContext.request.contextPath}/images/ceeme_logo_1.png">
		</a>
	</div>

	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav">
			<t:menuTab label="Manage" triggerURIEnd="/manage.jsp" target="manage" />
			<t:menuTab label="Send" triggerURIEnd="/send.jsp" target="send" />
			<t:menuTab label="Search" triggerURIEnd="/search.jsp" target="search" />
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
				<a data-toggle="dropdown" href="#">Theme</a>
				<ul class="dropdown-menu theme-dropdown-menu" role="menu" aria-labelledby="dLabel">
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.amelia.min.css">
							Amelia
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.cerulean.min.css">
							Cerulean
						</a>
					</li>
					
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.cosmo.min.css">
							Cosmo
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.cyborg.min.css">
							Cyborg
						</a>
					</li>
					
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.flatly.min.css">
							Flatly
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.journal.min.css">
							Journal
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.readable.min.css">
							Readable
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.simplex.min.css">
							Simplex
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.slate.min.css">
							Slate
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.spacelab.min.css">
							Spacelab
						</a>
					</li>
					<li role="presentation">
						<a role="menuitem" tabindex="-1" href="#" rel="/bootstrap/css/bootstrap.united.min.css">
							United
						</a>
					</li>
				</ul>
			</li>
		
		
		
			<t:menuLoginTab />
		</ul>
	</div>
	<!-- /.navbar-collapse -->
</nav>

