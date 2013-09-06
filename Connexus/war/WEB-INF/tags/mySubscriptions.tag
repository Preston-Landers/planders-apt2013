<%@ tag description="My Subscriptions" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<form action="/manage" method="post" role="form">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">Streams I subscribe to</H3>
		</div>
		<div class="panel-body">
			<div class="container">

				<c:choose>
					<c:when test="${ not empty subscriptionList }">

						<div class="row">
							<div class="col-md-8">
								<div class="form-group">This is your subscription table.</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<button class="btn btn-primary" type="submit">
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
