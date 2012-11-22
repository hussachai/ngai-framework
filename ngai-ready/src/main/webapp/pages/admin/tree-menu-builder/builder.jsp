<%@ include file="/page-includes/commons.inc.jsp" %>

<s:layout-render name="/page-layouts/standard.jsp">
	<c:set var="mainAction" value="${ctx}/action/admin/treeMenuBuilder" />
	<s:layout-component name="html$head">
		<link rel="stylesheet" href="${ctx }/css/simpletree/simpletree.css" type="text/css" media="screen, projection"></link>
		<script type="text/javascript" src="${ctx }/js/jquery.simple.tree-0.3.js" ></script>
		<script type="text/javascript">
			var selectedNodeId;
			var selectedNodeName;
			var simpleTreeCollection;
			$(document).ready(function(){
				simpleTreeCollection = $('#treeMenuEditor').simpleTree({
					autoclose: false,
					imagesDir: '${ctx}/css/simpletree',
					afterClick:function(node){
						selectedNodeId = node.attr('id');
						$('#selectedNodeId').val(selectedNodeId);
						selectedNodeName = $('span:first',node).text();
					},
					afterDblClick:function(node){
						//alert("text-"+$('span:first',node).text());
					},
					afterMove:function(destination, source, pos){
						//alert("destination-"+destination.attr('id')+" source-"+source.attr('id')+" pos-"+pos);
						$.post('${mainAction}', {ajaxMove:'',
							selectedNodeId:source.attr('id'),
							newParentId:destination.attr('id'),
							newPosition:pos}, function(data){
								if(data!='ok'){
									alert(data);
									window.location.href = '${mainAction}';
								}
							});
					},
					afterAjax:function(){
						//alert('Loaded');
					},
					animate:true,
					docToFolderConvert:true
				});
				
				$('#dialog').dialog( {
					bgiframe : true,
					autoOpen : false,
					height : 450,
					width: 670,
					modal : false,
					buttons : {
						'Cancel' : function() {
							$(this).dialog('close');
						},
						'Save' : function() {
							//submitAction(document.mainForm,'save');
							$('select.selectAll').each( function() {
								SelectUI.selectAll(this);
							});;
							ajaxInvokeWithCallbacks($('#mainForm'),'ajaxSave',
								function(success){
									window.location.href = '${mainAction}';
								},
								function(error){
									$('#errorDetail').html(error);
								}
							);
						}
					}
				});
			});
			
			$(document).ready(function(){
				$('#backButton').click(function(){
					alert("Don't do that dude!");
				});
				$('#newButton').click(function(){
					Form.clear(document.mainForm);
					SelectUI.clear(document.getElementById('roles'));
					$('#parentMenu').val(selectedNodeId);
					$('#parentMenuDisplay').val(selectedNodeName);
					$('#dialog').dialog('open');
				});
				$('#editButton').click(function(){
					$.post('${mainAction}',
						{edit:' ',model:selectedNodeId},
						function(data){
							//alert(data);
							$('#dialog').html(data);
							$('#deleteRoleButton').addClass('deleteItem');
							$('#dialog').dialog('open');
						});	
				});
				$('#deleteButton').click(function(){
					$.post('${mainAction}',
						{ajaxCountChildren:'',model:selectedNodeId},
						function(data){
							if(data > 0){
								if(!confirm('This menu has total children = '+data+'\n'
										+'Do you still need to delete this menu? \n'
										+'If you\'d like to do so all children will be removed too!'))
									return;
							}else{
								if(!confirm('Are you sure to delete this item'))
									return;
									
							}
							$.post('${mainAction}',
								{'ajaxDelete':'', model:selectedNodeId},
								function(data){
									if(data == 'ok'){
										//window.location.href = '${mainAction}';
										simpleTreeCollection.get(0).delNode(); //delete selected node
									}else{
										alert(data);
									}
								});
						});
				});
			});
		</script>
	</s:layout-component>
	<s:layout-component name="title">
		<h2><s:label for="treeMenuBuilder">Tree Menu Builder</s:label></h2>
	</s:layout-component>
    <s:layout-component name="content">
    	<div id="message" style="color: red;"></div>
    	<div id="dialog" style="padding:1.2em; margin-right:2.4em;" title="Tree Menu Editor">
			<%@ include file="dialog.inc.jsp" %>
		</div>
		
    	<div class="span-workarea last">
    		<div class="fg-toolbar ui-widget-header ui-corner-all ui-helper-clearfix">
			<div class="fg-buttonset fg-buttonset-multi">
				<a id="backButton" href="#" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" 
					title="Back">
					<span class="ui-icon  ui-icon-arrowreturnthick-1-w"></span> Back
				</a>
			</div>
			<div class="fg-buttonset fg-buttonset-multi">
				<button id="newButton" class="fg-button ui-state-default ui-corner-left">New</button>
				<button id="editButton" class="fg-button ui-state-default ui-corner-right">Edit</button>
				<button id="deleteButton" class="fg-button ui-state-default">Delete</button>
			</div>
		</div>
		<hr class="space"/>
    	</div>
    	<div class="span-workarea last">
	    	<ul id="treeMenuEditor" class="simpleTree">
				<li class="root" id='0'><span><l10n:field key="rootMenu">Root Menu</l10n:field></span>
					<ul>
						${treeView }
					</ul>
				</li>
			</ul>
    	</div>
		<br/>
    </s:layout-component>
</s:layout-render>
	