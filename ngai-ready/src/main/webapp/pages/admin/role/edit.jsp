<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="title">
		<h2><s:label for="role">Role</s:label></h2>
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
					<s:label for="roleName">Role Name: </s:label>
					<s:text name="model.roleName" />
				</p>
				<p>
					<s:label for="description">Description: </s:label>
					<s:textarea name="model.description" />
				</p>
			</fieldset>
			</div>
			<%@include file="/page-includes/edit-controls.inc.jsp" %>
		</s:form>
	</s:layout-component>
</s:layout-render>