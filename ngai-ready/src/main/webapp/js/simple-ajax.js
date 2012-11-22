
function ajaxInvoke(form, event, resultElem, infoClass, errorClass) {
	params = {};
	if (event != null)
		params = event + '&' + form.serialize();
	// alert("params="+params);
	// alert("form="+form.serialize());
	if(errorClass==null){
		errorClass = infoClass;
	}
	$.post(form.attr('action'), params, function(result) {
		if (result.substring(0, 6) == 'error:') {
			if(infoClass!=null){
				resultElem.removeClass(infoClass);
				resultElem.addClass(errorClass);
			}
			if(resultElem.attr('type')==null){
				resultElem.html(result.substring(6));
			}else{
				resultElem.val(result.substring(6));
			}
		} else {
			if(infoClass!=null){
				resultElem.removeClass(errorClass);
				resultElem.addClass(infoClass);
			}
			if(resultElem.attr('type')==null){
				resultElem.html(result);
			}else{
				resultElem.val(result);
			}
		}
	});
}

function ajaxInvokeWithCallbacks(form, event, successFunction, errorFunction) {
	params = {};
	if (event != null)
		params = event + '&' + form.serialize();
	$.post(form.attr('action'), params, function(result) {
		if (result.substring(0, 6) == 'error:') {
			if(errorFunction!=null){
				result = result.substring(6);
				eval('errorFunction(result);');
			}else{
				alert(result);
			}
		}else{
			if(successFunction!=null){
				eval('successFunction(result);');
			}else{
				alert(result);
			}
		}
	});
}
