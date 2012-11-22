<%@ include file="/page-includes/commons.inc.jsp" %>

<c:set var="widthStyle" value="width:600px;"></c:set>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="title">
		<h2><s:label for="userAuditLog">User Audit Log</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">	
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<div id="searchPanel" class="span-workarea last">
			<fieldset>
				<legend>Search</legend>
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
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			<div id="resultPanel" class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" sortable="true" />
					<display:column property="username" title="Username" sortable="true" />
					<display:column property="actionBeanName" title="ActionBean Name" sortable="true" />
					<display:column property="eventName" title="Event Name" sortable="true" />
					<display:column property="executedAt" title="Executed At" sortable="true" />
					<display:column property="forbiddenAction" title="Forbidden?" sortable="true" />
					<display:column title="Action">
						<%@include file="/page-includes/item-action-controls.inc.jsp" %>
					</display:column>
					<display:setProperty name="paging.banner.placement" value="bottom" />
				</display:table>
			</div>
		</s:form>
		<br/>
    </s:layout-component>
</s:layout-render>
	