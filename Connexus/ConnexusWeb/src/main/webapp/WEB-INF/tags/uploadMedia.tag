<%@ tag description="My Streams" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%-- NOTE: not actually using this attribute ... probably remove --%>
<%@ attribute name="stream" required="true" %>
<%@ attribute name="streamUser" required="true" %>

<form method="post" role="form" id="fileupload" enctype="multipart/form-data">
	<%-- All the cool kids use single letter keys --%>
	<input type="hidden" name="v" value="${ viewingStream.objectURI }">
    <input type="hidden" name="upload" value="true">
	<div id="createStreamPanel" class="panel panel-default  jquery-ui">
		<div class="panel-heading">
			<H3 class="panel-title">Upload pictures to <b>${ fn:escapeXml(viewingStream.name) }</b></H3>
		</div>
		<div class="panel-body">
			<div class="container">
                <div class="row">
                    <div id="uploadCompleteAlert" class="alert alert-success" style="display: none;">
                        <span class="glyphicon glyphicon-thumbs-up" style="margin-right: 6px;"></span>
                        Upload complete. Please wait a moment.
                    </div>
                    <div id="uploadInProgressAlert" class="alert alert-info" style="display: none;">
                        <span class="glyphicon glyphicon-link" style="margin-right: 6px;"></span>
                        Please wait while I contact the upload server.
                        <img style="margin-left: 6px;" src="images/jquery.file.upload/loading.gif" width="20px" height="20px">
                    </div>
                </div>
                <div class="row">
                    <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
                    <div class="row fileupload-buttonbar">
                        <div class="col-lg-7">
                            <!-- The fileinput-button span is used to style the file input field as button -->
                            <span class="btn btn-success fileinput-button">
                                <i class="glyphicon glyphicon-plus"></i>
                                <span>Add files...</span>
                                <input type="file" name="media" multiple>
                            </span>
                            <button type="submit" class="btn btn-primary start">
                                <i class="glyphicon glyphicon-upload"></i>
                                <span>Start upload</span>
                            </button>
                            <button type="reset" class="btn btn-warning cancel">
                                <i class="glyphicon glyphicon-ban-circle"></i>
                                <span>Cancel upload</span>
                            </button>

                            <%--<button type="button" class="btn btn-danger delete">--%>
                                <%--<i class="glyphicon glyphicon-trash"></i>--%>
                                <%--<span>Delete</span>--%>
                            <%--</button>--%>
                            <%--<input type="checkbox" class="toggle">--%>
                            <!-- The loading indicator is shown during file processing -->
                            <span class="fileupload-loading"></span>
                        </div>
                        <!-- The global progress information -->
                        <div class="col-lg-5 fileupload-progress fade">
                            <!-- The global progress bar -->
                            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0"
                                 aria-valuemax="100">
                                <div class="progress-bar progress-bar-success" style="width:0%;"></div>
                            </div>
                            <!-- The extended global progress information -->
                            <div class="progress-extended">&nbsp;</div>
                        </div>
                    </div>
                    <div class="row fileupload-previewtable">
                        <div class="col-lg-12">
                            <!-- The table listing the files available for upload/download -->
                            <table role="presentation" class="table table-striped">
                                <tbody class="files"></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row fileupload-hint">
                        <div class="col-lg-6">
                            <span class="help-block">
                                <em>Tip:</em> You can drag & drop files from your desktop or other programs to here.
                                <BR>You can also select multiple files in the Add Files chooser.
                            </span>
                        </div>
                    </div>
                </div>
			</div>
		</div>
	</div>
</form>

<!-- The blueimp Gallery widget -->
<div id="blueimp-gallery" class="blueimp-gallery blueimp-gallery-controls" data-filter=":even">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev">‹</a>
    <a class="next">›</a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>
