<%@ include file="commons.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<s:layout-render name="/pages/layout.jsp">
	<s:layout-component name="content">
		<h2>Login</h2>
		<br/>
		<s:form name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			Username: <s:text name="username"></s:text><br/>
			Password: <s:password name="password"></s:password><br/>
			Remember Me? : <s:checkbox name="rememberMe"></s:checkbox><br/>
			<div style="margin-left: 10px;">
				<s:submit class="buttonSubmit" name="login" value="Login"></s:submit>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>