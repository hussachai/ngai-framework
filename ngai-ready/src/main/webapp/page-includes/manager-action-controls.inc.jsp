
<c:if test="${not actionBean.minimumLayout}">
<script type="text/javascript">
	$(document).ready(function(){
		$('#hideSearchBtn').click(function(){
			$('#searchPanel').fadeOut(300);
			$.cookie('searchFormVisible','hidden',cookiePath);
		});
		$('#showSearchBtn').click(function(){
			$('#searchPanel').fadeIn(300);
			$.cookie('searchFormVisible',null,cookiePath);
		});
		if($.cookie('searchFormVisible')=='hidden'){
			$('#searchPanel').hide();
		}

		$('#exportDialog').dialog( {
			bgiframe : true,
			autoOpen : false,
			height : 350,
			width: 450,
			modal : true,
			buttons : {
				'Close' : function() {
					$(this).dialog('close');
				},
				'Export' : function() {
					var searchParams = $('#mainForm').serialize();
					var exportParams = $('#exportForm').serialize();
					var actionUrl = '${actionBeanPath}?'+exportParams+'&'+searchParams;
					window.open(actionUrl, '_blank');
					//$('#exportForm').submit();
					//$(this).dialog('close');
				}
			}
		});
		$('#exportBtn').click( function() {
			$('#exportDialog').dialog('open');
		});
	});
</script>
<div id="exportDialog" title="Export Options">
	<form id="exportForm" class="cmxform" action="${actionBeanPath }" target="_blank">
		<input type="hidden" name="export" />
		<fieldset>
			<p>
				<s:label for="fileName">File Name: </s:label>
				<input id="fileName" type="text" name="fileName" value="data.csv" />
			</p>
			<p>
				<s:label for="escapeCsv">Escape CSV</s:label>
				<input id="escapeCsv" type="checkbox" name="escapeCsv" 
					value="true" checked="checked"/>
			</p>
			<p>
				<s:label for="columnSeparator">Column Separator</s:label>
				<input id="columnSeparator" type="text" name="columnSeparator" value="\t"/>
			</p>
			<p>
				<s:label for="fileFormat">File Format</s:label>
				<select id="fileFormat" name="fileFormat">
					<option value="WIN" selected="selected">WINDOWS</option>
					<option value="UNIX">UNIX</option>
					<option value="MAC">MAC</option>
				</select>
			</p>
			<p>
				<s:label for="fileEncoding">File Encoding</s:label>
				<select id="fileEncoding" name="fileEncoding" >
					<option value="UTF-8">UTF-8</option>
					<option value="TIS-620">TIS-620</option>
					<option value="x-windows-874" selected="selected">x-windows-874</option>
					<option value="US-ASCII">US-ASCII</option>
				</select>
			</p>
			
		</fieldset>
	</form>
</div>
<div class="fg-toolbar ui-widget-header ui-corner-all ui-helper-clearfix">
	<div class="fg-buttonset fg-buttonset-multi">
		<a href="#" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" 
			title="Back" onclick="onBack();">
			<span class="ui-icon  ui-icon-arrowreturnthick-1-w"></span> Back
		</a>
	</div>
	<div class="fg-buttonset fg-buttonset-multi">
		<button class="fg-button ui-state-default ui-corner-left" onclick="onCreate();">New</button>
		<button class="fg-button ui-state-default ui-corner-right" onclick="onEdit();">Edit</button>
		<button class="fg-button ui-state-default" onclick="onBulkDelete();">Delete</button>
	</div>
	<div class="fg-buttonset fg-buttonset-multi">
		<button id="exportBtn" class="fg-button ui-state-default ui-corner-all">Export</button>
	</div>
	<div class="fg-buttonset fg-buttonset-single ui-helper-clearfix">
		<button id="hideSearchBtn" class="fg-button ui-state-default ui-state-active ui-priority-primary ui-corner-left"
			onclick="$('#searchPanel').fadeOut(300);">
			Hide
		</button>
		<button id="showSearchBtn" class="fg-button ui-state-default ui-priority-primary ui-corner-right"
			onclick="$('#searchPanel').fadeIn(300);">
			Show
		</button>
	</div>
</div>
<hr class="space"/>
</c:if>
