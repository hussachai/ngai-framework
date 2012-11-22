<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="html$head$script">
		function onCreate(){
			submitAction(document.mainForm,'checkUser');
		}
	</s:layout-component>
	<s:layout-component name="title">
		<img src="${ctx}/images/admin/ico-users.gif" /><s:label class="title" for="user">User</s:label>
	</s:layout-component>
    <s:layout-component name="content">
    	<div class="span-workarea last">
    		<%@include file="/page-includes/manager-action-controls.inc.jsp" %>
    	</div>
		<s:form id="mainForm" class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<div id="searchPanel" class="span-workarea last col2">
			<fieldset>
				<legend>Search</legend>
				<p>
					<s:label for="username">Username: </s:label>
					<s:text id="username" name="model.username" />
				</p>
				<p>
					<s:label for="firstName">First Name: </s:label>
					<s:text id="firstName" name="model.firstName" />
					<s:label for="lastName">Last Name: </s:label>
					<s:text id="lastName" name="model.lastName" />
				</p>
				<p>
					<s:label for="email">Email: </s:label>
					<s:text id="email" name="model.email" />
					<s:label for="contactNumber">Contact Number: </s:label>
					<s:text id="contactNumber" name="model.contactNumber" />
				</p>
				<p>
					<s:label for="alias">Alias: </s:label>
					<s:text id="alias" name="model.alias" />
				</p>
				<p>
					<s:label for="status">Status: </s:label>
					<s:select id="status" name="model.active">
						<s:option value="">All</s:option>
						<s:option value="true">Active</s:option>
						<s:option value="false">Inactive</s:option>
					</s:select>
				</p>
			</fieldset>
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			</div>
			<div id="resultPanel" class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag"  >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" titleKey="id" sortable="true" />
					<display:column property="username" title="Username" titleKey="username" sortable="true" />
					<display:column title="Full Name" titleKey="fullName">
						${item.firstName } &nbsp; ${item.lastName }
					</display:column>
					<display:column property="roleSet" title="Roles" titleKey="roles" />
					<display:column style="width: 120px" title="Action" titleKey="action">
						<%@include file="/page-includes/item-action-controls.inc.jsp" %>
						<c:if test="${not actionBean.minimumLayout}">
							&nbsp;|&nbsp;
							<s:link href="/action/admin/userPermission" title="permission">
								<img src="${ctx }/images/page_key.png"/>
								<s:param name="username" value="${item.username}"></s:param>
								<s:param name="model.userId" value="${item.id}"></s:param>
							</s:link>
						</c:if>
					</display:column>
					<display:setProperty name="paging.banner.placement" value="bottom" />
				</display:table>
			</div>
		</s:form>
		<br/>
    </s:layout-component>
</s:layout-render>
	