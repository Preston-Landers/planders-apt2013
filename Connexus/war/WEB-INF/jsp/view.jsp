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
						<c:forEach items="${ mediaList }" var="media">
	  						<div class="col-sm-6 col-md-4">
  								<div id="thumb-div-${media.id}" class="thumbnail">
	    						<a id="thumb-a-${media.id}" data-toggle="modal" href="#thumb-viewer-${media.id}" class="thumbnail">
    	  							<img src="${media.mediaServingURL}">
<%--     	  								alt="${fn:escapeXML(media.comments)}"> --%>
<%-- XXX TODO: why isn't escapeXML working here? --%>
    							</a>
								<div class="caption">
									<h6>
       								 ${media.comments }
       								</h6>
							    </div>
							    </div>
							    
<!-- Modal image popup -->
  <div class="modal fade" id="thumb-viewer-${media.id}" tabindex="-1" role="dialog" aria-labelledby="thumb-title-${media.id}" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-body">
<!--           <button type="button" class="btn btn-default" data-dismiss="modal"> -->
			<a data-dismiss="modal">
			<%-- TODO: add =s0 but only in production?! doesn't work on test server --%>
          	<img class="img-thumbnail" src="${media.mediaServingURL}">
          	</a>          
<!--           	Close -->
<!--           </button> -->
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->
  							    
  							</div>
						</c:forEach>
					</div>		
				</c:otherwise>
			</c:choose>
		</div>
		
		<c:if test="${fn:length(mediaList) > 0 }">
			<ul class="pager">
				<c:if test="${ offset > limit }">
					<li class="previous"><a href="#">&larr; Newer</a></li>
				</c:if>
				<%-- TODO: only show older when they exist --%>
				<li class="next"><a href="#">Older &rarr;</a></li>
			</ul>		
		</c:if>

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
