<%@ include file="/WEB-INF/jsp/include.jsp" %>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>

<t:ceeme>
    <jsp:body>
        <div class="jumbotron" style="position: relative">
            <div class="welcome-pane">

                <h1><strong>Welcome to ${Context.productName}!</strong></h1>

                <p class="text-center" style="margin-top: 30px">
                    Send photos, videos, documents and more to your Android device or other users.
                </p>

                <c:choose>
                    <c:when test="${ Context.guser ne null }">
                        <p style="margin-top: 80px">
                            <small><em>
                                You are signed in with your Google Account.
                            </em></small>
                        </p>
                        <div class="row welcome-buttons-row">
                            <div class="col-xs-8 col-md-3">
                                <a class="btn btn-success" href="${pageContext.request.contextPath}/send">
                                    <span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;
                                    Send Files Now!
                                </a>
                            </div>
                            <div class="col-xs-8 col-md-3">
                                <a class="btn btn-primary" onClick="$('#modal-install-app').modal({});return false;">
                                    <span class="glyphicon glyphicon-download-alt"></span>&nbsp;
                                    Install Android App
                                </a>
                            </div>
                            <div class="col-xs-8 col-md-3">
                                <a class="btn btn-info" href="${pageContext.request.contextPath}/manage">
                                    <span class="glyphicon glyphicon-list-alt"></span>&nbsp;
                                    Manage your Devices
                                </a>
                            </div>
                            <div class="col-xs-8 col-md-3">
                                <a class="btn btn-danger" href="${ Context.logoutURL }">
                                    <span class="glyphicon glyphicon-log-out"></span>&nbsp;
                                    Sign Out of Google
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p style="margin-top: 100px">
                            <small><em>
                                    ${Context.productName } uses Google Accounts.
                            </em></small>
                        </p>
                        <p style="margin-top: 30px">
                            <a class="btn btn-lg btn-success" href="${ requestScope.welcomeLoginURL }">
                                <span class="glyphicon glyphicon-log-in"></span>&nbsp;
                                Please sign in with Google
                            </a>
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="welcome-image"></div>

        </div>

        <h6>
            <em><strong>*</strong>This site is currently in development.</em>
        </h6>

        <div id="modal-install-app" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content modal-welcome-install">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><strong>${Context.productName } Android App Installation</strong></h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12 about-col">

                                <p>In order to install this app you will need:</p>
                                <ul>
                                    <li>An Android device running 4.0 or higher (API 14, Ice Cream Sandwich)</li>
                                    <li><P>Set the "Allow installation from Unknown Sources" security setting.</P>
                                        <p>(This is temporary until the app is added to Google Play Store.)</p>
                                    </li>
                                    <li>A Google Account. You can create one from within the app.</li>
                                    <li><P>Either visit the link below in your Android device, or click to download the
                                        file to your
                                        desktop.</P>

                                        <P>From there you can
                                            and then use email or some other method to transfer to the Android
                                            device.</p>
                                    </li>

                                </ul>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 about-col">
                                <A class="btn btn-lg btn-info"
                                   HREF="${pageContext.request.contextPath}/installers/com.appspot.cee_me.android.apk">
                                    <span class="glyphicon glyphicon-download-alt"></span>&nbsp;
                                    Install Android App Now
                                </A>
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
    </jsp:body>
</t:ceeme>
