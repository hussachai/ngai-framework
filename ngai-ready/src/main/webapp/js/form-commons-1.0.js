var Form = {}

Form.setReadonly = function(targetForm) {
	for (i = 0; i < targetForm.length; i++) {
		var element = targetForm.elements[i];
		element.setAttribute("className", "readonly");
		element.setAttribute("class", "readonly");
		element.setAttribute("style", "border: none");
		element.setAttribute("readonly", "readonly");
		// element.setAttribute("disabled","disabled");
	}
}

Form.createField = function(targetForm, fieldType, attribs){
	var field = document.createElement(fieldType);
	for(var key in attribs){
		field.setAttribute(key, attribs[key]);
	}
	targetForm.appendChild(field);
}

Form.setFocus = function setFocus(id) {
    var field = document.getElementById(id);
    if (field && field.focus && field.type != "hidden" && field.disabled != true) {
    	try {
			field.focus();
		} catch (err) {
		}
    }
}

Form.trim = function trim(str) {  
    while (str.charAt(0) == (" ")) {  
        str = str.substring(1);
      }
      while (str.charAt(str.length - 1) == " ") {  
          str = str.substring(0,str.length-1);
      }
      return str;
}

Form.selectParentItem = function(modelValue, modelLabel, parentElemId) {
	var elem = UI.getParentElementById(parentElemId);
	if (elem.type == 'hidden') {
		//alert('hidden');
		var elemLabel = UI.getParentElementById(parentElemId + 'Label');
		if (elemLabel) {
			elemLabel.value = modelLabel;
		}
		elem.value = modelValue;
		// }else if(elem.type=='text'){
		// alert('text');
	} else if (elem.type == 'select-one' || elem.type == 'select-multiple') {
		//alert('select');
		elem.value = modelValue;
	}
	window.close();
}

Form.addParentItem = function(modelValue, modelLabel, parentElemId) {
	var elem = UI.getParentElementById(parentElemId);
	if (elem.type == 'select-one' || elem.type == 'select-multiple') {
		//alert('select');
		if (SelectUI.hasOption(elem, modelValue)) {
			alert(modelLabel + ' has already existed!');
			return;
		}
		SelectUI.addOption(elem, modelValue, modelLabel);
	} else {
		alert('Error: Operation does not support!');
	}
}
/*
Form.submitAction = function(formElem, actionName) {
	var inputElem = document.createElement("input");
	inputElem.setAttribute("type", "hidden");
	inputElem.setAttribute("name", actionName);
	formElem.appendChild(inputElem);
	formElem.submit();
}
*/
Form.clear = function(targetForm) {
	var object = new Array();
	object[0] = targetForm.getElementsByTagName('input');
	object[1] = targetForm.getElementsByTagName('textarea');
	object[2] = targetForm.getElementsByTagName('select');
	var type = null;
	for ( var x = 0; x < object.length; x++) {
		for ( var y = 0; y < object[x].length; y++) {
			type = object[x][y].type
			switch (type) {
			case "hidden":
			case "text":
			case "textarea":
			case "password":
				object[x][y].value = "";
				break;
			case "radio":
			case "checkbox":
				object[x][y].checked = "";
				break;
			case "select-one":
				object[x][y].options[0].selected = true;
				break;
			case "select-multiple":
				for (z = 0; z < object[x][y].options.length; z++) {
					object[x][y].options[z].selected = false;
				}
				break;
			}
		}
	}
}
