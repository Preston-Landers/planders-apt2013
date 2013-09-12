<%@ tag description="My Streams" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<form action="/manage" method="post" role="form">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">Streams I own</H3>
		</div>
		<div class="panel-body">
			<div class="container">
				<c:choose>
					<c:when test="${ not empty myStreamList }">

						<div class="row">
							<div class="col-md-10">
								<TABLE id="myStreamsTable" class="table table-striped tablesorter">
									<thead>
										<tr>
											<TH>Name</TH>
											<TH>Last New Picture</TH>
											<TH>Number of Pictures</TH>
											<TH>Delete</TH>
										</tr>
									</thead>
									<tbody>
										<c:forEach var='stream' items='${ myStreamList }'>
											<TR>
												<TD><A HREF="${ stream.viewURI }">${fn:escapeXml(stream.name)}</A></TD>
												<TD>
${fn:length(stream.lastNewMedia) > 0  ? stream.lastNewMedia : '<small><em>(None)</em></small>'}</TD>
												<TD>${stream.numberOfMedia}</TD>
												<TD><input type="checkbox" name="delete" value="${ stream.id }"></TD>
											</TR>
										</c:forEach>
									</tbody>
								</TABLE>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<button class="btn btn-danger" name="delete" type="submit">
									<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
									Delete Checked
								</button>
							</div>
						</div>

					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="well">You don't have any streams yet. Create
								some!</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<button onClick="window.location.href='/create'" class="btn btn-primary" type="button">Create Stream</button>
							</div>
						</div>
						
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</form>
