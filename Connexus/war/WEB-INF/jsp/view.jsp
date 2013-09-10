<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="func" uri="/WEB-INF/tlds/functions.tld" %>
<t:connexus><jsp:body>
<c:choose>
<%-- VIEWING A SINGLE STREAM   style="background: url(${ viewingStream.coverURL });" --%>
	<c:when test="${ viewingStream ne null }">
		<div class="stream-cover-bg" >
		<div class="page-header stream-page-header" >
			<h1>${ viewingStream.name }</h1>
			<table width="100%">
				<TR>
					<TD>
						<small><em>currently viewing stream of</em></small> 
							<strong>${ viewingStreamUser.realName }</strong> 
					</TD>
					<TD style="text-align: right">					
					<small><em>${ numberOfMedia } images total</em></small> &mdash;
					<small><em>Last updated: ${ viewingStream.lastNewMedia} </em></small> &mdash;
					<small><em> ${ viewingStream.views } views</em></small>
					</TD>
				</TR>
			</table>
		</div>
		<div class="container">
			<div class="jumbotron" style="text-align: center;">
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
        <%-- TODO: still need to show info tooltip even if not your image --%>
        <c:if test="${media.uploader == cuser.key}">
			<div class="modal-footer" style="margin-top: 0px;">
			
				<%-- INFO TOOLTIP --%>
				<span style="float: left" rel="popover" data-toggle="popover" data-placement="bottom" 
					data-html="true" data-trigger="hover" data-content="${ media.thumbnailDescription }">
					<button type="button" onClick="return false;" class="btn btn-info" >
						<span class="glyphicon glyphicon-info-sign"></span>
					</button>
				</span>
				
				<%-- SET COVER IMAGE --%>
				<form action="/view" method="post" role="form" style="display: inline">
					<input type="hidden" name="v" value="${ viewingStream.objectURI }" />
					<input type="hidden" name="m" value="${ media.id }" />
					<span style="float: left; margin-left: 10px" rel="popover" data-toggle="popover" data-placement="bottom" 
						data-html="true" data-trigger="hover" data-content="<h5>Set as Cover Image For Stream</h5>">
						<button name="setCover" value="go" type="submit" class="btn btn-success" >
							<span class="glyphicon glyphicon-pushpin"></span>
						</button>
					</span>
				</form>

				<%-- DELETE OPTION --%>
				<form action="/view" method="post" role="form" style="display: inline">
					<input type="hidden" name="v" value="${ viewingStream.objectURI }" />
					<input type="hidden" name="delete" value="${ media.id }" />
					<span rel="tooltip" title="Permanently delete this file">
						<button type="submit" class="btn btn-danger" >
							<span class="glyphicon glyphicon-remove-trash"></span>&nbsp;Delete
						</button>
					</span>
				</form>
				
				<%-- CLOSE BUTTON --%>
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
						<a href="${viewingStream.viewURI }&offset=${newerOffset}&limit=${newerLimit}">&larr; Newer</a>
					</li>
				</c:if>

				<c:if test="${ showOlderButton }">

					<%-- only show older when they exist --%>
					<li class="next">
						<a href="${viewingStream.viewURI }&offset=${olderOffset}&limit=${olderLimit}">Older &rarr;</a>
					</li>

				</c:if>
				
			</ul>		
		</c:if>

	</div>
	<%-- Show the stream's tags --%>
	<div class="container">
		<div class="well tags-well" style="padding: 10px;">
			<p class="text-center" style="margin: 2px;">
				<c:choose>
					<c:when test="${fn:length(viewingStream.tags) > 0 }">
						<c:forEach var="tag" items="${ viewingStream.tags }">
							<c:if test="${ fn:length(tag) > 0 }">
								<a href="${func:getSearchURI(tag)}"><span style="margin: 0px 5px;" class="stream-tag">
									<span class="badge" style="padding: 8px">
										<c:out value="${tag}"></c:out>
									</span>						
								</span></a>					
							</c:if>
						</c:forEach>				
					</c:when>
					<c:otherwise>
						<em>(No tags set)</em>
					</c:otherwise>
				</c:choose>
			</p>
		</div>
		</div>
	</div>
		
	<%-- If this is your stream, show the upload widget. Otherwise show the subscribe widget. --%>
	<c:choose>
		<c:when test="${ viewingStreamUser == cuser }">
			<t:uploadMedia stream="${ viewingStream.id }" streamUser="${ viewingStreamUser.id }"></t:uploadMedia>
		</c:when>
		<c:otherwise>
			<t:subscribe stream="${ viewingStream.id }" streamUser="${ viewingStreamUser.id }"></t:subscribe>
		</c:otherwise>
	</c:choose>
	</c:when>
	
	<%-- BROWSE AVAILABLE STREAMS --%>
	<c:otherwise>
		<div class="page-header">
			<h3>Available Streams</h3>
			<small><em>Subscribe to your favorite streams!</em></small>
		</div>
		<div id="browseStreamPanel" class="panel panel-default">
			<div class="panel-heading">
				<H3 class="panel-title">
					Browse available streams &ndash;
					<small><em>Most recently updated first</em></small>	
				</H3>
				
			</div>
			<div class="panel-body">
				<t:streamList streamList="${ allStreamsList }"></t:streamList>
			</div>
		
		</div>
		
	</c:otherwise>
</c:choose>
</jsp:body>
</t:connexus>
