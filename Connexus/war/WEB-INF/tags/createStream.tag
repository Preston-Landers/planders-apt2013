<%@ tag description="Connexus Create Stream" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<%-- move to another file? TEXTAREA sizes --%>
<c:set var="txtRows" scope="page" value="3"/>
<c:set var="txtCols" scope="page" value="50"/>

<form action="/create" method="post" role="form">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">Create a ${productName} Stream</div>
		<div class="panel-body">
			<div class="container">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<input autofocus type="text" class="form-control" id="createStreamName"
								name="name" placeholder="Give your stream a name">
							<label for="createStreamName">Name your stream</label>						
						</div>
						<div class="form-group">
							<div>
								<textarea id="createStreamSubscribers" name="subscribers"
									class="field"
									rows="${ txtRows }"
									placeholder="adnan@aziz.com, billg@microsoft.com">${param['subscribers']}</textarea>
								<textarea id="createStreamSubscribersNote"
									class="field"
									name="subscribersNote" rows="${ txtRows }" 
									placeholder="Optional message for invite">${param['subscribersNote']}</textarea>
							</div>
							<label for="createStreamSubscribers">Add Subscribers</label>						
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<div>
								<textarea id="createStreamTags" name="tags" class="field"
									rows="${ txtRows }"
									placeholder="#PacificOcean, #FriendsOfAziz, #music">${param['tags']}</textarea>
							</div>
							<label for="createStreamTags">
								Tag your stream
							</label>						
						</div>
						<div class="form-group">
							<input type="text" class="form-control" id="createStreamCoverURL"
								name="cover" placeholder="Optional URL of cover image" value="${param['cover']}">
							<label for="createStreamCoverURL">
								URL to cover image
								<BR />
								<h6>
									<em>Tip: you can set a cover later after you upload some pictures</em>
								</h6>
								
							</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-success btn-lg" type="submit">Create Stream</button>
					</div>
				</div>
			</div>
		</div>

	</div>

</form>
