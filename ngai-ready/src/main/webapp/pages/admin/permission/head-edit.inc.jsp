<script type="text/javascript">
	var persistedEventNames = new Array();
	<c:forEach items="${model.eventNames}" var="eventName">
		persistedEventNames.push('${eventName}');
	</c:forEach>
	function getAvailabeEvents(){
		$.getJSON('${ctx}/action/admin/eventList',
			{ajaxGetEvents:'',actionUri:$('#actionUri').val(),sqlRegex:$('#uriRegex').val()},
				function(items){
					var eventOptions = '';
					$.each(items, function(i,n){
						eventOptions += '<li><input type="checkbox" id="eventNames.'+i+
							'" name="model.eventNames" value="'+n+'"/>&nbsp;'+n+'</li>';
					});
							
					$('#eventNames').html(eventOptions);
					
					if(persistedEventNames.length==1 && persistedEventNames[0]=='ALL'){
						$('#checkAllEvents').attr('checked','checked');
						var frmEventNames = document.mainForm['model.eventNames'];
						for(i=0;i<frmEventNames.length;i++){
							frmEventNames[i].checked = true;
							frmEventNames[i].disabled = true;
						}
					}else{
						var frmEventNames = document.mainForm['model.eventNames'];
						for(i=0;i<frmEventNames.length;i++){
							for(j=0;j<persistedEventNames.length;j++){
								if(persistedEventNames[j]==frmEventNames[i].value){
									frmEventNames[i].checked = true;
								}
							}
						}
					}
				});
	}
	$(document).ready(function(){
		$('#actionUri').blur(function(){
			getAvailabeEvents();
		});
		$('#checkAllEvents').click(function(){
			var frmEventNames = document.mainForm['model.eventNames'];
			for(i=0;i<frmEventNames.length;i++){
				if($('#checkAllEvents').attr('checked')){
					frmEventNames[i].checked = true;
					frmEventNames[i].disabled = true;
				}else{
					frmEventNames[i].disabled = false;
				}
			}
		});
		getAvailabeEvents();	
	});
	
</script>