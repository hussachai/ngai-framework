package com.siberhus.ngai.ui.tags.treemenu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.config.GuardianConfig;
import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.localization.service.impl.ResourceBundleService;
import com.siberhus.ngai.ui.tags.treemenu.adapter.DropdownMenuAdapter;
import com.siberhus.ngai.ui.tags.treemenu.adapter.ITreeMenuAdapter;
import com.siberhus.ngai.ui.tags.treemenu.adapter.TreeMenuAdapter;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.model.TreeMenu.Status;
import com.siberhus.ngaiready.admin.service.ITreeMenuService;
import com.siberhus.ngaiready.admin.service.impl.TreeMenuService;

public class TreeMenuTag extends TagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ITreeMenuService treeMenuService;
	
	private IResourceBundleService resourceBundleService;
	
	private ITreeMenuAdapter adapterObject;
	
	private User user;
	
	private String name = null; //cookie friendly name (cookie allows pure ascii name)
	
	private String store = "session";
	
	private String adapter = "dropdown";
	
	private String theme = "green";
	
	@Override
	public int doStartTag() throws JspException {
		
		SessionUser sessionUser = (SessionUser)pageContext.getSession()
			.getAttribute(SessionUser.USER_SESSION_ATTR);
		
		if(sessionUser==null){
			return SKIP_BODY;
		}
		
		String treeViewHtml = getAttribute(getName());
		
		if(treeViewHtml==null){
			user = sessionUser.getRealUserObject();
			treeMenuService = (ITreeMenuService)ServiceBeanRegistry
				.get(ITreeMenuService.class, TreeMenuService.class);
			resourceBundleService = (IResourceBundleService)ServiceBeanRegistry
				.get(IResourceBundleService.class, ResourceBundleService.class);
			
			if("dropdown".equals(adapter)){
				adapterObject = new DropdownMenuAdapter();
			}else if("tree".equals(adapter)){
				adapterObject = new TreeMenuAdapter();
			}
			adapterObject.setPageContext(pageContext);
			adapterObject.setUser(user);
			adapterObject.setTreeMenuService(treeMenuService);
			adapterObject.setResourceBundleService(resourceBundleService);
			
			treeViewHtml = generateTreeView();
			setAttribute(getName(), treeViewHtml);
		}
		
		try {
			pageContext.getOut().append(treeViewHtml);
		} catch (Exception e) {
			throw new JspTagException(e);
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	public String generateTreeView(){
		
		List<TreeMenu> treeMenuList = getTreeMenuService().getFirstLevelMenus();
		StringBuilder builder = new StringBuilder();
		builder.append(adapterObject.getHtmlTop());
		for(TreeMenu menu : treeMenuList){
			adapterObject.addMenuItem(menu, builder, true);
		}
		builder.append(adapterObject.getHtmlBottom());
		
		return builder.toString();
	}
	
	@Override
	public void release(){
		super.release();
		user = null;
		name = null;
		store = null;
		adapter = null;
		theme = null;
		adapterObject = null;
	}
	
	protected String getAttribute(String name){
		if("session".equalsIgnoreCase(getStore())){
			return (String)pageContext.getSession().getAttribute(name);
		}else if("none".equalsIgnoreCase(getStore())){
			return (String)pageContext.getAttribute(name);
		}else if("cookie".equalsIgnoreCase(getStore())){
			
		}
		throw new IllegalArgumentException("Unsupported store: "+store);
	}
	
	protected void setAttribute(String name, String value){
		if("session".equalsIgnoreCase(getStore())){
			pageContext.getSession().setAttribute(name, value);
		}else if("none".equalsIgnoreCase(getStore())){
			pageContext.setAttribute(name, value);
		}else if("cookie".equalsIgnoreCase(getStore())){
			
		}else{
			throw new IllegalArgumentException("Unsupported store: "+store);
		}
	}
	
	protected boolean isPermitted(TreeMenu menu){
		if(menu.getRoleSet().size()==0){
			return true;
		}
		if(getUser().getUsername().equals(
				GuardianConfig.get().getRootUsername())){
			return true;
		}
		for(Role role : menu.getRoleSet()){
			if(getUser().getRoleSet().contains(role)){
				return true;
			}
		}
		
		return false;
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
	
	public User getUser() {
		return user;
	}

	public ITreeMenuService getTreeMenuService() {
		return treeMenuService;
	}

	public IResourceBundleService getResourceBundleService() {
		return resourceBundleService;
	}
	
	//========== Tag Attributes ================//
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
	
	public String getAdapter() {
		return adapter;
	}
	
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	
	public String getTheme() {
		return theme;
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
}
