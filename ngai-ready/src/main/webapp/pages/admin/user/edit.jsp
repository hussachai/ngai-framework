<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	 <s:layout-component name="title">
		<h2><s:label for="user">User</s:label></h2>
		<h3>
			<c:if test="${empty model.id}">New</c:if>
			<c:if test="${not empty model.id}">Edit</c:if>
		</h3>
	</s:layout-component>
    <s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<div class="span-workarea last col2">
			<fieldset>
				<s:hidden name="model"></s:hidden>
				<p>
					<s:label for="username">Username: </s:label>
					<s:text style="border:none;" name="model.username" readonly="true" />
					<c:if test="${empty actionBean.model.id}">
						<s:label for="password">Password: </s:label>
						<s:password style="border:none;" id="password" name="model.password" repopulate="true" readonly="true"/>
					</c:if>
					<c:if test="${not empty actionBean.model.id}">
						<a href="#" onclick="UI.showDialog(this,{width:'680px',target:'${ctx}/action/admin/changeUserPassword?username=${model.username}'});">
							Change Password
						</a>
					</c:if>
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
					<s:label for="description">Description: </s:label>
					<s:textarea id="description" name="model.description"></s:textarea>
				</p>
				
				<p>
					<s:label for="roles">Roles: </s:label>
					<s:select id="roles" class="selectAll" name="model.roleSet" multiple="multiple" size="5">
						<s:options-collection collection="${model.roleSet}" 
							label="roleName" value="id"/>
					</s:select>
					<span style="vertical-align: top;">
						<img src="${ctx }/images/delete.png" onclick="SelectUI.removeSelected('roles')"/>
						<img src="${ctx }/images/add.png" onclick="UI.showDialog(this,{width:'680px',target:'${ctx}/action/admin/role?layout=minimum&elemId=roles&modelName=roleName&multiSel=true'});"/>
					</span>
				</p>
			</fieldset>
			</div>
			<div class="span-workarea last">
				<%@include file="/page-includes/edit-controls.inc.jsp" %>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>