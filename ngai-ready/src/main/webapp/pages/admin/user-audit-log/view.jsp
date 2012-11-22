<%@ include file="/page-includes/commons.inc.jsp"%>

<s:layout-render name="/page-layouts/minimum.jsp">
	 <s:layout-component name="title">
		<h2>View</h2>
	</s:layout-component>
	<s:layout-component name="content">
		
		<div class="span-3"><s:label for="username">Username: </s:label></div>
		<div class="span-11 last">
			<s:link beanclass="com.onem.mcrm.admin.action.UserAction" event="view">
				<s:param name="id" value="${model.userId}"></s:param>
				&nbsp;${model.username}
			</s:link>
		</div>
		
		<div class="span-3"><label for="actionUri">Action URI: </label></div>
		<div class="span-11 last">&nbsp;${model.actionUri}</div>
		
		<div class="span-3"><label for="eventName">Event Name: </label></div>
		<div class="span-11 last">&nbsp;${model.eventName}</div>
		
		<div class="span-3"><label for="executedAt">Executed At: </label></div>
		<div class="span-11 last">&nbsp;${model.executedAt}</div>
		
		<div class="span-3"><label for="forbiddenAction">Is Forbidden Action: </label></div>
		<div class="span-11 last">&nbsp;${model.forbiddenAction}</div>
		
		<div class="span-workarea last">
			<a class="button negative" href="#" onclick="window.close();">
				<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Close
			</a>
		</div>
	</s:layout-component>
	
</s:layout-render>