<%@ tag description="Connexus Stream" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="stream" required="true" type="connexus.model.Stream"%>
<%@ attribute name="trending" required="false"%>

<%-- A reusable representation of the stream --%>

<!-- TODO: special display for trending! -->
<%-- Top Streams: ${ stream.name } @ ${ stream.trendingViews } views --%>

<a href="${ stream.viewURI }">
	<span style="display: block;" class="thumbnail stream-thumbnail">
		<c:choose>
			<c:when test="${ fn:length(stream.coverURL) > 0 }">
				<img src="${  stream.coverURL }=s230">
			</c:when>
			<c:otherwise>
				<div class="jumbotron">
<!-- 					<span class="text-center glyphicon glyphicon-picture"></span> -->
					<p class="text-center no-cover-image">
						<em>no cover available</em>
					</p>
					
				</div>
			</c:otherwise>
		</c:choose>
		<c:if test=""></c:if>
		
		<div class="caption">
			<h4 class="list-group-item-heading">
				<strong>
					${ stream.name }
				</strong>
				<BR> 
				<small>
					by <strong><em>${ stream.ownerName }</em></strong>
				</small>
				
			</h4>
			
			<c:choose>
				<c:when test="${ trending }">
					<%-- TODO: hardcoded value of trending window! --%>
					<p class="list-group-item-text">
						<strong>${ stream.trendingViews }</strong>
						views in the last hour
					</p>
				</c:when>
				<c:otherwise>
					<div>
						${ stream.numberOfMedia } images
						&ndash; ${ stream.views } view${ stream.views > 1 ? 's' : '' }
					</div>
					<div>
						<small><em>
							Last updated: ${ stream.lastNewMedia } 
						</em></small>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</span>
</a>
