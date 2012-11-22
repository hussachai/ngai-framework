<%@ include file="/page-includes/commons.inc.jsp" %>


<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="title">
		<h2><s:label for="loginFailureLog">Login Failure Log</s:label></h2>
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
					<label for="attemptCount">Attempt Count: </label>
					<s:text id="attemptCount" name="model.attemptCount"></s:text>
				</p>
				<p>
					<label for="lastAttemptAt">Last Attempt At: </label>
					<s:text id="lastAttemptAt" name="model.lastAttemptAt"></s:text>
				</p>
			</fieldset>
			</div>
			<div class="span-workarea last">
			<%@include file="/page-includes/edit-controls.inc.jsp" %>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>