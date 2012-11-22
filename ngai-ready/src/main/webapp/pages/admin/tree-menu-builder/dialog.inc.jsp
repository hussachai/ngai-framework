
<script type="text/javascript">
$(document).ready(function(){
	$('#deleteRoleButton').click(function(){
		SelectUI.removeSelected('roles')
	});
	$('#addRoleButton').click(function(){
		var target = '${ctx}/action/admin/role?layout=minimum&elemId=roles&modelName=roleName&multiSel=true';
		UI.showDialog(this,{width:'680px',target:target});
	});
});
</script>
<div id="errorDetail" style="background: white; padding-left: 20px; color:red;"></div>
<s:form id="mainForm" style="text-align: left;" class="cmxform" name="mainForm" action="/action/admin/treeMenuBuilder" method="post">
	<s:hidden name="layout" value="${param.layout}"></s:hidden>
	<s:hidden id="model" name="model" value="model.id"></s:hidden>
	<s:hidden id="selectedNodeId" name="selectedNodeId" />
	<s:hidden id="position" name="model.position" value="0"></s:hidden>
	<div class="col2">
		<fieldset>
			<p>
				<s:label for="firstLevel">Status: </s:label>
				<s:select id="status" name="model.status" >
					<s:option value="ENABLED">ENABLED</s:option>
					<s:option value="DISABLED">DISABLED</s:option>
					<s:option value="HIDDEN">HIDDEN</s:option>
				</s:select>
			</p>
			<p id="parent">
				<s:label for="parentMenu">Parent Menu: </s:label>
				<s:hidden id="parentMenu" name="model.parent"></s:hidden>
				<s:text id="parentMenuDisplay" style="border:none;" name="parentDisplay" readonly="true"></s:text>
			</p>
			<p>
				<s:label for="roles">Roles: </s:label>
				<s:select id="roles" class="selectAll" name="model.roleSet" multiple="multiple" size="5">
					<s:options-collection collection="${model.roleSet}" 
						label="roleName" value="id"/>
				</s:select>
				<span style="vertical-align: top;">
					<img id="deleteRoleButton" src="${ctx}/images/delete.png" />
					<img id="addRoleButton" src="${ctx}/images/add.png"  />
				</span>
			</p>
			<p>
				<s:label for="label">Label: </s:label>
				<s:text id="label" name="model.label"></s:text>
				<s:label for="labelKey">Label Key: </s:label>
				<s:text id="labelKey" name="model.labelKey"></s:text>
			</p>
			<p>
				<s:label for="linkUrl">Link URL: </s:label>
				<s:text id="linkUrl" style="width: 400px;" name="model.linkUrl"></s:text>
			</p>
			<p>
				<s:label for="linkType">Link Type: </s:label>
				<s:select id="linkType" name="model.linkType">
					<s:option value="HREF">HREF</s:option>
					<s:option value="JAVASCRIPT">JAVASCRIPT</s:option>
				</s:select>
				<s:label for="linkMethod">Link Method: </s:label>
				<s:select id="linkMethod" name="model.linkMethod">
					<s:option value="GET">GET</s:option>
					<s:option value="POST">POST</s:option>
				</s:select>
			</p>
			<p>
				<s:label for="description">Description: </s:label>
				<s:textarea id="description" name="model.description"></s:textarea>
			</p>
			<p>
				<s:label for="descriptionKey">Description Key: </s:label>
				<s:text id="descriptionKey" name="model.descriptionKey"></s:text>
			</p>
			
		</fieldset>
	</div>
</s:form>