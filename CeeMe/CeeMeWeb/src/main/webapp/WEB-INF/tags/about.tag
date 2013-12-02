<%@ tag description="CeeMe Menu Tab item LI" language="java"
        pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf" %>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>

<div class="thumb-tb" style="float: left;">
    <button type="button" onClick="$('#modal-instructions').modal({});return false;"
            class="btn btn-sm btn-success">
        <span class="glyphicon glyphicon-question-sign"></span>&nbsp;
        Help for Android App
    </button>
</div>

<div class="thumb-tb" style="float: left; margin-left: 25px">
    <button type="button" onClick="$('#modal-about').modal({});return false;"
            class="btn btn-sm btn-info">
        <span class="glyphicon glyphicon-info-sign"></span>&nbsp;
        Tools Used
    </button>
</div>

<c:set var="aboutText" scope="page">
    <div style="text-align: center">
        <h4>Preston Landers</h4>
        <H5>Advanced Programming Tools 2013</H5>
        <H6>The University of Texas at Austin</H6>
        <h5>
            <small>
                Department of Computer and <BR>
                Electrical Engineering
            </small>
        </H5>
    </div>
</c:set>

<div class="thumb-tb" style="float: left;  margin-left: 25px"
     data-toggle="popover" data-placement="top" data-html="true"
     data-trigger="hover" data-content=" <c:out value="${aboutText }" /> ">
    <button type="button" onClick="return false;"
            class="btn btn-sm btn-warning">
        <span class="glyphicon glyphicon-fire"></span>&nbsp;
        About This Site
    </button>
</div>


<div id="modal-about" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content modal-about-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><strong>${Context.productName } Is Powered By:</strong></h4>
            </div>
            <div class="modal-body">
                <p>This site was created with the following tools:</p>

                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="https://developers.google.com/appengine/" target="_blank">Google App Engine</a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gijtu.html" target="_blank">JavaServer
                            Faces & HTML5</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://getbootstrap.com/" target="_blank">Twitter Bootstrap</a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="http://bootswatch.com/" target="_blank">Bootswatch Themes </a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://jquery.com/" target="_blank">jQuery </a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="http://tablesorter.com/docs/" target="_blank">Tablesorter plugin for jQuery</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://www.jetbrains.com/idea/" target="_blank">IntelliJ IDEA</a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="https://cloud.google.com/products/cloud-storage" target="_blank">Google Cloud Storage</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://developer.android.com/google/gcm/index.html" target="_blank">Google Cloud Messaging</a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="https://developers.google.com/appengine/docs/java/endpoints/" target="_blank">Google Cloud Endpoints</a>
                    </div>
                </div>

                <p style="margin-top: 20px">Special thanks to:</p>

                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://users.ece.utexas.edu/~adnan/" target="_blank">Dr. Adnan Aziz</a>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal">Great, thanks!</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<div id="modal-instructions" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content modal-instructions-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><strong>${Context.productName } Android App Instructions</strong></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12 about-col">
                        <h3>What does this thing do?</h3>

                        <p>This app lets you send links and files from one Android device to another.</p>
                        <ul>
                            <li>Install and launch the app. (See requirements on download page)</li>
                            <li>Sign in with a Google account and register the device.
                            </li>
                            <li>You can send a link or file from any other Android app. Go to the file in another app
                                and use the
                                Share option to open in ${Context.productName}
                            </li>
                            <li>
                                Likewise you can receive incoming messages (links or files) from other users. When a
                                message comes in you will get a notification in the bar at the top of your screen, or if
                                the app is already open, the file will open immediately.
                            </li>
                            <li>
                                Depending on the type of file sent (e.g. a video recorded by your phone) you may not
                                always be able to play it on the other end.
                            </li>
                            <li>
                                Regardless, the transferred files can be found in your Android device in
                                <code>Android/data/com.appspot.cee_me.android/files/</code>, organized by date.
                            </li>

                        </ul>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">
                    <span class="glyphicon glyphicon-remove-circle"></span>
                    Close
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
