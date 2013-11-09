<%@ tag description="CeeMe Menu Tab item LI" language="java"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mytaglibs.jspf"%>
<jsp:useBean id="Context" scope="request" type="com.appspot.cee_me.CeeMeContext"/>

<div class="thumb-tb" style="float: left;">
    <button type="button" onClick="$('#modal-about').modal({});return false;"
            class="btn btn-sm btn-info">
        <span class="glyphicon glyphicon-info-sign"></span>
        Tools Used
    </button>
</div>

<c:set var="aboutText" scope="page">
    <div style="text-align: center">
        <h4>Preston Landers</h4>
        <H5>Advanced Programming Tools 2013</H5>
        <H6>The University of Texas at Austin</H6>
        <h5>
            <small>
                Department of Computer and <BR>
                Electrical Engineering
            </small>
        </H5>
    </div>
</c:set>

<div class="thumb-tb" style="float: left;  margin-left: 25px"
     data-toggle="popover" data-placement="top" data-html="true"
     data-trigger="hover" data-content=" <c:out value="${aboutText }" /> ">
    <button type="button" onClick="return false;"
            class="btn btn-sm btn-warning">
        <span class="glyphicon glyphicon-fire"></span>
        About This Site
    </button>
</div>


<div id="modal-about" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content modal-about-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><strong>${Context.productName } Is Powered By:</strong></h4>
            </div>
            <div class="modal-body">
                <p>This site was created with the following tools:</p>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="https://developers.google.com/appengine/" target="_blank">Google App Engine</a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gijtu.html" target="_blank">JavaServer Faces & HTML5</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://getbootstrap.com/" target="_blank">Twitter Bootstrap</a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="http://bootswatch.com/" target="_blank">Bootswatch Themes </a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://jquery.com/" target="_blank">jQuery </a>
                    </div>
                    <div class="col-md-6 about-col">
                        <a href="http://tablesorter.com/docs/" target="_blank">Tablesorter plugin for jQuery</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://www.jetbrains.com/idea/" target="_blank">IntelliJ IDEA</a>
                    </div>
                </div>

                <p style="margin-top: 20px">Special thanks to:</p>
                <div class="row">
                    <div class="col-md-6 about-col">
                        <a href="http://users.ece.utexas.edu/~adnan/" target="_blank">Dr. Adnan Aziz</a>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal">Great, thanks!</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->