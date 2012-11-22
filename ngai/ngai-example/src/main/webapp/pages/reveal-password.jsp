<%@ include file="commons.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<s:layout-render name="/pages/layout.jsp">
	<s:layout-component name="content">
		<h2>Password Revealer</h2>
		<p>
			Are you ready to hack this stupid system. Just enter any username that you think
			it's existing. This form will show you the password of those guy.
		</p>
		<br/>
		<c:if test="${not empty actionBean.password}">
			The password of ${actionBean.username} is <strong>${actionBean.password}</strong>
		</c:if>
		<s:form name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<br/>
			<label>Username: </label>
			<s:text class="textinput" name="username"></s:text>
			<br/>
			<br/>
			<div style="margin-left: 10px;">
				<s:submit class="buttonSubmit" name="revealPassword" value="Reveal"></s:submit>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>