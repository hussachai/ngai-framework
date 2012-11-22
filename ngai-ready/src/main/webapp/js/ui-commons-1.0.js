/**
 * UICommons.js
 * version 1.0
 * @author Hussachai Puripunpinyo
 * Copyright 2009 SiberHus.com All Rights Reserved
 */
UI = {};
ChkBoxUI = {};
SelectUI= {};
TableUI = {};

UI.getParentElementById = function(elementId){
	if(window.opener){
		return window.opener.document.getElementById(elementId);
	}else{
		var xWin=window.dialogArguments;
		return xWin.document.getElementById(elementId);
	}
};

UI.createElement = function(elemeType, attribs){
	var elem = document.createElement(elemeType);
	for(var key in attribs){
		elem.setAttribute(key, attribs[key]);
	}
	return elem;
}

UI.findElement = function(elementOrId){
	var element;
	if (typeof elementOrId == "string") {
		element = document.getElementById(elementOrId);
		return element;
	}else{
		element = elementOrId;
		if(element!=null){
			return element;
		}
		return UI.getParentElementById(elementOrId);
	}
};

UI.getElementsByClass = function(searchClass,node,tag) {
	
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
};

UI.findPos = function(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		}
	}
	return [curleft,curtop];
};

UI.getScreenSize = function(){
	var screenW = 640, screenH = 480;
	if (parseInt(navigator.appVersion)>3) {
		screenW = screen.width;
		screenH = screen.height;
	}
	else if (navigator.appName == "Netscape" 
		&& parseInt(navigator.appVersion)==3
		&& navigator.javaEnabled()) {
		var jToolkit = java.awt.Toolkit.getDefaultToolkit();
		var jScreenSize = jToolkit.getScreenSize();
		screenW = jScreenSize.width;
		screenH = jScreenSize.height;
	}
	return [screenW,screenH];
};

/**
 * Example:
 * <img class="lookupItem" onclick="UI.showDialog(this,{target:'${ctx}/action/merchant?layout=minimum&elemId=merchant&modelName=name&multiSel=true'});"/>
 */
UI.showDialog = function(callerElem, attribs){
	var position = UI.findPos(callerElem);
	var screenSize = UI.getScreenSize();
	var width = screenSize[0]/2+'px';
	var height = screenSize[1]/2+'px';
	var top,left;
	var scrollable = 1;
	var resizable = 1;
	var statusbar = 1;
	var target = attribs.target;
	var modal = attribs.modal;
	var position = 'center';//center,relative,absolute
	if(modal==null){
		modal = false;
		if(BrowserDetect){
			if(BrowserDetect.browser=='Firefox'
				|| BrowserDetect.browser=='Safari'){
				modal = true;
			}
		}
	}
	if(!target){
		alert('Error: target is mandatory attribute.');
		return;
	}
	if(attribs.height!=null){
		height = attribs.height;
	}
	if(attribs.width!=null){
		width = attribs.width;
	}
	if(attribs.position!=null){
		position = attribs.position;
	}
	if(position=='center'){
		left = screenSize[0]/4+'px';
		top = screenSize[1]/4+'px';
	}else if(position=='relative'){
		left = position[0];
		top = position[1];
	}else{
		//absolute
		left = attribs.left;
		top = attribs.top;
	}
	
	if(attribs.scrollable!=null){
		scrollable = attribs.scrollable;
	}
	if(attribs.resizable!=null){
		resizable = attribs.resizable;
	}
	if(attribs.statusbar!=null){
		statusbar = attribs.statusbar;
	}
	if(modal){
		var params = 'dialogLeft:'+left+';'
			+ 'dialogTop:'+top+';'
			+ 'dialogHeight:'+height+';'
			+ 'dialogWidth:'+width+';'
			+ 'resizable:'+resizable+';'
			+ 'scroll:'+scrollable+';'
			+ 'status:'+statusbar;
		//alert(params);
		window.showModalDialog(target, window,params);
	}else{
		var fullscreen = 0;
		var addressbar = 0;
		var menubar = 0;
		var titlebar = 0;
		var toolbar = 0;
		var params = 'left='+left+','
			+ 'top='+top+','
			+ 'height='+height+','
			+ 'width='+width+','
			+ 'resizable='+resizable+','
			+ 'scroll='+scrollable+','
			+ 'status='+statusbar+','
			+ 'fullscreen='+fullscreen+','
			+ 'location='+addressbar+','
			+ 'menubar='+menubar+','
			+ 'titlebar='+titlebar+','
			+ 'toolbar='+toolbar;

		//alert(params);
		window.open(target,'pop',params);
	}
};

ChkBoxUI.toggleCheckedItems = function (chkElems, toggleElem) {
	var status = false;
	if (toggleElem.checked) {
		status = true;
	}
	if(chkElems.length>1){
		for (i = 0; i < chkElems.length; i++) {
			chkElems[i].checked = status;
		}
	}else{
		chkElems.checked = status;
	}
};

ChkBoxUI.equalsCheckedItemsCount = function (chkElems, expectedNumber){
	var counter = 0;
	var i;
	if(chkElems.length>1){
		for (i = 0; i < chkElems.length; i++) {
			if(chkElems[i].checked){
				counter = counter+1;
			}
		}
	}else{
		if(chkElems.checked){
			counter = counter+1;
		}
	}
	if(counter==expectedNumber){
		return true;
	}
	return false;
};

SelectUI.hasOption = function(selectElemOrId, optionValue){
	var selectElem = UI.findElement(selectElemOrId);
	var i;
	for (i = selectElem.length - 1; i >= 0; i--) {
		if (selectElem.options[i].value==optionValue) {
			return true;
		}
	}
	return false;
};

SelectUI.addOption = function(selectElemOrId, optionValue, optionLabel){
	var selectElem = UI.findElement(selectElemOrId);
	//selectElem.options[selectElem.options.length] = new Option(optionLabel, optionValue); 
	var option ;
	if(window.opener){
		option = window.opener.document.createElement('option');
	}else if(window.dialogArguments){
		option = window.dialogArguments.document.createElement("option");
	}else{
		option = window.document.createElement('option');
	}
	option.text =  optionLabel;
	option.value =  optionValue;
	try {
		selectElem.add(option, null); // standards compliant; doesn't work in IE
	} catch (ex) {
		selectElem.add(option); // IE only
	}
};

SelectUI.clear = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	selectElem.options.length = 0;
};

SelectUI.removeSelected = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	var i;
	for (i = selectElem.length - 1; i >= 0; i--) {
		if (selectElem.options[i].selected) {
			selectElem.remove(i);
		}
	}
};
SelectUI.selectAll = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	if (selectElem.type == "select-multiple") {
		for (var i = 0; i < selectElem.options.length; i++) {
			selectElem.options[i].selected = true;
		}
	}
};

var i = 0;
TableUI.insertRow = function (index){
	var row=document.getElementById('myTable').insertRow(index);
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);
	var cell3 = row.insertCell(2);
	cell1.innerHTML="CELL"+i;
	cell2.innerHTML="<input type='text' name='anytext'/>";
	cell3.innerHTML="<input type='button' value='remove' onclick='removeRow(this);'/>";
	i++;
};

TableUI.removeRow = function (r){
	var i=r.parentNode.parentNode.rowIndex;
	document.getElementById('myTable').deleteRow(i);
};

