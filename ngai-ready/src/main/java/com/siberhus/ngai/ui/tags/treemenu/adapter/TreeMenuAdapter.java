package com.siberhus.ngai.ui.tags.treemenu.adapter;

import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.model.TreeMenu.LinkType;
import com.siberhus.ngaiready.admin.model.TreeMenu.Status;

public class TreeMenuAdapter extends BaseTreeMenuAdapter {

	@Override
	public void addMenuItem(TreeMenu menu, StringBuilder builder,
			boolean topMenu) {
		
		if(menu.getStatus()==Status.HIDDEN){
			return;
		}
		boolean permitted = isPermitted(menu);
		
		if(!permitted && menu.getChildren().size()==0){
			return;
		}
		String targetUrl = getTargetUrl(menu, permitted);
		
		builder.append("<li id='").append(menu.getId()).append("'");
		if(targetUrl.startsWith("#") && menu.getChildren().size()!=0){
			builder.append(" class='folder' ");
		}
		builder.append(">");
		String label = menu.getLabel();
		if(menu.getLabelKey()!=null){
			NgaiResourceBundle resBundle = getResourceBundleService().findFieldBundle(
					getPreferredLanguage(), null, menu.getLabelKey());
			if(resBundle!=null){
				label = resBundle.getValue(); 
			}
		}
		//Begin link construction
		builder.append("<a ");
		if(menu.getLinkType()==LinkType.HREF){
			builder.append("href='");
			builder.append(targetUrl+"' ");
		}else{
			builder.append("href='#' ");
			builder.append("onclick='window.location.href=\"");
			builder.append(targetUrl+"\"' ");
		}
		builder.append(">");
		builder.append(label).append("</a>\n");
		
		if(menu.getChildren().size()!=0){
			builder.append("<ul>");
			for(TreeMenu child : menu.getChildren()){
				addMenuItem(child, builder, topMenu);
			}
			builder.append("</ul>");
		}
		builder.append("</li>\n");
	}
	
	@Override
	public String getHtmlTop() {
		return "";
	}
	
	@Override
	public String getHtmlBottom() {		
		
		return "";
	}
}
