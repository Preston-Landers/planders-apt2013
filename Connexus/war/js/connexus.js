// sets up a JS namespace called cx
window.cx = (function (cx, $, window, undefined) {

	cx.extractCommentFromFilename = function extractCommentFromFilename(fileInput) {
		var rawFn = $(fileInput).val();
		var fnParts = rawFn.split("\\");
		var rv = fnParts[fnParts.length-1]; // select last element
		var arr = /^(.*)[.]\w{3,4}$/g.exec(rv);
		rv = arr[1];
		return rv;
	};
	
	$(document).ready(function() {
		
		// When uploading a file, automatically set the comment to the filename
		$("#uploadMediaFile").change(function() {
			$("#uploadMediaComments").val(cx.extractCommentFromFilename(this)).select().focus();
		});

	});

	return cx;
})( window.cx || {}, jQuery, window );
