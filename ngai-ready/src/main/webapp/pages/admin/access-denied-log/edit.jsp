<%@ include file="/page-includes/commons.inc.jsp" %>


<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="title">
		<s:label class="title" for="accessDeniedLog">Access Denied Log</s:label>
		<h3>
			<c:if test="${empty model.id}">New</c:if>
			<c:if test="${not empty model.id}">Edit</c:if>
		</h3>
	</s:layout-component>
    <s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="model"></s:hidden>
			<div class="span-workarea last">
			<fieldset>
				<p>
					<label for="ipAddress">IP Address: </label>
					<s:text id="ipAddress" name="model.ipAddress"></s:text>
				</p>
				<p>
					<label for="requestURI">Request URI: </label>
					<s:text id="requestURI" name="model.requestURI"></s:text>
				</p>
				<p>
					<label for="userAgent">User Agent: </label>
					<s:text id="userAgent" name="model.userAgent"></s:text>
				</p>
				<p>
					<label for="retryCount">Retry Count ( &gt;= ) : </label>
					<s:text id="retryCount" name="model.retryCount"></s:text>
				</p>
			</fieldset>
			</div>
			<div class="span-workarea last">
			<%@include file="/page-includes/edit-controls.inc.jsp" %>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>