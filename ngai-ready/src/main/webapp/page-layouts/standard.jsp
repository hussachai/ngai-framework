<%@ include file="/page-includes/commons.inc.jsp" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<s:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <title>Ngai Ready</title>
			<%@include file="/page-layouts/resources.inc.jsp" %>
			<%@include file="/page-layouts/preset.inc.jsp" %>
            <s:layout-component name="html$head"/>
            <script type="text/javascript">
	            function hideSidebar(sidebar){
					sidebar.attr('href','#sidebar-show');
					sidebar.text('(Show)');
					$('.span-workarea').removeClass('span-16');
					$('.span-workarea').addClass('span-24');
					$('#sidebar').hide();
				}
				function showSidebar(sidebar){
					sidebar.attr('href','#sidebar-hide');
					sidebar.text('(Hide)');
					$('.span-workarea').removeClass('span-24');
					$('.span-workarea').addClass('span-16');
					$('#sidebar').show();
				}
				$(document).ready(function() {
					$('.span-workarea').addClass('span-17');
					$('#accordion').accordion({autoHeight:false});

					$('#toggleSidebar').click(function(){
						if($(this).attr('href')=='#sidebar-hide'){
							hideSidebar($(this));
							$.cookie('sidebar','hide', cookiePath);
						}else{
							showSidebar($(this));
							$.cookie('sidebar', null, cookiePath);
						}
					});
					if($.cookie('sidebar')=='hide'){
						hideSidebar($('#toggleSidebar'));
					}else{
						showSidebar($('#toggleSidebar'));
					}
				});
				<s:layout-component name="html$head$script"/>
			</script>
        </head>
        <body id="public" class="defaultBg">
            <div id="container" class="container" style="padding: 0px 10px 10px 10px;">
            	<div class="span-24 append-bottom last">
            		<ui:treeMenu name="dropdownMenu" store="none" adapter="dropdown"></ui:treeMenu>
            	</div>
            	<div class="span-24 last">
            		<div class="span-7">
            			<s:layout-component name="title"/>
            		</div>
            		<div style="text-align: right;" class="span-17 last">
            			<a href="#">
            				<img src="${ctx }/images/house.png" border="0"/>
            			</a>
            			<ui:treeMenuPath store="none" separator=">"></ui:treeMenuPath>
            		</div>
            	</div>
            	<div style="text-align: left;" class="span-24 last">
					Sidebar <a id="toggleSidebar" href="#sidebar-hide">(Hide)</a>
				</div>
            	<div id="sidebar" class="span-7">
	            	<%@ include file="/page-includes/sidebar.inc.jsp" %>
				</div>
            	<div class="span-workarea last">
            		<div class="span-main last">
	            		<s:errors />
		            	<s:messages/>
		            </div>
		            <div class="span-workarea last">
            			<s:layout-component name="content"/>
            		</div>
	            </div>
				<hr class="space" />
				<c:if test="${app.mode ne 'prod'}">
					<%@ include file="/page-includes/dev-panel.inc.jsp" %>
				</c:if>
        </body>
    </html>
</s:layout-definition>
