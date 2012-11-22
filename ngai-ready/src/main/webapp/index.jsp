<%@ include file="page-includes/commons.inc.jsp" %>
<html>
<body>
	<head>
		<style type="text/css">
			.warningPanel{
				border-style: dashed; 
				border-width:2px; 
				border-color: #448F2C; 
				background-color: #DFFFCF;
				padding: 10px;
				margin-top: 50px;
			}
		</style>
	</head>
	<noscript>
		<div class="warningPanel" align="center">
			Your must enable java script to use this web application.
			<a href="http://www.google.com/support/bin/answer.py?answer=23852"
			 target="_blank">
				Turn it on.
			</a>
		</div>
	</noscript>
	<script type="text/javascript">
		
		var cookieEnabled=(navigator.cookieEnabled)? true : false;
		
		//if not IE4+ nor NS6+
		if (typeof navigator.cookieEnabled=="undefined" && !cookieEnabled){ 
			document.cookie="testcookie";
			cookieEnabled=(document.cookie.indexOf("testcookie")!=-1)? true : false;
		}
		
		//if (cookieEnabled) //if cookies are enabled on client's browser
		//do whatever
		if(cookieEnabled){
			window.location.href='${pageContext.request.contextPath}/action/authen';
		}
	</script>
	<div class="warningPanel" align="center">
		It seems like you've disabled cookie feature in your browser.<br/>
		You may need to enable it to work with this system efficiently.<br/>
		<a href="${pageContext.request.contextPath}/action/authen">Continue</a>
		
	</div>
</body>
</html>
