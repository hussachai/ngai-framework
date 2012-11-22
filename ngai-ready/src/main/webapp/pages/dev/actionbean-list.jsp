<%@ include file="/page-includes/commons.inc.jsp"%>


<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="title">
		<h2>ActionBean List</h2>
	</s:layout-component>
	<s:layout-component name="content">
		<div class="span-workarea last">
			<h3>Links</h3>
			<ul style="margin-left: 30px">
			<c:forEach var="actionBeanPath" items="${actionBeanPathMap}">
				<li>
					<a href="${actionBeanPath.value}">${actionBeanPath.key}</a> 
				</li>
			</c:forEach>
			</ul>
		</div>
	</s:layout-component>
	
</s:layout-render>