<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="html$head">
		<%@ include file="head.inc.jsp" %>
		<%@ include file="head-edit.inc.jsp" %>
		
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="rolePermission">Role Permission</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<%@ include file="action-uri-dialog.inc.jsp" %>
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="model"></s:hidden>
			<s:hidden name="model.roleId"></s:hidden>
			<div class="span-workarea last">
			<fieldset>
				<p>
					<s:label for="roleName">Role Name: </s:label>
					<s:text id="roleName" style="border:none; " name="roleName" readonly="true"/>
				</p>
				<p>
					<s:label for="uriRegex">URI Regular Expression: </s:label>
					<s:select id="uriRegex" name="sqlRegex">
						<s:option value="true">SQL</s:option>
						<s:option value="false">JAVA</s:option>
					</s:select>
				</p>
				<p>
					<s:label for="actionUri">Action URI: </s:label>
					<s:text id="actionUri" name="model.actionUri" />
					<a id="chooseUri" href="#">
						<img src="${ctx}/images/zoom.png" />
					</a>
				</p>
				<div>
					<s:label for="eventNames">Event Names: </s:label>
					<input id="checkAllEvents" type="checkbox" name="eventNames" value="ALL"/>&nbsp;ALL
					<ul id="eventNames" style="padding-left: 145px"></ul>
				</div>
				
			</fieldset>
			</div>
			<%@include file="/page-includes/edit-controls.inc.jsp" %>
		</s:form>
	</s:layout-component>
</s:layout-render>