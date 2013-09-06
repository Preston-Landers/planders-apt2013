<%@ tag description="My Streams" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="stream" required="true" %>

<form action="/view" method="post" role="form">
	<input type="hidden" name="stream" value="${ stream }">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">Add an image</H3>
		</div>
		<div class="panel-body">
			<div class="container">
				<div class="row">
					<div style="padding-top: 10px;" class="col-md-4">
						<div class="form-group">
							<%-- <span class="btn btn-file">Upload<input type="file" name="media" /></span>  --%>
							<input type="file" name="media">
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<input type="text" class="form-control" id="uploadMediaComments"
								name="comments" placeholder="Comments or description" > 
								
							<label for="uploadMediaComments">
								Comments
							</label>

						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-primary" type="submit">
							<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
							Upload File
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
