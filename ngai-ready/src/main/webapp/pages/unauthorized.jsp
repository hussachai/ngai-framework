<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/standard.jsp">

	<s:layout-component name="title">
		<h1 style="color: red">Unauthorized Request</h1>
	</s:layout-component>
	<s:layout-component name="content">
		
		You are not authorized to access the requested page.
		<ol>
			<li>Back to the previous page</li>
			<li>Sign in as different user.</li>
			<li>Report this issue to administrator</li>
		</ol>
	</s:layout-component>
	
</s:layout-render>