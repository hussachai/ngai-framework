<%@ include file="/page-includes/commons.inc.jsp" %>


<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="html$head">
		<%@ include file="head.inc.jsp" %>
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="resourceBundle">Resource Bundle</s:label></h2>
		<h3>
			<c:if test="${empty model.id}">New</c:if>
			<c:if test="${not empty model.id}">Edit</c:if>
		</h3>
	</s:layout-component>
    <s:layout-component name="content">
    	<%@ include file="action-bean-dialog.inc.jsp" %>
		<s:form class="cmxform" name="mainForm" beanclass="${actionBeanFQCN}" method="post">
			<s:hidden name="model"></s:hidden>
			<div class="span-workarea last">
			<fieldset>
				<p>
					<s:label for="laungage">Language: </s:label>
					<s:select id="language" name="model.language" value="${empty preferredLanguage?pageContext.request.locale.language :preferredLanguage}">
						<s:options-map map="${languageMap}" label="key" value="value"/>
					</s:select>
				</p>
				<p>
					<s:label for="actionBeanName">ActionBean Name: </s:label>
					<s:text id="actionBeanName" name="model.actionBeanName"></s:text>
					<a id="chooseActionBeanName" href="#">
						<img src="${ctx}/images/zoom.png" />
					</a>
				</p>
				<p>
					<s:label for="key">Key: </s:label>
					<s:text id="key" name="model.key"></s:text>
				</p>
				<p>
					<s:label for="type">Type: </s:label>
					<s:select id="type" name="model.type">
						<s:option value=""></s:option>
						<s:option value="FIELD">FIELD</s:option>
						<s:option value="MESSAGE">MESSAGE</s:option>
					</s:select>
				</p>
				<p>
					<s:label for="value">Value: </s:label>
					<s:textarea id="value" name="model.value"></s:textarea>
				</p>
			</fieldset>
			</div>
			<div class="span-workarea last">
			<%@include file="/page-includes/edit-controls.inc.jsp" %>
			</div>
		</s:form>
	</s:layout-component>
</s:layout-render>