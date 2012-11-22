<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="title">
		<h2><s:label for="accessDeniedLog">Access Denied Log</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<div class="span-workarea last">
    		<%@include file="/page-includes/manager-action-controls.inc.jsp" %>
    	</div>
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<div id="searchPanel" class="span-workarea last">
			<fieldset>
				<legend>Search</legend>
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
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			</div>
			<div id="resultPanel" class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" sortable="true" />
					<display:column property="ipAddress" title="IP Address" sortable="true" />
					<display:column property="requestURI" title="Request URI" sortable="true" />
					<display:column property="userAgent" title="User Agent" sortable="true" />
					<display:column property="retryCount" title="Retry Count" sortable="true" />
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
	