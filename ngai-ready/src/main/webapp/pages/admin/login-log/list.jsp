<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="title">
		<h2><s:label for="loginLog">Login Log</s:label></h2>
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
					<s:label for="username">Username: </s:label>
					<s:text id="username" name="model.username"></s:text>
				</p>
				<p>
					<s:label for="loginAt">Login At: </s:label>
					<s:text id="loginAt" class="datePicker" name="model.loginAt"></s:text>
				</p>
				<p>
					<s:label for="logoutAt">Logout At: </s:label>
					<s:text id="logoutAt" class="datePicker" name="model.logoutAt"></s:text>
				</p>
				<p>
					<s:label for="ipAddress">IP Address: </s:label>
					<s:text id="ipAddress" name="model.ipAddress"></s:text>
				</p>
			</fieldset>
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			</div>
			<div id="resultPanel" class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" sortable="true" />
					<display:column style="width: 70px" property="username" title="Username" titleKey="username" sortable="true" />
					<display:column title="Login At" titleKey="loginAt" sortable="true" sortProperty="loginAt">
						${df:formatDate(item.loginAt)}
					</display:column>
					<display:column title="Logout At" titleKey="logoutAt" sortable="true" sortProperty="logoutAt">
						${df:formatDate(item.logoutAt)}
					</display:column>
					<display:column property="ipAddress" title="IP Address" titleKey="ipAddress" sortable="true" />
					<display:column style="width: 50px" title="Action" titleKey="action">
						<a href="#" onclick="UI.showDialog(this,{target:'${actionBeanPath}/view?layout=minimum&model=${item.id}'});"><img src="${ctx }/images/eye.png" /></a>
						&nbsp;|&nbsp;
						<s:link beanclass="${actionBeanFQCN}" event="delete" onclick="if(!confirm('Are you sure?'))return false;">
							<img src="${ctx }/images/page_delete.png" />
							<s:param name="model" value="${item.id}"></s:param>
						</s:link>
					</display:column>
					<display:setProperty name="paging.banner.placement" value="bottom" />
				</display:table>
			</div>
		</s:form>
		<br/>
    </s:layout-component>
</s:layout-render>
	