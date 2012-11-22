<%@ include file="/page-includes/commons.jsp" %>

<s:layout-render name="/page-layouts/standard.jsp">
	<s:layout-component name="html$head">
		<link rel="stylesheet" href="${ctx }/css/ui.multiselect/ui.multiselect.css" type="text/css" media="screen, projection">
		<style type="text/css">
			#languages {
				width: 570px;
				height: 200px;
			}
		</style>
		<script type="text/javascript" src="${ctx }/js/jquery.localisation.min.js"></script>
		<script type="text/javascript" src="${ctx }/js/jquery.scrollTo-1.4.1.min.js"></script>
		<script type="text/javascript" src="${ctx }/js/ui.multiselect-0.3.js"></script>
		<script type="text/javascript">
			$(function(){
				$.localise('ui-multiselect', {/*language: 'en',*/ path: '${ctx}/js/locale/'});
				$(".multiselect").multiselect();
			});
		</script>
		
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="login">Login</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
		<s:form class="cmxform" name="mainForm" action="/action/forward" method="post">
			<fieldset class="rounded">
				<p>
					<s:label for="textbox">Textbox: </s:label>
					<s:text id="textbox" name="textbox"></s:text>
				</p>
				<p>
					<s:label for="languages">Languages: </s:label>
					<s:select id="languages" class="multiselect" multiple="multiple" name="languages" value="${empty preferredLanguage?pageContext.request.locale.language:preferredLanguage}">
						<s:options-map map="${languageMap}" label="key" value="value"/>
					</s:select>
				</p>
			</fieldset>
			
			<button type="submit" class="button positive" onclick="#">Submit</button>
		</s:form>
		
	</s:layout-component>
</s:layout-render>

