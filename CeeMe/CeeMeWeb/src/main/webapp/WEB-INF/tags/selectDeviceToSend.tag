<%@ tag description="Select a device to send a CeeMe message" language="java"
        pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf" %>
<jsp:useBean id="deviceSelector" scope="page" class="com.appspot.cee_me.model.DeviceSelector"/>

<div id="selectDeviceToSendArea" class="clearfix">
    <label>
        Destination device:
        <select name="to">
            <c:forEach var="device" items="${deviceSelector.deviceSelectorList}">
                <%-- TODO: select the currently selected item --%>
                <option
                        value="<c:out value="${device.key.string}" />"
                        <c:if test="${device.key.string eq param['to'] }">
                            selected
                        </c:if>
                        >
                    <c:out value="${device.name}"/>
                </option>
            </c:forEach>

        </select>
    </label>
</div>
