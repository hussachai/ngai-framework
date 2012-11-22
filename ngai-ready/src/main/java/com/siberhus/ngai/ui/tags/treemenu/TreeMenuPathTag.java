package com.siberhus.ngai.ui.tags.treemenu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;

import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.tags.LocalizableTag;
import com.siberhus.ngaiready.admin.model.TreeMenu;

public class TreeMenuPathTag extends TreeMenuTag {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cssClass;
	
	private String cssStyle;
	
	private String separator = ">";
	
	@Override
	public int doStartTag() throws JspException {
		if(getName()==null){
			setName("NgaiReadyTreeMenuPath");
		}
		return super.doStartTag();
	}
	
	@Override
	public String generateTreeView() {
		
		String navPathHtml = null;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String contextPath = request.getContextPath();
		String requestUri = null;
		String actionBeanPath = (String)request.getAttribute("actionBeanPath");
		if(actionBeanPath!=null){
			if(actionBeanPath.startsWith(contextPath)){
				requestUri = actionBeanPath.substring(contextPath.length());
			}
			if(requestUri != null){
				List<TreeMenu> menuList = getTreeMenuService().getParentUntilRootByUri(requestUri);
				navPathHtml = generateNavigationPath(menuList);
			}
		}
		return navPathHtml;
	}
	
	protected String generateNavigationPath(List<TreeMenu> menuList){
		
		if(menuList==null || menuList.size()==0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		TreeMenu selfMenu = menuList.get(0);
		String pathAttribName =  getName()+"["+selfMenu.getId()+"]";
		String menuPathHtml = getAttribute(pathAttribName);
		
		if(menuPathHtml == null){
			
			for(int i=menuList.size()-1;i>=0;i--){
				
				TreeMenu menu = menuList.get(i);
				boolean permitted = isPermitted(menu);
				String targetUrl = getTargetUrl(menu, permitted);
				String preferredLang = LocalizableTag.getPreferredLanguage(pageContext);
				
				builder.append(" ").append(getSeparator()).append(" ");
				builder.append("<a href='");
				builder.append(targetUrl+"' ");
				if(menu.getLabelKey()!=null){
					builder.append("title='").append(
							menu.getLabelKey()).append("' ");
				}
				if(getStyle()!=null){
					builder.append("style='").append(
							getStyle()).append("' ");
				}
				if(getCssClass()!=null){
					builder.append("class='").append(
							getCssClass()).append("' ");
				}
				builder.append(">");
				String label = menu.getLabel();
				if(menu.getLabelKey()!=null){
					NgaiResourceBundle resBundle = getResourceBundleService().findFieldBundle(
							preferredLang, null, menu.getLabelKey());
					if(resBundle!=null){
						label = resBundle.getValue(); 
					}
				}
				label = StringEscapeUtils.escapeHtml(label);
				builder.append(label);
				builder.append("</a>");
			}
			menuPathHtml = builder.toString();
			setAttribute(pathAttribName, menuPathHtml);
		}
		return menuPathHtml;
	}
	
	
	@Override
	public void release() {
		super.release();
		cssClass = null;
		cssStyle = null;
		separator = ">";
	}
	
	//========== Tag Attributes ================//
	
	public String getCssClass() {
		return cssClass;
	}
	
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public void setClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	public String getStyle() {
		return cssStyle;
	}
	
	public void setStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
}
