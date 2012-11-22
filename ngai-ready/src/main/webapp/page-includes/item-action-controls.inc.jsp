

<c:if test="${not actionBean.minimumLayout}">
	<a href="#" title="view" onclick="UI.showDialog(this,{target:'${actionBeanPath}/view?layout=minimum&model=${item.id}'});"><img src="${ctx }/images/eye.png" /></a>
	&nbsp;|&nbsp;
	<s:link title="edit" beanclass="${actionBeanFQCN}" event="edit" >
		<img src="${ctx }/images/page_edit.png" />
		<s:param name="model" value="${item.id}"></s:param>
	</s:link>
	&nbsp;|&nbsp;
	<s:link title="delete" beanclass="${actionBeanFQCN}" event="delete" onclick="if(!confirm('Are you sure?'))return false;">
		<img src="${ctx }/images/page_delete.png" />
		<s:param name="model" value="${item.id}"></s:param>
	</s:link>
</c:if>
<c:if test="${actionBean.minimumLayout}">
	<c:if test="${not actionBean.multipleSelect}">
		<a href="#" title="select" onclick="Form.selectParentItem('${item[actionBean.lookupModelId]}','${item[actionBean.lookupModelName]}','${actionBean.lookupElementId}');">
			<img src="${ctx }/images/accept.png" />
		</a>
	</c:if>
	<c:if test="${actionBean.multipleSelect}">
		<a href="#" title="add" onclick="Form.addParentItem('${item[actionBean.lookupModelId]}','${item[actionBean.lookupModelName]}','${actionBean.lookupElementId}');">
			<img src="${ctx }/images/add.png" />
		</a>
	</c:if>
</c:if>

