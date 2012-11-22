
<div id="dialog" title="Available Action URI">
	<p id="validateTips">
		<s:label for="pleaseSelectOne">Please select one of these.</s:label>
	</p>
	<form class="cmxform">
		<fieldset>
			<s:label for="actionUri">Action URI: </s:label> 
			<select style="width: 300px" id="selectedActionUri">
				<c:forEach items="${actionUriSet}" var="actionUri">
					<option value="${actionUri}">${actionUri}</option>
				</c:forEach>
			</select>
		</fieldset>
	</form>
</div>