<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/standard.jsp">
	
	<s:layout-component name="title">
		<h2><s:label for="welcome">Welcome</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<div style="border-style: dashed;border-color: #325F23;width: 400px;border-width: 2px; margin: 100px 0px 0px 150px" align="center">
			Welcome ${loginInfo.firstName } ${loginInfo.lastName }<br/>
			<br/>
			<strong>This page was intentionally left blank for now.</strong>
		</div>
	</s:layout-component>
</s:layout-render>

