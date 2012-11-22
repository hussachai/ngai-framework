<script type="text/javascript">
	$(document).ready(function(){
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
				$('#actionBeanName').val($('#selectedActionBeanName').val());
				$(this).dialog('close');
			}
		}
		});
		$('#chooseActionBeanName').click( function() {
			$('#dialog').dialog('open');
		})
	});
</script>