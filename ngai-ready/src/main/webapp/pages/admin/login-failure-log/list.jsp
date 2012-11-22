<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="title">
		<h2><s:label for="loginFailureLog">Login Failure Log</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<div class="span-workarea last">
    		<%@include file="/page-includes/manager-action-controls.inc.jsp" %>
    	</div>
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<div id="searchPanel" class="span-16 last">
			<fieldset>
				<legend>Search</legend>
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
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			</div>
			<div id="resultPanel" class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" sortable="true" />
					<display:column property="ipAddress" title="IP Address" sortable="true" />
					<display:column property="attemptCount" title="Attempt Count" sortable="true" />
					<display:column property="lastAttemptAt" title="Last Attempt At" sortable="true" />
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
	