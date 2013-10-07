<%@ tag description="My Streams" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%-- NOTE: not actually using this attribute ... probably remove --%>
<%@ attribute name="stream" required="true" %>
<%@ attribute name="streamUser" required="true" %>

<form action="${ uploadUrl }" method="post" role="form" enctype="multipart/form-data">
	<%-- All the cool kids use single letter keys --%>
	<input type="hidden" name="v" value="${ viewingStream.objectURI }">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">Add an image to ${ fn:escapeXml(viewingStream.name) }</H3>
		</div>
		<div class="panel-body">
			<div class="container">
				<div class="row">
					<div style="padding-top: 10px;" class="col-md-4">
						<div class="form-group">
							<%-- <span class="btn btn-file">Upload<input type="file" name="media" /></span>  --%>
							<input id="uploadMediaFile" type="file" name="media">
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
						<button onClick="$(this).html('Please wait...');return true;" id="uploadButton" class="btn btn-primary" name="upload" type="submit">
							<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
							Upload File
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
