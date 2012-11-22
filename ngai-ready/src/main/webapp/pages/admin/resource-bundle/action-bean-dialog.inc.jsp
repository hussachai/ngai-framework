
<div id="dialog" title="Available Action Bean Name">
	<p id="validateTips">
		<s:label for="pleaseSelectOne">Please select one of these.</s:label>
	</p>
	<form class="cmxform">
		<fieldset>
			<s:label for="actionBeanName">ActionBean Name: </s:label> 
			<select style="width:300px" id="selectedActionBeanName">
				<c:forEach items="${actionBeanNameSet}" var="actionBeanName">
					<option value="${actionBeanName}">${actionBeanName}</option>
				</c:forEach>
			</select>
		</fieldset>
	</form>
</div>