<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<t:connexus><jsp:body>
<c:choose>
	<c:when test="${ viewingStream ne null }">
		<div class="page-header" style="margin-top: 5px">
			<h1>${ viewingStream.name }</h1>
			<table width="100%">
				<TR>
					<TD>
					<small><em>currently viewing</em></small>
					</TD>
					<TD style="text-align: right">
					<small><em>${ numberOfMedia } total images</em></small>
					</TD>
				</TR>
			</table>
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
    	  							<img src="${media.mediaServingURL}=s300">
<%--     	  								alt="${fn:escapeXML(media.comments)}"> --%>
<%-- XXX TODO: why isn't escapeXML working here? --%>
									<span style="display: block;">
										<h6>
    	   								 ${media.comments }
       									</h6>
									</span>
    							</a>
						    </div>
							    
<!-- Modal image popup -->
  <div class="modal fade thumb-viewer" id="thumb-viewer-${media.id}" tabindex="-1" role="dialog" aria-labelledby="thumb-title-${media.id}" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-body">
        	<div class="row">
        		<div class="col-md-12">
<!--           <button type="button" class="btn btn-default" data-dismiss="modal"> -->
			<a data-dismiss="modal">
			<%-- TODO: add =s0 but only in production?! doesn't work on test server --%>
          		<img class="img-thumbnail"
	          	 	alt="${media.comments}" 
          			src="${media.mediaServingURL}">
       		</a>          
<!--           	Close -->
<!--           </button> -->
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
       				<H4 style="padding-top: 10px">${media.comments}</H4>
       			</div>
       		</div>
        </div>
        <%-- Allow delete if it's my image --%>
        <c:if test="${media.uploader == cuser.key}">
			<div class="modal-footer" style="margin-top: 0px;">
				<span style="float: left" rel="popover" data-toggle="popover" data-placement="left" 
					data-html="true" data-trigger="hover" data-content="${ media.thumbnailDescription }">
					<button type="button" onClick="return false;" class="btn btn-info" >
						<span class="glyphicon glyphicon-info-sign"></span>
					</button>
				</span>


				<form action="/view" method="post" role="form" style="display: inline">
					<input type="hidden" name="v" value="${ viewingStream.id }" />
					<input type="hidden" name="delete" value="${ media.id }" />
					<span rel="tooltip" title="Permanently delete this file">
						<button type="submit" class="btn btn-danger" >
							<span class="glyphicon glyphicon-remove-trash"></span>&nbsp;Delete
						</button>
					</span>
				</form>
				<button style="margin-left: 20px" type="button" class="btn btn-primary" data-dismiss="modal">
					<span class="close"></span>&nbsp;Close
				</button>

			</div>
		</c:if>
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
				<c:if test="${ showNewerButton }">
					<li class="previous">
						<a href="/view?v=${viewingStream.id }&offset=${newerOffset}&limit=${newerLimit}">&larr; Newer</a>
					</li>
				</c:if>

				<c:if test="${ showOlderButton }">

					<%-- only show older when they exist --%>
					<li class="next">
						<a href="/view?v=${viewingStream.id }&offset=${olderOffset}&limit=${olderLimit}">Older &rarr;</a>
					</li>

				</c:if>
				
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
