<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/${actionBean.layout}.jsp">
	 <s:layout-component name="title">
		<img src="${ctx}/images/admin/ico-users.gif" /><s:label for="user">User</s:label>
	</s:layout-component>
    <s:layout-component name="content">
    	<script type="text/javascript">
    		$(document).ready(function(){
        		$('#username').blur(function(){
        			ajaxInvoke($('#mainForm'),'ajaxCheckUser',$('#usernameCheckResult'),'imgAccept','imgCancel');
        		});
        	});
    	</script>
    	
    	<div id="error" class="span-workarea last"></div>
		<s:form id="mainForm" class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<fieldset>
				<p>
					<s:label for="username" title="username">Username: </s:label>
					<s:text id="username" name="model.username" />
					<span id="usernameCheckResult"></span>
				</p>
				<p>
					<s:label for="password" title="password">Password: </s:label>
					<s:password id="password" name="model.password" />
				</p>
				<p>
					<s:label for="password2" title="password2">Re-Type Password: </s:label>
					<s:password id="password2" name="password" />
				</p>
			</fieldset>
			<hr class="space" />
			<div class="span-workarea last">
				<a class="button positive" href="#" onclick="submitAction(document.mainForm,'create');">
					<img src="${ctx }/images/page_save.png" alt="Confirm"> Next
				</a>
				<a class="button negative" href="#" onclick="submitAction(document.mainForm,'index');">
					<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Cancel
				</a>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>