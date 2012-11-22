<style type="text/css">
	input[type=text],select {
		width: 300px;
	}
</style>
<script type="text/javascript">
	function onBack() {
		window.location = '${ctx}/action/admin/role';
	}
	function onClear() {
		document.mainForm.reset();
		$('#eventNames').html('');
	}

	function getAvailabeEvents(){}
	
	$(document).ready( function() {
		$('#dialog').dialog( {
			bgiframe : true,
			autoOpen : false,
			height : 300,
			width: 350,
			modal : true,
			buttons : {
				'Cancel' : function() {
					$(this).dialog('close');
				},
				'Select' : function() {
					$('#actionUri').val($('#selectedActionUri').val());
					$(this).dialog('close');
					getAvailabeEvents();
				}
			}
		});
		$('#chooseUri').click( function() {
			$('#dialog').dialog('open');
		});
		
	});
</script>