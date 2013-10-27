<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:connexus>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="jquery-ui/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
        <style>
            .ui-autocomplete-loading {
                background: white url('images/ui-anim_basic_16x16.gif') right center no-repeat;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="tail">
        <script src="jquery-ui/js/jquery-ui-1.10.3.custom.min.js"></script>
        <script>
            $(function() {

                var acCache = {};  // autocomplete results cache

                $( "#q" ).autocomplete({
                    source: function( request, response ) {
                        var term = request.term;
                        if ( term in acCache ) {
                            response( acCache[ term ] );
                            return;
                        }

                        $.getJSON( "search", request, function( data, status, xhr ) {
                            acCache[ term ] = data;
                            response( data );
                        });
                    },
                    minLength: 1
                });
            });
        </script>
    </jsp:attribute>

    <jsp:body>
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
		<form id="searchform" role="form" action="/search" method="get" style="display: inline">
			<div class="row"><div class="col-md-12">		
					<label for="q">Search:</label>		
					<input style="margin-left: 5px" class="form-control" id="q" type="text" name="q"
							value="${fn:escapeXml(q)}" ${ showSearchResults ? '': 'autofocus' } placeholder="Type search terms here" />
			</div></div>
			<div class="row">
                <div class="col-md-6">
                    <button style="margin-top: 20px" name="search" value="go" class="btn btn-success" type="submit">
                        <span class="glyphicon glyphicon-search"></span>&nbsp;
                        Search
                    </button>
			    </div>
                <div class="col-md-6">
                    <button
                            class="btn btn-warning"
                            type="submit"
                            id="rebuild"
                            name="rebuild"
                            value="rebuild"
                            style="float: right; margin-top: 20px"
                            rel="tooltip"
                            title="Rebuild the text index used for auto-completion. Automatically rebuilds at the top of every hour."
                            onclick="$('#searchform').attr('method', 'post');return true;"
                            >
                        <span class="glyphicon glyphicon-wrench"></span>&nbsp;
                        Rebuild Completion Index
                    </button>
                </div>
            </div>
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
	
</jsp:body></t:connexus>
