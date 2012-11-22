<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/standard.jsp">
	
	<s:layout-component name="title">
		<h2><s:label class="title" for="login">Login</s:label></h2>
	</s:layout-component>
	<s:layout-component name="html$head">
		<c:if test="${logout}">
			<script type="text/javascript">
				$(document).ready(function(){
					$.cookie('DSNnfaD863bas0aarnDE',null,cookiePath);
					$.cookie('453DNnssd4n3hd23n931',null,cookiePath);
				});
			</script>
		</c:if>
	</s:layout-component>
    <s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" 
			action="/action/authen" method="post">
			<s:hidden name="targetAction" value="${params.targetAction}"></s:hidden>
			<fieldset class="rounded">
				<p>
					<s:label for="username">Username: </s:label>
					<s:text id="username" name="username" value="root"></s:text>
				</p>
				<p>
					<s:label for="password">Password: </s:label>
					<s:password id="password" name="password" repopulate="true" value="password"></s:password>
				</p>
				<p>
					<s:label for="laungage">Language: </s:label>
					<s:select id="language" name="language" value="${pageContext.request.locale.language}">
						<s:options-map map="${languageMap}" label="key" value="value"/>
					</s:select>
				</p>
				<p>
					<s:label for="rememberMe">Remember Me?: </s:label>
					<s:checkbox id="rememberMe" name="rememberMe"></s:checkbox>
				</p>
			</fieldset>
			<button type="submit" class="button positive" onclick="submitAction(document.mainForm,'login');">
				<img src="${ctx }/images/lock_open.png" alt="Login"> Login
			</button>
		</s:form>
		<hr class="space" />
	</s:layout-component>
	<s:layout-component name="sidebar">
		<div class="browserPanel">
			<strong>Tested with</strong> <br/>
			<img src="${ctx }/images/browsers/chrome.png" />&nbsp; 
			<a href="http://www.google.com/chrome" target="_blank">Google Chrome</a> (Most efficient)
			<br/>
			<img src="${ctx }/images/browsers/mfirefox.png" />&nbsp; 
			<a href="http://www.mozilla.com/firefox" target="_blank">Mozilla Firefox</a> (Most compatible)
			<br/>
			<img src="${ctx }/images/browsers/safari.png" />&nbsp; 
			<a href="http://www.apple.com/safari/" target="_blank">Apple Safari</a><br/>
			<img src="${ctx }/images/browsers/opera.png" />&nbsp; 
			<a href="http://www.opera.com" target="_blank">Opera</a><br/>
			<img src="${ctx }/images/browsers/ie.png" />&nbsp; Internet Explorer<br/>
			<br/>
			<strong>We recommended:</strong> Google Chrome<br/>
			See browser usage statistic <a href="http://www.w3counter.com/globalstats.php" target="_blank">here</a>
		</div>
	</s:layout-component>
	
</s:layout-render>

