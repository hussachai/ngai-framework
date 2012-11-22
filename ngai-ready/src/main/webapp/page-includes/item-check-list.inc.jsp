
<c:if test="${not actionBean.minimumLayout}">
	<c:set var="checkAllElem">
		<input type="checkbox" name="chkToggle"
			onclick="ChkBoxUI.toggleCheckedItems(document.mainForm.idList,this);" />
	</c:set>
	<display:column title="${checkAllElem}" media="html" style="width:30px;">
		<input type="checkbox" name="idList" value="${item.id}" />
	</display:column>
</c:if>
