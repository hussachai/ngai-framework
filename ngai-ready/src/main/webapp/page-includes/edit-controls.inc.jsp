
<a class="button positive" href="#" onclick="submitAction(document.mainForm,'save');">
	<img src="${ctx }/images/page_save.png" alt="Save"> Save
</a>
<c:if test="${not actionBean.minimumLayout}">
		<a class="button negative" href="#" onclick="submitAction(document.mainForm,'cancel');">
			<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Cancel
		</a>
</c:if>
<c:if test="${actionBean.minimumLayout}">
		<a class="button negative" href="#" onclick="window.close();">
			<img src="${ctx }/images/arrow_undo.png" alt="Cancel"> Close
		</a>
</c:if>


