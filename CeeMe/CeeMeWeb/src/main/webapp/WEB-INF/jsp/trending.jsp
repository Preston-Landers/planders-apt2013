<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:ceeme> <jsp:body>
	
<div class="page-header">
	<h3>Top ${ leaderBoardSize } trending streams</h3>
	
	<%-- NOTE: this is adjustable in connexus.model.Leaderboard --%>
	<small><em>Updated every 5 minutes</em></small>
</div>

<div class="row">
	<div class="col-md-9">
		<div id="trendingPanel" class="panel panel-default">
			<div class="panel-heading">
				<H3 class="panel-title">
					Trending Streams  
				</H3>
			</div>
			<div class="panel-body">
				<div class="row">
					<c:choose>
						<c:when test="${ (leaderBoard eq null) or (fn:length(leaderBoard) == 0) }">
							<div class="container">
								<P>
									<em>No streams have been viewed in the last hour. (Sure seems quiet around here.)</em>
								</P>
							</div>
						</c:when>
						<c:otherwise>
							<c:forEach items="${ leaderBoard }" var="stream">	
								<div class="col-md-4">
									<t:stream stream="${ stream }" trending="true"></t:stream>
								</div>
							</c:forEach>						
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>

	</div>

	<div class="col-md-3">
	
		<div id="trendingControlPanel" class="panel panel-default">
			<div class="panel-heading">
				<H3 class="panel-title">
					Control trending report email  
				</H3>
			</div>
			<div class="panel-body">
				<div class="container">
					<form id="formReportFreq" role="form" action="/trending" method="post">
					
						<ul class="list-group">
						  <li class="list-group-item">
							<div class="radio">
							  <label>
							    <input type="radio" name="reportFreq" id="reportFreq1"
												value="FREQ_NONE" ${ FREQ_NONE_CHECKED ? 'checked' : ''}>
							    No reports
							  </label>
							</div>
						  </li>
						  <li class="list-group-item">
							<div class="radio">
							  <label>
							    <input type="radio" name="reportFreq" id="reportFreq2"
												value="FREQ_5MIN" ${ FREQ_5MIN_CHECKED ? 'checked' : ''}>
							    Every 5 minutes
							  </label>
							</div>
						  </li>
						  
						  <li class="list-group-item">
							<div class="radio">
							  <label>
							    <input type="radio" name="reportFreq" id="reportFreq3"
												value="FREQ_HOUR" ${ FREQ_HOUR_CHECKED ? 'checked' : ''}>
							    Every 1 hour
							  </label>
							</div>
						  </li>
						  
						  <li class="list-group-item">
							<div class="radio">
							  <label>
							    <input type="radio" name="reportFreq" id="reportFreq4"
												value="FREQ_DAY" ${ FREQ_DAY_CHECKED ? 'checked' : ''}>
							    Every day
							  </label>
							</div>
						  </li>
						</ul>			
					
						<div class="row"><div class="col-md-8">
							<button rel="tooltip" data-placement="bottom" data-toggle="tooltip"
											title="Please don't click this unless you're the class instructor or TA"
											style="margin-top: 20px" name="updateRate" value="go"
											class="btn btn-primary" type="submit">
								Update Rate
							</button>		
						</div></div>
					</form>
				</div>
			</div>
		</div>
	
	</div>	

</div>	
</jsp:body></t:ceeme>
