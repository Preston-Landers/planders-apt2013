<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<t:connexus><jsp:body>
<c:choose>
	<c:when test="${ viewingStream ne null }">
		<div class="page-header" style="margin-top: 5px">
			<h1>${ viewingStream.name }</h1>
			<small><em>currently viewing</em></small>
		</div>
		<div class="container">
			<div class="jumbotron" style="text-align: center">
				<c:choose>
					<c:when test="${ (mediaList == null) or ( fn:length(mediaList) == 0 )}">
						<h1>Stream is empty</h1> <small><em>Upload something below!</em></small>			
					</c:when>
				<c:otherwise>
					<div class="row">
  						<div class="col-sm-6 col-md-3">
  							<c:forEach items="${ mediaList }" var="media">
	    						<a href="#" class="thumbnail">
    	  							<img src="${media.mediaServingURL}">
<%--     	  								alt="${fn:escapeXML(media.comments)}"> --%>
<%-- XXX TODO: why isn't escapeXML working here? --%>
    							</a>
  							</c:forEach>
  						</div>
					</div>		
				</c:otherwise>
			</c:choose>
		</div>
		<ul class="pager">
			<li class="next"><a href="#">Newer &rarr;</a></li>
			<li class="previous"><a href="#">&larr; Older</a></li>
		</ul>

	</div>
		
	<%-- TODO: only show if this is your stream? --%>
	<t:uploadMedia stream="${ viewingStream.id }"></t:uploadMedia>
		
		
	</c:when>
	<c:otherwise>
		<div class="page-header">
			<h1>You have not selected a stream to view.</h1>
			<small><em style="font-variant: small-caps">TODO: Implement Me</em></small>
		</div>
	 	(TODO!)
	</c:otherwise>
</c:choose>
</jsp:body>
</t:connexus>
