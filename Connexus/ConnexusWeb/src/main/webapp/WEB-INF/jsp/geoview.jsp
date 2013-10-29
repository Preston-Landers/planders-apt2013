<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="func" uri="/WEB-INF/tlds/functions.tld" %>
<t:connexus>

<jsp:attribute name="head">	
	<%-- put some attributes in the HEAD tag for Facebook likes  --%>
    <c:if test="${ viewingStream ne null }">
        <%-- Facebook / OpenGraph properties for this stream --%>
        <meta property="og:url" content="${ viewingStream.absoluteGeoViewURI }"/>
        <meta property="og:title" content="${ fn:escapeXml(viewingStream.name) }"/>
        <meta property="og:image" content="${ viewingStream.coverURL }"/>
        <meta property="og:description"
              content="${ fn:escapeXml(viewingStream.name) } is a ${productName } photo stream created by ${ fn:escapeXml(viewingStream.ownerName) } [Geographic View]"/>
        <meta property="og:type" content="article"/>
        <meta property="fb:app_id" content="${ facebookAppId }"/>

        <script>
            var USE_FB = true;
        </script>

    </c:if>

    <link rel="stylesheet" href="jquery-ui/css/smoothness/jquery-ui-1.10.3.custom.min.css"/>

    <c:if test="${ canDoUpload }">
        <t:uploadMediaStylesheets></t:uploadMediaStylesheets>
    </c:if>

</jsp:attribute>

<jsp:attribute name="tail">
    <script src="jquery-ui/js/jquery-ui-1.10.3.custom.min.js"></script>
    <c:if test="${ canDoUpload }">
        <t:uploadMediaScripts></t:uploadMediaScripts>
    </c:if>

    <%-- Geo-goodies --%>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <script type="text/javascript" src="js/jquery.ui.map/markerclusterer.min.js"></script>
    <script type="text/javascript" src="js/jquery.ui.map/min/jquery.ui.map.min.js"></script>
    <script>
        // We need to bind the map with the "init" event otherwise bounds will be null
        $('#map_canvas').gmap({'zoom': 2, 'disableDefaultUI': true}).bind('init', function (evt, map) {
            var bounds = map.getBounds();
            var southWest = bounds.getSouthWest();
            var northEast = bounds.getNorthEast();
            var lngSpan = northEast.lng() - southWest.lng();
            var latSpan = northEast.lat() - southWest.lat();
            for (var i = 0; i < 1000; i++) {
                var lat = southWest.lat() + latSpan * Math.random();
                var lng = southWest.lng() + lngSpan * Math.random();
                $('#map_canvas').gmap('addMarker', {
                    'position': new google.maps.LatLng(lat, lng)
                }).click(function () {
                            $('#map_canvas').gmap('openInfoWindow', { content: 'Hello world!' }, this);
                        });
            }
            $('#map_canvas').gmap('set', 'MarkerClusterer', new MarkerClusterer(map, $(this).gmap('get', 'markers')));
            // To call methods in MarkerClusterer simply call
            // $('#map_canvas').gmap('get', 'MarkerClusterer').callingSomeMethod();
        });
    </script>

    <%--
        Set up the date range slider. The value should be understood as a (day) offset from '365 days ago'.
    --%>
    <script>
        $(function () {
            function formatDate(dateObj) {
                return $.datepicker.formatDate('MM d, yy', dateObj);
            }

            function setRangeLabels(low, high) {
                var yearAgo = new Date();
                yearAgo.setYear(yearAgo.getFullYear() - 1);
                var lowDate = new Date(yearAgo);
                lowDate.setDate(yearAgo.getDate() + low);
                var highDate = new Date(yearAgo);
                highDate.setDate(yearAgo.getDate() + high);
                var label = formatDate(lowDate) + " to: " + formatDate(highDate);
                $("#geoDateRateDisplay").html(label);
            }

            $("#slider-range").slider({
                range: true,
                min: 0,
                max: 365,  // one year back
                values: [ 0, 365 ],
                slide: function (event, ui) {
                    setRangeLabels(ui.values[ 0 ], ui.values[ 1 ])
                }
            });
            setRangeLabels(
                    $("#slider-range").slider("values", 0),
                    $("#slider-range").slider("values", 1)
            );

        });
    </script>
</jsp:attribute>

<jsp:body>
    <c:choose>
        <%-- GEO-VIEW OF A SINGLE STREAM  --%>
        <c:when test="${ viewingStream ne null }">
            <div class="stream-cover-bg">
                <div class="page-header stream-page-header">

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
                                <small><em>Geographic view of stream</em></small>
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
                    <div id="map-jumbotron" class="jumbotron map-jumbotron" style="text-align: center;">
                            <%-- MAIN GEO-VIEW HERE --%>
                        <div class="panel panel-default map-panel thick-shadow">
                            <div class="panel-heading">
                                <h3 class="panel-title">Geo view of:
                                    <strong>${ fn:escapeXml(viewingStream.name) }</strong>

                                    <div class="pull-right">
                                        <a href="${ viewingStream.viewURI }">
                                            <button
                                                    rel="tooltip" title="Return to the normal view of this stream."
                                                    type="button"
                                                    class="btn btn-primary btn-xs">
                                                Return to Gallery View
                                            </button>
                                        </a>
                                    </div>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div id="map_canvas"></div>
                            </div>
                        </div>
                            <%-- panel-default map-panel --%>

                        <div class="panel panel-date-range-slider thick-shadow jquery-ui">
                            <p>
                                <label for="geoDateRateDisplay">Date range:</label>
                                <span
                                    style="border: 0; color: #f6931f; font-weight: bold;"
                                    id="geoDateRateDisplay"
                                    >
                                </span>
                            </p>

                            <div id="slider-range"></div>

                        </div>
                            <%-- panel-date-range-slider --%>

                    </div>
                        <%-- jumbotron --%>

                    <c:if test="${fn:length(mediaList) > 0 }">
                        <ul class="pager">
                            <c:if test="${ showNewerButton }">
                                <li class="previous">
                                    <a href="${viewingStream.viewURI }&offset=${newerOffset}&limit=${newerLimit}">&larr;
                                        Newer</a>
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
                                            <a href="${func:getSearchURI(tag)}"><span style="margin: 0px 5px;"
                                                                                      class="stream-tag">
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
            <%-- stream cover (curently unused) --%>

            <%-- If this is your stream, show the upload widget. Otherwise show the subscribe widget. --%>
            <c:choose>
                <c:when test="${ canDoUpload }">
                    <t:uploadMedia stream="${ viewingStream.id }"
                                   streamUser="${ viewingStreamUser.id }"></t:uploadMedia>
                </c:when>
                <c:otherwise>
                    <t:subscribe stream="${ viewingStream.id }"
                                 streamUser="${ viewingStreamUser.id }"></t:subscribe>
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
