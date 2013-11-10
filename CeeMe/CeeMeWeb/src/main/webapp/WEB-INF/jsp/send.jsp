<%@ include file="/WEB-INF/jsp/include.jsp" %>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>
<t:ceeme>
    <jsp:body>
        <form action="${pageContext.request.contextPath}/send" method="post" role="form">
            <div id="sendPanel" class="panel panel-default">
                <div class="panel-heading">
                    <H3 class="panel-title">Send Message to Device</H3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <t:selectDeviceToSend/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col col-md-12">
                            <label style="padding: 15px; width: 100%;">
                                <textarea rows="6" name="messageText" style="width: 100%;">

                                </textarea>
                                <BR>
                                Message
                            </label>
                        </div>
                    </div>
                    <div class="row" style="padding-top: 15px;">
                        <div class="col-md-6">
                            <button class="btn btn-success" type="submit" name="send">
                                <span class="glyphicon glyphicon-send"></span>&nbsp;
                                Send Message
                            </button>
                        </div>
                    </div>

                </div>
            </div>

            <t:recentlySent />
        </form>

    </jsp:body>
</t:ceeme>
