<%@ tag description="My Devices" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>

<form action="manage" method="post" role="form">
	<div id="createStreamPanel" class="panel panel-default">
		<div class="panel-heading">
			<H3 class="panel-title">My Registered Devices</H3>
		</div>
		<div class="panel-body">
			<div class="container">
				<c:choose>
					<c:when test="${ not empty myDeviceList }">

						<div class="row">
							<div class="col-md-12 table-responsive">
								<TABLE id="myDevicesTable" class="table table-striped tablesorter">
									<thead>
										<tr>
											<TH>Name</TH>
											<TH>Public ID</TH>
											<TH>Device Type</TH>
											<TH>Comment</TH>
                                            <TH>Registered</TH>
                                            <TH>Delete</TH>
										</tr>
									</thead>
									<tbody>
										<c:forEach var='device' items='${ myDeviceList }'>
											<TR>
												<TD>${fn:escapeXml(device.name)}</TD>
                                                <td>${fn:escapeXml(device.publicId)}</td>
                                                <td>${fn:escapeXml(device.hardwareDescription )}</td>
                                                <td>${fn:escapeXml(device.comment )}</td>
                                                <td>${func:formatDateTime(device.creationDate)}</td>
												<TD><input type="checkbox" name="delete" value="${ device.key.string }"></TD>
											</TR>
										</c:forEach>
									</tbody>
								</TABLE>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<button class="btn btn-danger btn-sm" name="doDelete" type="submit">
									<span class="glyphicon glyphicon-remove-circle"></span>&nbsp;
									Delete Checked Registrations
								</button>
							</div>
						</div>

					</c:when>
					<c:otherwise>
						<div class="row">
							<div class="well">
                                You haven't registered any devices yet.
                                <BR>
                                (TODO) Install the Android app today!
                            </div>
						</div>

					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</form>
