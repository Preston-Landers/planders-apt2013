// sets up a JS namespace called cx
window.cx = (function (cx, $, window, undefined) {
	
	$(document).ready(function() {
		
		// Reparent modals to BODY so when they pop up they don't mess with the main content 
		// (was causing a little jumpiness when popping up)
		// $("body").append($("div.modal"));
		
		// Activate tooltip and popover features
		$(function () {
	        $("[rel='tooltip']").tooltip();
	    });
		$(function () {
	        $("[rel='popover']").popover();
	    });
		
		
		// When uploading a file, automatically set the comment to the filename
		$("#uploadMediaFile").change(function() {
			
			// Choose a default comment based on the filename
			cx.extractCommentFromFilename = function extractCommentFromFilename(fileInput) {
				var fnParts = $(fileInput).val().split("\\");
				var rv = fnParts[fnParts.length-1]; // select base filename out of path
				var arr = /^(.*)[.]\w{3,4}$/g.exec(rv);  // remove any 3 or 4 letter extensions
				rv = arr[1];
				return rv;
			};
			
			$("#uploadMediaComments").val(
					cx.extractCommentFromFilename(this))
					.select().focus();
		});

	});

	return cx;
})( window.cx || {}, jQuery, window );
