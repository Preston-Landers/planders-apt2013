<%@ tag description="Upload Files support script" language="java"
        pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<%-- MiniProject3: file upload --%>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr id="fileupload-preview-row-{%=fileToId(file.name)%}" class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">
                <div class="form-group">
                    <input type="text" class="form-control fileupload-comment" id="uploadMediaComments{%=i%}"
                           name="comments{%=file.name%}" placeholder="Comments or description"
                           value="{%=file.name%}">

                    <label for="uploadMediaComments{%=i%}">
                        Comments
                    </label>
                </div>
            </p>
            {% if (file.error) { %}
            <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <p class="size">{%=o.formatFileSize(file.size)%}</p>
            {% if (!o.files.error) { %}
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
            {% } %}
        </td>
        <td>
            {% if (!o.files.error && !i && !o.options.autoUpload) { %}
            <button class="btn btn-primary start" style="display: none;">
                <i class="glyphicon glyphicon-upload"></i>
                <span>Start</span>
            </button>
            {% } %}
            {% if (!i) { %}
            <button class="btn btn-warning cancel">
                <i class="glyphicon glyphicon-ban-circle"></i>
                <span>Cancel</span>
            </button>
            {% } %}
        </td>
    </tr>
    {% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
            <div><span class="label label-danger">Error</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
            <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
            <i class="glyphicon glyphicon-trash"></i>
            <span>Delete</span>
            </button>
            <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else { %}
            <button class="btn btn-warning cancel">
                <i class="glyphicon glyphicon-ban-circle"></i>
                <span>Cancel</span>
            </button>
            {% } %}
        </td>
    </tr>
    {% } %}
</script>

<script src="js/jquery.file.upload/vendor/jquery.ui.widget.js"></script>

<!-- The Templates plugin is included to render the upload/download listings -->
<script src="http://blueimp.github.io/JavaScript-Templates/js/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="http://blueimp.github.io/JavaScript-Load-Image/js/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="http://blueimp.github.io/JavaScript-Canvas-to-Blob/js/canvas-to-blob.min.js"></script>
<!-- blueimp Gallery script -->
<script src="http://blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="/js/jquery.file.upload/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload.js"></script>
<!-- The File Upload processing plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload-process.js"></script>
<!-- The File Upload image preview & resize plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload-image.js"></script>
<!-- The File Upload audio preview plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload-audio.js"></script>
<!-- The File Upload video preview plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload-video.js"></script>
<!-- The File Upload validation plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload-validate.js"></script>
<!-- The File Upload user interface plugin -->
<script src="/js/jquery.file.upload/jquery.fileupload-ui.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="/js/jquery.file.upload/cors/jquery.xdr-transport.js"></script>
<![endif]-->


<script>
    function fileToId(fname) {
        return fname.replace(".", "-");
    }
    $(function () {
        function allUploadsComplete() {
            // window.alert("Batch upload done");
            $("#uploadInProgressAlert").css("display", "none");
            $("#uploadCompleteAlert").css("display", "block");
            // Wait a few seconds so they can see that it's finished
            window.setTimeout(function () {
                // window.alert("Redirecting...");
                window.location = "${ streamViewUrl }";
            }, 2500);
        }

        var pendingUploads = 0;

        $('#fileupload').fileupload({
            submit: function (e, data) {
                $("#uploadInProgressAlert").css("display", "block");
                var $this = $(this);
                // debugger;
                $.getJSON('/rest/file/url?' + new Date().getTime(), function (result) {
                    data.url = result.url;
                    $this.fileupload('send', data);
                });
                pendingUploads++;
                return false;
            },
            done: function (e, data) {
                // #F5FCF7
                $.each(data.result.files, function (index, file) {
                    var $previewRow = $("#fileupload-preview-row-" + fileToId(file.name));
                    $previewRow.find("td").css("background-color", "#F5FCF7");
                    $previewRow.find(".fileupload-comment").attr("disabled", true);
                    $previewRow.find("td:last").html("Upload Complete!");
                });
                pendingUploads--;
                window.setTimeout(function() {
                    if (pendingUploads == 0) {
                        pendingUploads--;
                        allUploadsComplete();
                    }
                }, 1000);
            }

        });

    });
</script>
