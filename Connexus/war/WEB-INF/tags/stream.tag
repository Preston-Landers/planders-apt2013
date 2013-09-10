<%@ tag description="Connexus Stream" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="stream" required="true"  type="connexus.model.Stream" %>
<%@ attribute name="trending" required="false"  %>

<%-- A reusable representation of the stream --%>

<!-- TODO: special display for trending! -->
<%-- Top Streams: ${ stream.name } @ ${ stream.trendingViews } views --%>


<a href="${ stream.viewURI }" class="list-group-item">
	<h4 class="list-group-item-heading">
		${ stream.name } <small>by <strong>${ stream.ownerName }</strong></small>
	</h4>
	<p class="list-group-item-text">${ stream.numberOfMedia } images
		&ndash; last updated: ${ stream.lastNewMedia } &ndash; ${ stream.views }
		view${ stream.views > 1 ? 's' : '' }</p>
		
	<c:if test="${ trending }">
		<p class="list-group-item-text">
			Trending info: 
		</p>
	</c:if>
		
</a>
