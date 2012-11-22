package com.siberhus.ngai.ui.tags.treemenu.adapter;

import javax.servlet.jsp.PageContext;

import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.service.ITreeMenuService;

public interface ITreeMenuAdapter {
	
	public String getHtmlTop();
	
	public String getHtmlBottom();
	
	public void addMenuItem(TreeMenu menu, StringBuilder builder, boolean topMenu);
	
	public void setTreeMenuService(ITreeMenuService treeMenuService);
	
	public void setResourceBundleService(
			IResourceBundleService resourceBundleService);
	
	public void setPageContext(PageContext pageContext);
	
	public void setUser(User user);
	
}
