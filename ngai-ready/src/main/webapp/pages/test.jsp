<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="html$head">
		<link rel="stylesheet" type="text/css" href="${ctx }/css/jqueryFileTree/jqueryFileTree.css"></link>
		<script type="text/javascript" src="${ctx }/js/jqueryFileTree-1.01.js"></script>
		<script type="text/javascript">
			$(document).ready( function() {
			    $('#container_id').fileTree({ 
				    	root: 'd:\\',
				    	script: '${ctx}/pages/jqueryFileTree.jsp',
				    	multiFolder: true
					}, function(file) {
			        //alert(file);
			    });
			});
		</script>
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="fileManager">File Manager</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
		<div id="container_id"></div>
	</s:layout-component>
	<s:layout-component name="sidebar">&nbsp;</s:layout-component>
</s:layout-render>