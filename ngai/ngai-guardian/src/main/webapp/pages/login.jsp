<%@ include file="/page-includes/commons.jsp" %>


<s:layout-render name="/page-layouts/standard.jsp">
	
    <s:layout-component name="content$center">
    	<h2>Login</h2>
		<hr/>
		
		<s:form class="cmxform" name="mainForm" 
			beanclass="com.siberhus.ngai.example.action.AuthenticationAction" method="post">
			<s:hidden name="targetAction" value="${params.targetAction}"></s:hidden>
			<fieldset class="login">
				<p>
					<label for="username">Username: </label>
					<s:text name="username"></s:text>
				</p>
				<p>
					<label for="password">Password: </label>
					<s:text name="password"></s:text>
				</p>
			</fieldset>
			<s:submit name="login" value="Login"></s:submit>
		</s:form>
	</s:layout-component>
</s:layout-render>

