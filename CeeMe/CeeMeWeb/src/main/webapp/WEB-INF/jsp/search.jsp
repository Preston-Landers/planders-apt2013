<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:ceeme><jsp:body>
<div class="page-header">
	<h3>Search Streams</h3>
	<small><em>Search within stream tags</em></small>
</div>

<div id="searchPanel" class="panel panel-default">
	<div class="panel-heading">
		<H3 class="panel-title">
			Search  
		</H3>
	
	</div>
	<div class="panel-body">
		<form role="form" action="/search" method="get" style="display: inline">
			<div class="row"><div class="col-md-12">		
					<label for="q">Search:</label>		
					<input style="margin-left: 5px" class="form-control" id="q" type="text" name="q"
							value="${fn:escapeXml(q)}" ${ showSearchResults ? '': 'autofocus' } placeholder="Type search terms here" />
			</div></div>
			<div class="row"><div class="col-md-12">
				<button style="margin-top: 20px" name="search" value="go" class="btn btn-success" type="submit">
					Search
				</button>		
			</div></div>
		</form>
	</div>
</div>

<c:choose><c:when test="${ not showSearchResults }">
<%--  (No search yet) --%>
</c:when><c:otherwise>

<div id="searchResultsPanel" class="panel panel-default">
	<div class="panel-heading">
		<H3 class="panel-title">
			${ fn:length(searchResultsList) } search results for  
			<strong> ${ fn:escapeXml(q) } </strong>
<!-- 			<small><em>&ndash; Most recently updated first</em></small>	 -->
		</H3>
		
	</div>
	<div class="panel-body">
		<t:streamList streamList="${searchResultsList }"
							emptyText="I can't find any streams that match your search terms."></t:streamList>
	</div>

</div>
</c:otherwise></c:choose>
	
</jsp:body></t:ceeme>
