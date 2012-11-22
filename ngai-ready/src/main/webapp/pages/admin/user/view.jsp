<%@ include file="/page-includes/commons.inc.jsp"%>


<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	 <s:layout-component name="title">
		<h2>View</h2>
	</s:layout-component>
	<s:layout-component name="content">
		<div class="span-3"><s:label for="username">Username: </s:label></div>
		<div class="span-11 last">&nbsp;${model.username}</div>
		
		<div class="span-3"><s:label for="firstName">First Name: </s:label></div>
		<div class="span-4">&nbsp;${model.firstName}</div>
		<div class="span-3"><s:label for="lastName">Last Name: </s:label></div>
		<div class="span-4 last">&nbsp;${model.lastName}</div>
		
		<div class="span-3"><s:label for="email">Email: </s:label></div>
		<div class="span-4">&nbsp;<a href="mailto:${model.email}">${model.email}</a></div>
		<div class="span-3"><s:label for="contactNumber">Contact Number: </s:label></div>
		<div class="span-4 last">&nbsp;${model.contactNumber}</div>
		
		<div class="span-3"><s:label for="alias">Alias: </s:label></div>
		<div class="span-11 last">&nbsp;${model.alias}</div>
		
		<div class="span-3"><s:label for="description">Description: </s:label></div>
		<div class="span-11 last">&nbsp;${model.description}</div>
		
		<div class="span-3"><s:label for="roles">Roles: </s:label></div>
		<div class="span-11 last">
			<ul>
				<c:forEach items="${model.roleSet}" var="role">
					<s:link href="/action/admin/role" event="view">
						<s:param name="model">${role.id}</s:param>
						<s:param name="layout">minimum</s:param>
						<li>${role}</li>
					</s:link>
				</c:forEach>
			</ul>
		</div>
		<div class="span-workarea last">
			<a class="button negative" href="#" onclick="window.close();">
				<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Close
			</a>
		</div>
	</s:layout-component>
	
</s:layout-render>