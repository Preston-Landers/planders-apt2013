// sets up a JS namespace called cx
window.cx = (function (cx, $, window, undefined) {

	if($.cookie("css")) {
		var curTheme = $("#bootswatch-theme").attr("href");
		var cookieTheme = $.cookie("css"); 
		if (curTheme != cookieTheme) {
			$("#bootswatch-theme").attr("href", cookieTheme);
			// window.alert("SWITCH " + cookieTheme + "  " + curTheme);
		}
		
	}
	
	$(document).ready(function() {

		// THEME
		$("#mainMenu .theme-dropdown-menu li a").click(function() { 
			$("#bootswatch-theme").attr("href",$(this).attr('rel'));
			$.cookie("css",$(this).attr('rel'), {expires: 365, path: '/'});
			return false;
		});
		
		window.cx.FBUSERID = null;
		window.cx.FBTOKEN = null;
		window.cx.FBNOTAUTH = null;
		window.cx.FBSTATUS = 'You are not logged into FB.';
		window.cx.FBLoginStatusCallback = function FBLoginStatusCallback(response) {
		  if (response.status === 'connected') {
			    // the user is logged in and has authenticated your
			    // app, and response.authResponse supplies
			    // the user's ID, a valid access token, a signed
			    // request, and the time the access token 
			    // and signed request each expire
			    var uid = response.authResponse.userID;
			    var accessToken = response.authResponse.accessToken;
			    // window.alert("You are logged into FB and authenticated this app.");
			    window.cx.FBUSERID = uid;
			    window.cx.FBTOKEN = accessToken;
				window.cx.FBSTATUS = 'You are logged into FB and have authenticated this app.';
			  } else if (response.status === 'not_authorized') {
			    // the user is logged in to Facebook, 
			    // but has not authenticated your app
				  // window.alert("You are logged into FB but have not authenticated this app.");
					window.cx.FBSTATUS = 'You are logged into FB but have not authenticated this app.';
					window.cx.FBNOTAUTH = true;
			  } else {
			    // the user isn't logged in to Facebook.
				  // window.alert("You are not logged into FB");
			  }
		      $(".social-buttons").css("opacity", "1");

		  	  if (cx.SocialPageFBReady) { cx.SocialPageFBReady(); }
		  	  
		}
		window.FBLoginStatusCallback = cx.FBLoginStatusCallback;
		
		  $.ajaxSetup({ cache: true });
		  $.getScript('//connect.facebook.net/en_US/all.js', function(){
			FB.init({
			  appId: '194168720763795',
			  channelUrl: '//connexus-apt.appspot.com/channel.html', // Channel file for x-domain comms
			  status     : true,                                 // Check Facebook Login status
			  xfbml      : true                                  // Look for social plugins on the page
			});     
		$('#loginbutton,#feedbutton').removeAttr('disabled');
		    FB.getLoginStatus(cx.FBLoginStatusCallback);
		  });

		
		// Activate tooltip and popover features
		$(function () {
	        $("[rel='tooltip']").tooltip();
	    });
		$(function () {
	        $("[rel='popover']").popover();
	    });
		
//		//for each element that is classed as 'pull-down', set its margin-top to the difference between its own height and the height of its parent
//		$('.pull-down').each(function() {
//		    $(this).css('margin-top', $(this).parent().height()-$(this).height())
//		});
		
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

		$("#uploadButton").click(function() {
			if (!$("#uploadMediaFile").val()) {
				return false;
			}
		});

	});

	return cx;
})( window.cx || {}, jQuery, window );
