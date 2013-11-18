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
            <span class="pull-right">
                <label>
                    Select
                    <input type="checkbox" name="msgKey" value="${message.key.string}">
                </label>
            </span>
        </div>
        <div class="list-group-item">
            <span class="label label-warning">From:</span>
            <strong> ${message.fromUser.accountName} </strong>
        </div>
        <div class="list-group-item">
            <div class="row">
                <div class="col col-md-6">
                    <span class="label label-info">Link:</span>
                    <strong> <A href="${message.urlData}"><c:out value="${message.urlData}"/></A> </strong>
                </div>
                <div class="col col-md-6">
                    <span class="label label-info">Sent:</span>
                    <strong> ${func:formatDateTime(message.creationDate)} </strong>
                </div>
            </div>
        </div>
    </div>
</div>
