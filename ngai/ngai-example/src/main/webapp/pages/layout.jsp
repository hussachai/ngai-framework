<%@ include file="commons.jsp" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<s:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <title>Ngai Example</title>
            <link rel="stylesheet" type="text/css" href="${ctx}/pages/assets/niceforms-default.css"/>
            
            <script type="text/javascript" src="${ctx}/pages/assets/niceforms.js"></script>
        </head>
        <body>
        	<h1>${actionBean.title}</h1>
        	<h2>${actionBean.detail }</h2>
        	<br/>
        	<hr/>
        	<br/>
			<s:errors/>
			<s:messages/>
			<div id="container">
				<s:layout-component name="content"/>
			</div>
        </body>
    </html>
</s:layout-definition>
