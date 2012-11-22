
<s:hidden name="multipleSelect" value="${param.multiSel}"></s:hidden>
<s:hidden name="lookupElementId" value="${param.elemId}"></s:hidden>
<s:hidden name="lookupModelId" value="${param.modelId}"></s:hidden>
<s:hidden name="lookupModelName" value="${param.modelName}"></s:hidden>

<div class="span-workarea last">
	<a class="button positive" href="#" onclick="onSearch();">
		<img src="${ctx}/images/page_find.png"> Search
	</a>
	<a class="button positive" href="#" onclick="onClear();">
		<img src="${ctx}/images/page.png"> Clear
	</a>
	<c:if test="${actionBean.minimumLayout }">
		<a class="button negative" href="#" onclick="window.close();">
			<img src="${ctx}/images/page_delete.png">Close
		</a>
	</c:if>
</div>
