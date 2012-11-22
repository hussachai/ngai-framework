<%@ include file="/page-includes/commons.inc.jsp"%>


<s:layout-render name="/page-layouts/minimum.jsp">
	<s:layout-component name="html$head">
		<link rel="stylesheet" href="${ctx }/css/tablesorter/themes/blue/style.css" type="text/css"></link>
		<link rel="stylesheet" href="${ctx }/css/tablesorter/jquery.tablesorter.pager.css" type="text/css"></link>
		<script type="text/javascript" src="${ctx}/js/jquery.tablesorter-2.0.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.tablesorter.pager-2.0.js"></script>
		<script type="text/javascript" src="${ctx}/js/picnet.jquery.tablefilter.js"></script>
		<script type="text/javascript"> 
			$(function() {
				$('#detailTable').tableFilter();
				$('#detailTable')
				.tablesorter({widthFixed: true, widgets: ['zebra']})
				.tablesorterPager({container: $("#pager")}); 
			});
		</script>
	</s:layout-component>
	<s:layout-component name="title">
		<h2>${actionBean.title } Attributes Manager</h2>
	</s:layout-component>
	<s:layout-component name="content">
		<div class="span-18 last">
			<strong>Total size</strong>
			<fmt:formatNumber value="${actionBean.totalAttributesSize}" pattern="###,###"/> Bytes 
			( <fmt:formatNumber value="${actionBean.totalAttributesSize/1024}" pattern="###,###.00"/> Kbytes )
		</div>
		<div class="span-18 last">
			<table id="detailTable" cellspacing="1" class="tablesorter"> 
			<thead> 
				<tr> 
					<th>Name</th> 
					<th>Type</th> 
					<th width="80px">Size(bytes)</th> 
					<th width="80px">Action</th> 
				</tr>
			</thead> 
			<tfoot> 
				<tr> 
					<th>Name</th> 
					<th>Type</th> 
					<th>Size(bytes)</th> 
					<th>Action</th>
				</tr> 
			</tfoot> 
			<tbody> 
				<c:forEach items="${actionBean.attribInfoList}" var="info">
					<tr> 
						<td><span title="${info.name }">${str:abbreviate(info.name,30) }</span></td> 
						<td>${info.type }</td> 
						<td>${info.size }</td> 
						<td>-</td> 
					</tr> 
				</c:forEach>
			</tbody>
			</table>
		</div>
		<div class="span-18 last">
			<div id="pager" class="pager"> 
				<form> 
					<img src="${ctx }/css/tablesorter/icons/first.png" class="first"/> 
					<img src="${ctx }/css/tablesorter/icons/prev.png" class="prev"/> 
					<input type="text" class="pagedisplay"/> 
					<img src="${ctx }/css/tablesorter/icons/next.png" class="next"/> 
					<img src="${ctx }/css/tablesorter/icons/last.png" class="last"/> 
					<select class="pagesize"> 
						<option selected="selected"  value="10">10</option> 
						<option value="20">20</option> 
						<option value="30">30</option> 
						<option  value="40">40</option> 
					</select> 
				</form> 
			</div>
		</div>
	</s:layout-component>
	
</s:layout-render>