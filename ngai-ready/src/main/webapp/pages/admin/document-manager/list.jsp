<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="html$head">
		<script type="text/javascript">
			$(document).ready(function(){
				$('#leftTreeMenu').dynatree({
					persist: true,
					onLazyRead: function(dtnode){
						dtnode.appendAjax(
							{url: "/getChildrenAsJson",
								data: {key: dtnode.data.key,
								mode: "funnyMode"
							}
						});		
					},
					children: [
						{title: "Simple node", key: "1" },
						{title: "Lazy folder", isFolder: true, isLazy: true, key: "2"},
						{title: "Lazy document", isLazy: true, key: "3"}
					]
				});
			});
		</script>
	</s:layout-component>
	<s:layout-component name="sidebar">
		
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="fileManager">File Manager</s:label></h2>
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
	