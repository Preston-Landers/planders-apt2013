<%@ include file="/WEB-INF/jsp/include.jsp"%>

<t:ceeme><jsp:body>
<div class="container">
	<div class="page-header" style="margin-top: 5px">

		<H1>I'm sorry, but there was a problem.</H1>
		<small>The tiny monkey powering this site has escaped.</small>
	</div>
	<div>
	
<div class="panel-group" id="accordion">
  <div class="panel panel-default">
    <div class="panel-heading">
        <a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion" href="#collapseOne">
	      <h4 class="panel-title">
    	      Internal Error
      		</h4>
        </a>
    </div>
    <div id="collapseOne" class="panel-collapse collapse in">
      <div class="panel-body">
<%--       	<PRE>${ fn:escapeXml(errorMessage) }</PRE> --%>
      	<PRE>${ errorMessage }</PRE>
      </div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading">
     <a class="accordion-toggle" data-toggle="collapse"
									data-parent="#accordion" href="#collapseTwo">
     	 <h4 class="panel-title">
        	  Stack Trace
		 </h4>
      </a>
    </div>
    <div id="collapseTwo" class="panel-collapse collapse">
      <div class="panel-body">
      	<PRE>${ stackTrace }</PRE>
      </div>
    </div>
  </div>
  
</div>
	</div>
</div>
</jsp:body></t:ceeme>
