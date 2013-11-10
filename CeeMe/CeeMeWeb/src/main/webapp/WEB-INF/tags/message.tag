<%@ tag description="CeeMe message web display" language="java"
        pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf" %>
<%@ attribute name="message" required="true" type="com.appspot.cee_me.model.Message" %>
<%@ attribute name="deleteCheckbox" required="false" type="java.lang.Boolean" %>

<div class="messageContainer">
    <div class="list-group">
        <div class="list-group-item active">
            <span class="h4">
                <c:out value="${message.text}"/>
            </span>
            <c:if test="${deleteCheckbox}">
                <span class="pull-right">
                    <label>
                        Delete
                        <input type="checkbox" name="delete" value="${message.key.string}">
                    </label>
                </span>
            </c:if>
        </div>
        <div class="list-group-item">
            <span class="label label-warning">From:</span>
            <strong> ${message.fromUser.accountName} </strong>
        </div>
        <div class="list-group-item">
            <span class="label label-info">Sent:</span>
            <strong> ${func:formatDateTime(message.creationDate)} </strong>
        </div>
    </div>
</div>