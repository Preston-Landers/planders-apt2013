<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="func" uri="/WEB-INF/tlds/functions.tld" %>
<t:connexus>

<jsp:attribute name="head">	
	<%-- put some attributes in the HEAD tag for Facebook likes  --%>
	<c:if test="${ viewingStream ne null }">
		<%-- Facebook / OpenGraph properties for this stream --%>
		<meta property="og:url"             content="${ viewingStream.absoluteViewURI }" /> 
		<meta property="og:title"           content="${ fn:escapeXml(viewingStream.name) }" /> 
		<meta property="og:image"           content="${ viewingStream.coverURL }" />
		<meta property="og:description"     
				content="${ fn:escapeXml(viewingStream.name) } is a ${productName } photo stream created by ${ fn:escapeXml(viewingStream.ownerName) }" />
		<meta property="og:type"            content="article"/> 
		<meta property="fb:app_id"          content="194168720763795"/>
		
		<script>
			var USE_FB = true;	
		</script>			
		
	</c:if>
</jsp:attribute>

<jsp:attribute name="tail">
	<script src="/js/jquery.colorbox.js"></script>
	<script>
		$(document).ready(function() {
			// Activate image viewing galleries
			$('a.gallery').colorbox({ 
				 opacity:0.5,
				 rel: 'group1',
				 photo: true,
	//			 transition: "fade",
				 speed: 450,
				 current: "", // disable the "image X of Y"
			});
		});		
	</script>
	
</jsp:attribute>

<jsp:body xmlns:fb="http://www.facebook.com/2008/fbml">
<c:choose>
<%-- VIEWING A SINGLE STREAM   style="background: url(${ viewingStream.coverURL });" --%>
	<c:when test="${ viewingStream ne null }">
		<div class="stream-cover-bg" >
		<div class="page-header stream-page-header" >
		
			<%-- should get rid of table here --%>
			<table width=100%>
				<tr>
					<td>
						<h1>${fn:escapeXml(viewingStream.name )}</h1>
					</td>
					<td style="vertical-align: middle; text-align: right;">
						<div class="social-buttons" style="float: right;">
							<fb:like href="${ viewingStream.absoluteViewURI }" layout="standard"
											show-faces="true" send="true" width="400" action="like"
											colorscheme="light"></fb:like>
										
						</div>										
					</td>
				</tr>
			</table>
		

			<table width="100%">
				<TR>
					<TD>
						<small><em>currently viewing stream of</em></small> 
							<strong>${ fn:escapeXml(viewingStreamUser.realName) }</strong> 
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
										<a title="${fn:escapeXml(fn:escapeXml(media.comments)) }" class='group1 gallery' href='${media.mediaServingURL}'> 
	    	  							<img class="thumbnail" src="${media.thumbURL}">
										</a>


										<%-- INFO TOOLTIP --%>
										<div class="thumb-tb" style="float: left" rel="popover" data-toggle="popover" data-placement="bottom" 
											data-html="true" data-trigger="hover" data-content="${ fn:escapeXml(media.thumbnailDescription) }">
											<button type="button" onClick="return false;" class="btn btn-xs btn-info" >
												<span class="glyphicon glyphicon-info-sign"></span>
											</button>
										</div>
										
        <%-- Allow delete if it's my image --%>
        <c:if test="${media.uploader == cuser.key}">
			
				
				<%-- SET COVER IMAGE --%>
				<div class="thumb-tb" style="float: left" >
				
				<form action="/view" method="post" role="form" style="display: inline">
					<input type="hidden" name="v" value="${ viewingStream.objectURI }" />
					<input type="hidden" name="m" value="${ media.id }" />
					<span style="float: left; margin-left: 10px" rel="popover" data-toggle="popover" data-placement="bottom" 
						data-html="true" data-trigger="hover" data-content="<h5>Set as Cover Image For Stream</h5>">
						<button name="setCover" value="go" type="submit" class="btn btn-xs btn-success" >
							<span class="glyphicon glyphicon-pushpin"></span>
						</button>
					</span>
				</form>
				</div>


				<%-- DELETE OPTION --%>
				<div class="thumb-tb" style="float:right">
				<form action="/view" method="post" role="form" style="display: inline">
					<input type="hidden" name="v" value="${ viewingStream.objectURI }" />
					<input type="hidden" name="delete" value="${ media.id }" />
					<span rel="tooltip" title="Permanently delete this file">
						<button type="submit" class="btn btn-xs btn-danger" >
							<span class="glyphicon glyphicon-remove-trash"></span>&nbsp;Delete
						</button>
					</span>
				</form>
				</div>
				
		</c:if>
										<div style="clear: both">
										</div> 
										
							    </div>
							    
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
	</div> <%-- stream cover (curently unused) --%>

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
