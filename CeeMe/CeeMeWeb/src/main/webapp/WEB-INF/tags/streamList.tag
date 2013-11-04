<%@ tag description="CeeMe Stream" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<%@ attribute name="streamList" required="true"  type="java.lang.Object" %>
<%@ attribute name="trending" required="false"  %>
<%@ attribute name="emptyText" required="false" %>

<%-- A reusable representation of a list of streams --%>

<c:if test="${ emptyText == null }">
	<c:set var="emptyText" value="I can't find any photo streams yet!" ></c:set>
</c:if>

<!-- TODO: special display for trending? -->


<c:choose><c:when test="${ not empty streamList }">

	<c:forEach var='rowList' items='${ func:partition(streamList, 4) }'>
		<div class="row" style="margin-bottom: 25px">
			<c:forEach var='stream' items='${ rowList }'>
				<div class="col-md-3">
					<t:stream stream="${ stream }"></t:stream>
				</div>
			</c:forEach>
	
		</div>
	</c:forEach>


<!-- 	<div class="container list-group"> -->
<%-- 		<c:forEach var='stream' items='${ streamList }'> --%>
<%-- 			<t:stream stream="${ stream }"></t:stream> --%>
<%-- 		</c:forEach> --%>
<!-- 	</div> -->
</c:when><c:otherwise>
<%-- might want this outside this tag... --%>
	<div class="well text-center">
		<H3>${ emptyText }</H3> 
	</div>
	<div class="text-center">
		<a href="/create" >
			<button type="button" class="btn btn-primary btn-lg">Create a Stream Now!</button>
		</a>
	</div>
						
</c:otherwise>
</c:choose>

