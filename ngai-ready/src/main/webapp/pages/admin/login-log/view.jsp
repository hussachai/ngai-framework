<%@ include file="/page-includes/commons.inc.jsp"%>

<s:layout-render name="/page-layouts/minimum.jsp">
	 <s:layout-component name="title">
		<h2>View</h2>
	</s:layout-component>
	<s:layout-component name="content">
		<div class="span-3"><s:label for="username">Username: </s:label></div>
		<div class="span-11 last">&nbsp;${model.username}</div>
		
		<div class="span-3"><s:label for="loginAt">Login At: </s:label></div>
		<div class="span-11 last">&nbsp;${model.loginAt}</div>
		
		<div class="span-3"><s:label for="logoutAt">Logout At: </s:label></div>
		<div class="span-11 last">&nbsp;${model.logoutAt}</div>
		
		<div class="span-3"><s:label for="ipAddress">IP Address: </s:label></div>
		<div class="span-11 last">&nbsp;${model.ipAddress}</div>
		
		<div class="span-workarea last">
			<a class="button negative" href="#" onclick="window.close();">
				<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Close
			</a>
		</div>
	</s:layout-component>
	
</s:layout-render>
