<%@ include file="commons.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<s:layout-render name="/pages/layout.jsp">
	<s:layout-component name="content">
		<h2>Number Counter</h2>
		<p>
			
		</p>
		<br/>
		Counter value : <strong> ${actionBean.counter} </strong>
		<s:form name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<div style="margin-left: 10px;">
				<s:submit class="buttonSubmit" name="incrementCounter" value="Increment"></s:submit>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>