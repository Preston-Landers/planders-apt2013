<%@ tag description="Messages I have recently sent" language="java"
        pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf" %>
<jsp:useBean id="messageQuery" scope="request" class="com.appspot.cee_me.model.MessageQuery"/>
<c:set var="messageList" scope="page" value="${messageQuery.messageList}"/>
<c:choose>
    <c:when test="${ empty messageList}">
        <%-- something here to indicate no available messages... --%>
    </c:when>
    <c:otherwise>
        <div id="recentlySentPanel" class="panel panel-default">
            <div class="panel-heading">
                <H3 class="panel-title">
                    Recently sent messages to
                    <strong><c:out value="${messageQuery.toDevice.name}"/></strong>
                </H3>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <c:forEach items="${messageList}" var="message">
                            <t:message message="${message}" deleteCheckbox="${true}"/>
                        </c:forEach>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <button class="btn btn-danger btn-sm" type="submit" name="deleteMessages" value="deleteMessages">
                            <span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
                            Delete Messages
                        </button>
                    </div>
                </div>
            </div>
        </div>

    </c:otherwise>
</c:choose>
