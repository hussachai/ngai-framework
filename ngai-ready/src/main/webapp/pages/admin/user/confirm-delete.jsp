<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	 <s:layout-component name="title">
		<h2>User Management</h2>
		<h3>
			Delete confirmation [User ID: ${model.id}]
		</h3>
	</s:layout-component>
    <s:layout-component name="content">
    	
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="model" value="${model.id}"></s:hidden>
			
			<div class="span-3">
				<s:label name="action">Action</s:label>
			</div>
			<div class="span-12 last">
				<s:radio value="true" name="wipeUser" /> 
				Wipe - Clean all user data. This action cannot be undone.<br/>
				<s:radio value="false" name="wipeUser" checked="true"/>
				Deactivate - This operation doesn't delete user data. This is safer way.
			</div>
			<hr class="space" />
			<div class="span-workarea last">
				<a class="button positive" href="#" onclick="submitAction(document.mainForm,'confirmDeletion');">
					<img src="${ctx }/images/page_save.png" alt="Confirm"> Confirm
				</a>
				<a class="button negative" href="#" onclick="submitAction(document.mainForm,'index');">
					<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Cancel
				</a>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>