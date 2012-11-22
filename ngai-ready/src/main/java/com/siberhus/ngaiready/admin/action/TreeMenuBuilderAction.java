package com.siberhus.ngaiready.admin.action;

import java.util.List;
import java.util.Locale;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import com.siberhus.ngai.action.AbstractAction;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.localization.SessionLocalePicker;
import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.localization.service.impl.ResourceBundleService;
import com.siberhus.ngaiready.admin.model.TreeMenu;
import com.siberhus.ngaiready.admin.service.ITreeMenuService;
import com.siberhus.ngaiready.admin.service.impl.TreeMenuService;

@UrlBinding("/action/admin/treeMenuBuilder")
public class TreeMenuBuilderAction extends AbstractAction {
	
	@InjectService(implementation=TreeMenuService.class)
	private ITreeMenuService treeMenuService;
	
	@InjectService(implementation=ResourceBundleService.class)
	private IResourceBundleService resourceBundleService;
	
	@ValidateNestedProperties({
		@Validate(on="ajaxSave", field="status", required=true),
		@Validate(on="ajaxSave", field="position", minvalue=0),
		@Validate(on="ajaxSave", field="label", required=true)
	})
	private TreeMenu model;
	
	private Long selectedNodeId;
	
	private Long newParentId;
	
	private Integer newPosition;
	
	private String preferredLanguage;
	
	@Override
	public String getPathPrefix() {
		
		return "/pages/admin/tree-menu-builder";
	}
	
	@Override
	public String getIndexPage() {
		return "/builder.jsp";
	}
	
	public Resolution edit(){
		getContext().getRequest().setAttribute("model", model);
		return forward(getPathPrefix()+"/edit.jsp");
	}
	
	public Resolution ajaxSave(){
		try{
			treeMenuService.save(model);
			return new StreamingResolution("text/plain","ok");
		}catch(Exception e){
			return new StreamingResolution("text/plain", "error:"+e.getLocalizedMessage()); 
		}
	}
	
	public Resolution ajaxDelete(){
		try{
			treeMenuService.delete(model);
			return new StreamingResolution("text/plain", "ok"); 
		}catch(Exception e){
			return new StreamingResolution("text/plain", "error:"+e.getLocalizedMessage()); 
		}
	}
	
	public Resolution ajaxCountChildren(){
		if(model!=null){
			return new StreamingResolution("text/plain", 
					String.valueOf(model.getChildren().size()));
		}
		return new StreamingResolution("text/plain", "error:Enity not found"); 
	}
	
	public Resolution ajaxMove(){
		try{
			treeMenuService.move(selectedNodeId, newParentId, newPosition);
			return new StreamingResolution("text/plain", "ok");
		}catch(Exception e){
			return new StreamingResolution("text/plain", "error:"+e.getLocalizedMessage()); 
		}
	}
	
	public TreeMenu getModel() {
		return model;
	}
	
	public void setModel(TreeMenu model) {
		this.model = model;
	}
	
	@After
	public void generateTreeView(){
		preferredLanguage = getPreferredLocale().getLanguage();
		List<TreeMenu> treeMenuList = treeMenuService.getFirstLevelMenus();
		StringBuilder builder = new StringBuilder();
		for(TreeMenu menu : treeMenuList){
			addMenuItem(menu, builder);
		}
		getContext().getRequest().setAttribute(
				"treeView", builder.toString());
	}
	
	protected void addMenuItem(TreeMenu menu, StringBuilder builder){
		builder.append("<li id='").append(menu.getId()).append("'");
		if(menu.getId() == selectedNodeId){
			builder.append(" class='open'");
		}
		builder.append(">");
		String label = menu.getLabel();
		if(menu.getLabelKey()!=null){
			NgaiResourceBundle resBundle = resourceBundleService.findFieldBundle(
					preferredLanguage, null, menu.getLabelKey());
			if(resBundle!=null){
				label = resBundle.getValue(); 
			}
		}
		builder.append("<span>").append(label).append("</span>\n");
		if(menu.getChildren().size()!=0){
			builder.append("<ul>");
			for(TreeMenu child : menu.getChildren()){
				addMenuItem(child, builder);
			}
			builder.append("</ul>");
		}
		builder.append("</li>\n");
	}

	public Long getSelectedNodeId() {
		return selectedNodeId;
	}

	public void setSelectedNodeId(Long selectedNodeId) {
		this.selectedNodeId = selectedNodeId;
	}
	
	
	public Long getNewParentId() {
		return newParentId;
	}

	public void setNewParentId(Long newParentId) {
		this.newParentId = newParentId;
	}

	public Integer getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(Integer newPosition) {
		this.newPosition = newPosition;
	}
	
	protected Locale getPreferredLocale(){
		Locale preferredLocale = (Locale)getContext().getRequest().getSession()
			.getAttribute(SessionLocalePicker.LOCALE_SESSION_ATTR);
		if(preferredLocale==null){
			preferredLocale = getContext().getRequest().getLocale();
		}
		return preferredLocale;
	}
	
}
