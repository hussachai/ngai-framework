<%@ include file="../commons.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<c:set var="widthStyle" value="width:600px;"></c:set>
<s:layout-render name="/pages/layout.jsp">
	<s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanClass}" method="get">
			<s:hidden name="model.id"></s:hidden>
			<fieldset>
				<legend><div style="${widthStyle}">&nbsp;</div></legend>
				<p>
					<label>Username: </label>
					<s:text name="model.username"></s:text>
					${actionBean.model.username}
				</p>
				<p>
					<label>Password: </label>
					<s:text name="model.password"></s:text>
				</p>
				<p>
					<label>Status: </label>
					<s:radio value="true" name="model.active"></s:radio>Active
					<s:radio value="false" name="model.active"></s:radio>Inactive
				</p>
				<div style="margin-left: 10px;">
					<s:submit name="save" value="Save"></s:submit>
					&nbsp;
					<s:submit name="cancel" value="Cancel"></s:submit>
				</div>
			</fieldset>
		</s:form>
	</s:layout-component>
</s:layout-render>