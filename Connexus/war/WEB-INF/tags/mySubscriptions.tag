<%@ tag description="My Subscriptions" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<form action="/subscribe" method="post" role="form">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">Streams I subscribe to</H3>
		</div>
		<div class="panel-body">
			<div class="container">

				<c:choose>
					<c:when test="${ not empty mySubscriptions }">

						<div class="row">
							<div class="col-md-10">
								<div class="form-group">
								<TABLE class="table">
									<thead>
										<tr>
											<TH>Name</TH>
											<TH>Last New Picture</TH>
											<TH>Number of Pictures</TH>
											<TH>Views</TH>
											<TH>Unsubscribe</TH>
										</tr>
									</thead>
									<c:forEach var='sub' items='${ mySubscriptions }'>
										<c:set var="stream" value="${ sub.streamLoaded }"></c:set>
										<TR>
											<TD><A HREF="${ stream.viewURI }">${ stream.name}</A></TD>
											<TD>
${fn:length(stream.lastNewMedia) > 0 ? stream.lastNewMedia: '<small><em>(None)</em></small>'}</TD>
											<TD>${stream.numberOfMedia}</TD>
											<TD>${stream.views}</TD>
											<TD><input type="checkbox" name="vvu" value="${ stream.unsubURI }"></TD>
										</TR>
									</c:forEach>
								</TABLE>
								
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<button name="unsubscribe" class="btn btn-warning" type="submit">
									<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
									Unsubscribe Checked Streams
								</button>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="well">You don't have any subscriptions yet. Subscribe to some today!</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<a href="/view">
									<button class="btn btn-primary" type="button">
										View Streams
									</button>
								</a>
							</div>
						</div>

					</c:otherwise>

				</c:choose>
			</div>
		</div>
	</div>
</form>
