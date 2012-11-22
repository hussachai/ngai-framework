<%@ include file="/page-includes/commons.inc.jsp" %>


<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="html$head">
		<%@ include file="head.inc.jsp" %>
		<script type="text/javascript">
			function onBack() {
				window.location = '${ctx}/action/admin/user';
			}
		</script>
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="userPermission">User Permission</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<div class="span-workarea last">
    		<%@include file="/page-includes/manager-action-controls.inc.jsp" %>
    	</div>
    	<%@ include file="action-uri-dialog.inc.jsp" %>
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<s:hidden name="model.userId"></s:hidden>
			<div id="searchPanel" class="span-16 last">
			<fieldset>
				<legend>Search</legend>
				<p>
					<s:label for="username">Username</s:label>
					<s:text id="username" style="border:none;" name="username" readonly="true"/>
				</p>
				<p>
					<s:label for="actionUri">Action URI: </s:label>
					<s:text id="actionUri" name="model.actionUri" />
					<a id="chooseUri" href="#">
						<img src="${ctx}/images/zoom.png" />
					</a>
				</p>
				<p>
					<s:label for="eventNames">Event Name: </s:label>
					<s:text id="eventNames" name="searchEventName"/>
				</p>
			</fieldset>
			</div>
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			
			<div class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" titleKey="id" sortable="true" />
					<display:column property="actionUri" titleKey="actionUri" sortable="true" />
					<display:column titleKey="eventName" title="Event Names">
						${str:abbreviate(fn:join(item.eventNames,", "),40)}
					</display:column>
					<display:column titleKey="Action">
						<a href="#" onclick="UI.showDialog(this,{target:'${actionBeanPath}/view?layout=minimum&id=${item.id}&model.userId=${model.userId }&username=${actionBean.username}'});">view</a>
						&nbsp;|&nbsp;
						<s:link beanclass="${actionBeanClass}" event="edit" >edit
							<s:param name="id" value="${item.id}"></s:param>
							<s:param name="model.userId" value="${model.userId}"></s:param>
							<s:param name="username" value="${actionBean.username}"></s:param>
						</s:link>
						&nbsp;|&nbsp;
						<s:link beanclass="${actionBeanClass}" event="delete" onclick="if(!confirm('Are you sure?'))return false;">delete
							<s:param name="id" value="${item.id}"></s:param>
							<s:param name="model.userId" value="${model.userId}"></s:param>
							<s:param name="username" value="${actionBean.username}"></s:param>
						</s:link>
					</display:column>
					<display:setProperty name="paging.banner.placement" value="bottom" />
				</display:table>
			</div>
		</s:form>
		<br/>
    </s:layout-component>
</s:layout-render>
	