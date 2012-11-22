<script type="text/javascript">
	$(document).ready(function(){
		
		$.datepicker.setDefaults($.extend({
			showMonthAfterYear: false,
			appendText: "&nbsp;&nbsp;(${app.formatPattern.date})",
			dateFormat: "dd/mm/yyyy",
			showOn: "button", //button|both
			duration: 'fast',
			buttonImage: "${ctx}/images/calendar.png", 
			buttonImageOnly: true,
			changeMonth: true, 
			changeYear: true,
			gotoCurrent: true
			}, $.datepicker.regional['']));
		$(".datePicker").datepicker($.datepicker.regional['${preferredLanguage}']);
		
		$(".double").attr("onkeypress","javascript:return doubleFilter(event);");
		$(".integer").attr("onkeypress","javascript:return integerFilter(event);");
		$(".noLetter").attr("onkeypress","javascript:return noLetterFilter(event);");
	});
	
	var cookiePath = {path: '${ctx}'};
	
	function submitAction(formElem, actionName) {
		var inputElem = document.createElement("input");
		inputElem.setAttribute("type", "hidden");
		inputElem.setAttribute("name", actionName);
		formElem.appendChild(inputElem);
		$('select.selectAll').each( function() {
			SelectUI.selectAll(this);
		});
		formElem.submit();
	}
	
	//CRUD Operations
	function onSearch(){
		submitAction(document.mainForm, 'search');
	}
	function onCreate(){
		submitAction(document.mainForm, 'create');
	}
	function onEdit(){
		if(document.mainForm.idList==null){
			alert('Please select item to edit.');
			return;
		}
		if (ChkBoxUI.equalsCheckedItemsCount(document.mainForm.idList, 0)) {
			alert('Please select item to edit.');
			return;
		}
		if (!ChkBoxUI.equalsCheckedItemsCount(document.mainForm.idList, 1)) {
			alert('Can edit only 1 item per time.');
			return;
		}
		submitAction(document.mainForm, 'edit');
	}
	function onBulkDelete(){
		if(document.mainForm.idList==null){
			alert('Please select item to delete.');
			return;
		}
		if (ChkBoxUI.equalsCheckedItemsCount(document.mainForm.idList, 0)) {
			alert('Please select item to delete.');
			return;
		}
		if (confirm('Are you sure to delete all selected items?')) {
			submitAction(document.mainForm, 'deleteCheckedItems');
		}
	}
	function onExport(){
		submitAction(document.mainForm, 'export');
	}
	function onBack(){
		alert('Override me, Dude!');
	}
	function onClear(){
		Form.clear(document.mainForm);
	}
</script>