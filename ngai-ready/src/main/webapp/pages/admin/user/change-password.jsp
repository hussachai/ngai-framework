
<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/minimum.jsp">
	
	<s:layout-component name="html$head$script">
		<c:if test="${actionBean.success}">
			window.close();
		</c:if>
	</s:layout-component>
	<s:layout-component name="title">
		<h2>Change Password</h2>
	</s:layout-component>
    <s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}">
			<s:hidden name="username"></s:hidden>
			<fieldset>
				<p>
					<s:label for="username">Username: </s:label>
					<span>${actionBean.username }</span>
				</p>
				<p>
					<s:label for="oldPassword">Old Password: </s:label>
					<s:password name="oldPassword" repopulate="true"/>
				</p>
				<p>
					<s:label for="newPassword">New Password: </s:label>
					<s:password name="newPassword" repopulate="true"/>
				</p>
				<p>
					<s:label for="confirmPassword">Confirm Password: </s:label>
					<s:password name="confirmPassword" />
				</p>
			</fieldset>
			<a class="button positive" href="#" onclick="submitAction(document.mainForm,'changePassword');">
				<img src="${ctx }/images/page_save.png" alt="Change"> Change
			</a>
			<a class="button negative" href="#" onclick="window.close();">
				<img src="${ctx }/images/arrow_undo.png" alt="Close"> Close
			</a>
		</s:form>
    </s:layout-component>
	
</s:layout-render>