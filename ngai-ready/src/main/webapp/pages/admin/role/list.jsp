<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="title">
		<h2><s:label for="role">Role</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<div class="span-workarea last">
    		<%@include file="/page-includes/manager-action-controls.inc.jsp" %>
    	</div>
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="get">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<div id="searchPanel" class="span-workarea last">
			<fieldset>
				<legend>Search</legend>
				<p>
					<s:label for="roleName">Role Name: </s:label>
					<s:text id="roleName" name="model.roleName"/>
				</p>
				<p>
					<s:label for="description">Description: </s:label>
					<s:text id="description" name="model.description" />
				</p>
			</fieldset>
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			</div>
			<div id="resultPanel" class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" titleKey="id" sortable="true" />
					<display:column property="roleName" title="Role Name" titleKey="roleName" sortable="true" />
					<display:column title="Description" titleKey="description">
						${str:abbreviate(item.description,40)}
					</display:column>
					<display:column style="width: 120px" title="Action" titleKey="action">
						<%@include file="/page-includes/item-action-controls.inc.jsp" %>
						<c:if test="${not actionBean.minimumLayout}">
							&nbsp;|&nbsp;
							<s:link href="/action/admin/rolePermission">
								<img src="${ctx }/images/page_key.png"/>
								<s:param name="roleName" value="${item.roleName}"></s:param>
								<s:param name="model.roleId" value="${item.id}"></s:param>
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
	