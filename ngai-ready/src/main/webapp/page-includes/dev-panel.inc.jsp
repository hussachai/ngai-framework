<div id="devPanel">
	<script type="text/javascript">
		$(document).ready(function(){
			$('#toggleGrid').click(function(){
				if($(this).attr('href')=='#grid-show'){
					$(this).attr('href', '#grid-hide');
					$(this).text('(Hide)');
					$('#container').addClass('showgrid');
				}else{
					$(this).attr('href', '#grid-show');
					$(this).text('(Show)');
					$('#container').removeClass('showgrid');
				}
			});
			$('#showCookies').click(function(){
				UI.showDialog($(this).get(),{
					target:'${ctx}/action/dev/cookieManager',
					width:'780px'
				});
			});
			$('#showSession').click(function(){
				UI.showDialog($(this).get(),{
					target:'${ctx}/action/dev/sessionManager',
					width:'780px'
				});
			});
			$('#showContext').click(function(){
				UI.showDialog($(this).get(),{
					target:'${ctx}/action/dev/contextManager',
					width:'780px'
				});
			});
		});
	</script>
	<div style="text-align: center;" class="rounded devPanel span-4">
		Dev Panel
	</div>
	<div class="rounded devPanel span-20 last">
		&nbsp; 
		Grid <a id="toggleGrid" href="#grid-show">(Show)</a>
		&nbsp; | &nbsp;
		<a id="showCookies" href="#" >Cookies</a>
		&nbsp; | &nbsp;
		<a id="showSession" href="#" >Session</a>
		&nbsp; | &nbsp;
		<a id="showContext" href="#" >Context</a>
		&nbsp; | &nbsp;
		<a href="${ctx }/action/dev/actionBeanList">ActionBean List</a>
		<br/>
		${measurement }
	</div>
</div>
<hr class="space" />