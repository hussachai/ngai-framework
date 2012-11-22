<%@ include file="/page-includes/commons.inc.jsp" %>


<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="title">
		<h2><s:label for="userAuditLog">User Audit Log</s:label></h2>
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
					<label for="username">Username: </label>
					<s:text id="username" name="model.username"></s:text>
				</p>
				<p>
					<label for="actionBeanName">ActionBean Name: </label>
					<s:text id="actionBeanName" name="model.actionBeanName"></s:text>
				</p>
				<p>
					<label for="eventName">Event Name: </label>
					<s:text id="eventName" name="model.eventName"></s:text>
				</p>
				<p>
					<label for="executedAt">Executed At : </label>
					<s:text id="executedAt" name="model.executedAt"></s:text>
				</p>
				<p>
					<label for="forbiddenAction">Forbidden Action : </label>
					<s:checkbox id="forbiddenAction" name="model.forbiddenAction"></s:checkbox>
				</p>
			</fieldset>
			</div>
			<%@include file="/page-includes/edit-controls.inc.jsp" %>
		</s:form>
	</s:layout-component>
</s:layout-render>