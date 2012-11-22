<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	<s:layout-component name="html$head">
		<%@ include file="head.inc.jsp" %>
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="resourceBundle">Resource Bundle</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<%@ include file="action-bean-dialog.inc.jsp" %>
    	<%@include file="/page-includes/manager-action-controls.inc.jsp" %>
    	
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="layout" value="${param.layout}"></s:hidden>
			<div id="searchPanel" class="span-workarea last">
			<fieldset>
				<legend>Search</legend>
				<p>
					<s:label for="laungage">Language: </s:label>
					<s:select id="language" name="model.language" value="${empty preferredLanguage?pageContext.request.locale.language :preferredLanguage}">
						<s:options-map map="${languageMap}" label="key" value="value"/>
					</s:select>
				</p>
				<p>
					<s:label for="actionBeanName">ActionBean Name: </s:label>
					<s:text id="actionBeanName" name="model.actionBeanName"></s:text>
					<a id="chooseActionBeanName" href="#">
						<img src="${ctx}/images/zoom.png" />
					</a>
				</p>
				<p>
					<s:label for="key">Key: </s:label>
					<s:text id="key" name="model.key"></s:text>
				</p>
				<p>
					<s:label for="type">Type: </s:label>
					<s:select id="type" name="model.type">
						<s:option value="">ALL</s:option>
						<s:option value="FIELD" selected="true">FIELD</s:option>
						<s:option value="MESSAGE">MESSAGE</s:option>
					</s:select>
				</p>
				<p>
					<s:label for="value">Value: </s:label>
					<s:text id="value" name="model.value"></s:text>
				</p>
			</fieldset>
			</div>
			
			<%@include file="/page-includes/main-action-controls.inc.jsp" %>
			<div class="span-workarea last">
				<display:table name="${actionBean.paginatedList}" uid="item"
					requestURI="${actionBeanPath}" sort="external" class="displaytag" >
					<%@include file="/page-includes/item-check-list.inc.jsp" %>
					<display:column property="id" title="ID" titleKey="id" sortable="true" />
					<display:column property="actionBeanName" title="ActionBean Name" titleKey="actionBeanName" sortable="true" />
					<display:column property="key" title="Key" titleKey="local.key" sortable="true" />
					<display:column property="type" title="Type" titleKey="local.type" sortable="true" />
					<display:column property="value" title="Value" titleKey="local.value"/>
					<display:column title="Action" titleKey="action">
						<%@include file="/page-includes/item-action-controls.inc.jsp" %>
					</display:column>
					<display:setProperty name="paging.banner.placement" value="bottom" />
				</display:table>
			</div>
		</s:form>
		<br/>
    </s:layout-component>
</s:layout-render>
	