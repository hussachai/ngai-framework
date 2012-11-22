<s:layout-component name="sidebar">
		<div>
			<script type="text/javascript">
			/*
				var simpleTreeCollection;
				$(document).ready(function(){
					simpleTreeCollection = $('#leftTreeMenu').simpleTree({
						drag: false,
						autoclose: false,
						imagesDir: '${ctx}/css/simpletree',
						afterClick:function(node){
							
						},
						animate:true,
						docToFolderConvert:true
					});
				});
				*/
				$(document).ready(function(){
						$('#leftTreeMenu').dynatree({
							persist: true
						});
				});
			</script>
			<div id="leftTreeMenu">
				<ul >
					<ui:treeMenu name="leftTreeMenu" store="none" adapter="tree"></ui:treeMenu>
				</ul>
			</div>
		</div>
	<div id="accordion">
		<s:layout-component name="accordion"></s:layout-component>
		
		<h3><a href="#">Login Info</a></h3>
		<div>
			<div>
				<s:label for="username">Username: </s:label>
				<span>${loginInfo.username }</span><br/>
				<s:label for="roles">Roles: </s:label>
				<span>${loginInfo.roles }</span><br/>
				<s:label for="ipAddress">IP Address: </s:label>
				<span>${loginInfo.ipAddress }</span><br/>
				<s:label for="systemLocale">System Locale: </s:label>
				<span>${loginInfo.systemLocale }</span><br/>
				<s:label for="preferredLocale">Preferred Locale: </s:label>
				<span>${loginInfo.preferredLocale }</span><br/>
				<s:label for="loginAt">Login At: </s:label>
				<span>${loginInfo.loginAt }</span><br/>
				<div align="center" style="padding-top: 10px;">
					<img src="${ctx }/images/lock.png" />
					<a href="${ctx }/action/authen/logout">logout</a>
				</div>
			</div>
		</div>
		<h3><a href="#">Last Visit Pages</a></h3>
		<div>
			<p>
			Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet
			purus. Vivamus hendrerit, dolor at aliquet laoreet, mauris turpis porttitor
			velit, faucibus interdum tellus libero ac justo. Vivamus non quam. In
			suscipit faucibus urna.
			</p>
		</div>
		<h3><a href="#">FAQ</a></h3>
		<div>
			<p>
			Nam enim risus, molestie et, porta ac, aliquam ac, risus. Quisque lobortis.
			Phasellus pellentesque purus in massa. Aenean in pede. Phasellus ac libero
			ac tellus pellentesque semper. Sed ac felis. Sed commodo, magna quis
			lacinia ornare, quam ante aliquam nisi, eu iaculis leo purus venenatis dui.
			</p>
			<ul>
				<li>List item one</li>
				<li>List item two</li>
				<li>List item three</li>
			</ul>
		</div>
		
		<auth:hasPermission uri="/action/admin/resourceBundle">
		<h3><a href="#" title="localization">Localization</a></h3>
		<div id="localization">
			<script type="text/javascript">
				
				$(document).ready(function() {
					var actionBeanName = '${actionBeanFQCN}';
					var localizationIdx = -1;
					$.each($('label'),function(i, elem){
						//alert(elem.getAttribute('for'));
						var key = $(this).attr('for');
						/*
						if($(this).get().type=='label'){
							key = $(this).attr('for');
						}else{
							key = $(this).attr('key');
						}
						*/
						$(this).click(function(){
							$('#local\\.actionBeanName').val(actionBeanName);
							//var actionBeanNameDisplay = actionBeanName.substr(actionBeanName.lastIndexOf('.')+1);
							//$('#local\\.actionBeanNameDisplay').text(actionBeanNameDisplay);
							$('#local\\.key').val(key);
							$('#local\\.keyDisplay').text(key);
							ajaxInvokeWithCallbacks($('#langForm'),'ajaxGetFieldValue',
									function(data){
										$('#local\\.value').val(data);
									}, 
									function(data){
										$('#local\\.result').val(data);
										$('#local\\.result').addClass('redColor');
									});
							if(localizationIdx==-1){
								$.each($('#accordion > div'),function(i,val){
									if($(this).attr('id')=='localization'){
										localizationIdx = i;
									}
								});
							}
							$('#accordion').accordion('activate',localizationIdx);
						});
					});

					$('#ajaxSaveFieldBundle').click(function(){
						ajaxInvoke($('#langForm'),'ajaxSaveFieldValue',$('#local\\.result'), 'blueColor','redColor');
					});
					$('#ajaxClearFieldBundleCache').click(function(){
						if(!confirm('Are you sure to rebuild message bundle cache? It\'s quite slow operation.')){
							return;
						}
						ajaxInvoke($('#langForm'),'ajaxClearFieldBundleCache',$('#local\\.result'), 'blueColor','redColor');
					});
				});
			</script>
			<s:form id="langForm" action="/action/admin/resourceBundle">
				<input id="local.language" type="hidden" name="language" value="${preferredLanguage }" />
				<input id="local.actionBeanName" type="hidden" name="actionBeanName" />
				<input id="local.key" type="hidden" name="key" />
				<div>
					<s:label for="local.language">Language: </s:label>
					<span id="local.languageDisplay">${preferredLanguage }</span>
				</div>
				<div>
					<s:label for="local.key">Key: </s:label>
					<span id="local.keyDisplay">-</span>
				</div>
				<div>
					<s:label for="local.global">Global: </s:label>
					<input type="checkbox" name="global" value="true" checked="checked"/>
				</div>
				<div>
					<s:label for="local.value">Value: </s:label>
					<input id="local.value" style="width: 200px;" type="text" name="value" />
				</div>
				<div>
					<a id="ajaxSaveFieldBundle" class="button positive" href="#">Save</a>
					<a id="ajaxClearFieldBundleCache" class="button positive" href="#">Rebuild Cache</a>
				</div>
				<hr/>
				<div>
					<span id="local.result"></span>
				</div>
			</s:form>
		</div>
		</auth:hasPermission>
	</div>
	<br/>
	
</s:layout-component>