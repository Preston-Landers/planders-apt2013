<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:connexus> <jsp:body>
	
<div class="page-header">
	<h3>Top ${ leaderBoardSize } trending streams</h3>
	
	<%-- NOTE: this is adjustable in connexus.model.Leaderboard --%>
	<small><em>Updated every 5 minutes</em></small>
</div>

<div id="trendingPanel" class="panel panel-default">
	<div class="panel-heading">
		<H3 class="panel-title">
			Trending Streams  
		</H3>
	
	</div>
	<div class="panel-body">
		<div class="row">
			<c:forEach items="${ leaderBoard }" var="stream">
				<div class="col-md-3">		
					Top Streams: ${ stream.name } @ ${ stream.trendingViews } views
				</div>
			</c:forEach>
		</div>
	</div>
</div>

<div id="trendingControlPanel" class="panel panel-default">
	<div class="panel-heading">
		<H3 class="panel-title">
			Control trending report  
		</H3>
	</div>
	<div class="panel-body">
		<div class="container list-group">
			Control your leaderboard report email here. (TODO)
		</div>
	</div>
</div>
	
</jsp:body></t:connexus>
