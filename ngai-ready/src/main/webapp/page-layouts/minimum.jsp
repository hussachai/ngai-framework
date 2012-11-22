<%@ include file="/page-includes/commons.inc.jsp" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<s:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <title>Ngai Ready</title>
			<%@include file="/page-layouts/resources.inc.jsp" %>
			<%@include file="/page-layouts/preset.inc.jsp" %>
			<script type="text/javascript">
				$(document).ready(function(){
					$('.span-workarea').addClass('span-16');
				});
				<s:layout-component name="html$head$script"/>
			</script>
            <s:layout-component name="html$head"/>
        </head>
        <body id="public">
            <div class="container">
            	<div class="span-workarea prepend-1 last">
	            	<s:errors />
		            <s:messages/>
		        </div>
            	<div class="span-workarea prepend-1 last">
            		<s:layout-component name="title"/>
            		<hr/>
            	</div>
            	<div class="span-workarea prepend-1 last">
            		<s:layout-component name="content"/>
	            </div>
			</div>
        </body>
    </html>
</s:layout-definition>
