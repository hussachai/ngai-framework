<%@ include file="../commons.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<c:set var="widthStyle" value="width:600px;"></c:set>
<s:layout-render name="/pages/layout.jsp">
	<s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanClass}" method="get">
			<fieldset>
				<legend><div style="${widthStyle }">&nbsp;</div></legend>
				<p>
					<label>Username: </label>
					<s:text name="model.username"></s:text>
				</p>
				<p>
					<label>Status:</label>
					<s:radio value="" name="model.active"></s:radio>All
					<s:radio value="true" name="model.active"></s:radio>Active
					<s:radio value="false" name="model.active"></s:radio>Inactive
				</p>
				<div style="margin-left: 10px;">
					<s:submit name="search" value="Search"></s:submit>
				</div>
			</fieldset>
			
			<div id="resultPanel">
	            <div>
	            	<input type="button" value="New" onclick="onNewClick();"/> &nbsp;
	            	<input type="button" value="Edit" onclick="onEditClick();"/> &nbsp;
	            	<input type="button" value="Delete" onclick="onDeleteClick();"/> &nbsp;
	            </div>
	            <br/>
				<display:table name="${actionBean.paginatedList}" uid="list"
					requestURI="${actionBeanPath}" sort="external"
					class="displaytag" style="${widthStyle }">
					<c:set var="checkAllElem">
						<input type="checkbox" name="chkToggle"
							onclick="ChkBox.toggleCheckedItems(this,document.mainForm.idList);" />
					</c:set>
					<display:column title="${checkAllElem}" media="html" style="width:30px;">
						<input type="checkbox" name="idList" value="${list.id}" />
					</display:column>
					<display:column property="id" title="Id" sortable="true" media="all"/>
					<display:column property="username" title="Username" sortable="true" media="all"/>
					<display:column property="password" title="Password" sortable="true" />
					<display:setProperty name="paging.banner.placement" value="bottom" />
				</display:table>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>