package com.siberhus.ngai.ui.tags.treemenu.adapter;

import org.apache.commons.lang.StringEscapeUtils;

import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.model.TreeMenu.LinkType;
import com.siberhus.ngaiready.admin.model.TreeMenu.Status;

public class DropdownMenuAdapter extends BaseTreeMenuAdapter {
	
	public String getHtmlTop(){
		return "<span id='menu_dropdown_preload1'></span>" +
				"<span id='menu_dropdown_preload2'></span>" +
				"<ul id='menu_dropdown_nav'>";
	}
	
	public String getHtmlBottom(){
		return "</ul>";
	}
	
	public void addMenuItem(TreeMenu menu, StringBuilder builder, boolean topMenu){
		if(menu.getStatus()==Status.HIDDEN){
			return;
		}
		boolean permitted = isPermitted(menu);
		
		if(!permitted && menu.getChildren().size()==0){
			return;
		}
		
		String targetUrl = getTargetUrl(menu, permitted);
		
		builder.append("<li id='").append(menu.getId()).append("' ");
		if(topMenu){
			builder.append("class='top' ");
		}
		builder.append(">");
		
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
		if(menu.getLabelKey()!=null){
			builder.append("title='").append(
					menu.getLabelKey()).append("' ");
		}
		if(topMenu){
			builder.append("class='top_link' ");
		}else{
			if(menu.getChildren().size()!=0){
				builder.append("class='fly' ");
			}
		}
		builder.append(">");
		if(topMenu){
			if(menu.getChildren().size()==0){
				builder.append("<span>");
			}else{
				builder.append("<span class='down'>");
			}
		}
		String label = menu.getLabel();
		if(menu.getLabelKey()!=null){
			NgaiResourceBundle resBundle = getResourceBundleService().findFieldBundle(
					getPreferredLanguage(), null, menu.getLabelKey());
			if(resBundle!=null){
				label = resBundle.getValue(); 
			}
		}
		label = StringEscapeUtils.escapeHtml(label);
		builder.append(label);
		if(topMenu){
			builder.append("</span>");
		}
		builder.append("</a>\n");
		//End link construction
		
		if(menu.getChildren().size()!=0){
			builder.append("<ul");
			if(topMenu){
				builder.append(" class='sub'");
			}
			builder.append(">");
			for(TreeMenu child : menu.getChildren()){
				addMenuItem(child, builder, false);
			}
			builder.append("</ul>");
		}
		
		builder.append("</li>\n");
	}
	
}
