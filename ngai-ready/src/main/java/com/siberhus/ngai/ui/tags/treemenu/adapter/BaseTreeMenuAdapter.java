package com.siberhus.ngai.ui.tags.treemenu.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.guardian.config.GuardianConfig;
import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.localization.tags.LocalizableTag;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.model.TreeMenu.Status;
import com.siberhus.ngaiready.admin.service.ITreeMenuService;

public abstract class BaseTreeMenuAdapter implements ITreeMenuAdapter {
	
	private ITreeMenuService treeMenuService;
	
	private IResourceBundleService resourceBundleService;
	
	private PageContext pageContext;
	
	private User user;
	
	public ITreeMenuService getTreeMenuService() {
		return treeMenuService;
	}

	public void setTreeMenuService(ITreeMenuService treeMenuService) {
		this.treeMenuService = treeMenuService;
	}
	
	public IResourceBundleService getResourceBundleService() {
		return resourceBundleService;
	}

	public void setResourceBundleService(
			IResourceBundleService resourceBundleService) {
		this.resourceBundleService = resourceBundleService;
	}

	public PageContext getPageContext() {
		return pageContext;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	protected String getPreferredLanguage(){
		return LocalizableTag.getPreferredLanguage(getPageContext());
	}
	
	protected String getTargetUrl(TreeMenu menu, boolean permitted){
		if(!permitted || menu.getStatus()==Status.DISABLED){
			return "#disabled";
		}
		String targetUrl = menu.getLinkUrl();
		if(targetUrl!=null){
			if(targetUrl.startsWith("/")){
				String contextPath = pageContext.getServletContext().getContextPath();
				targetUrl = contextPath + targetUrl;
			}
			String parameters = StringUtils.substringAfter(targetUrl, "?");
			if(parameters!=null){
				targetUrl = StringUtils.substringBefore(targetUrl, "?");
				try {
					parameters = URLEncoder.encode(parameters,"UTF-8");
				} catch (UnsupportedEncodingException e) {}
				targetUrl = targetUrl+"?"+parameters;
			}
				
			return targetUrl;
		}
		return "#";
	}
	
	protected boolean isPermitted(TreeMenu menu){
		if(menu.getRoleSet().size()==0){
			return true;
		}
		if(user.getUsername().equals(
				GuardianConfig.get().getRootUsername())){
			return true;
		}
		for(Role role : menu.getRoleSet()){
			if(user.getRoleSet().contains(role)){
				return true;
			}
		}
		
		return false;
	}
	
}
