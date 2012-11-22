package com.siberhus.ngaiready.admin.action;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.UrlBindingFactory;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.localization.action.Messages;
import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.model.ResourceType;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.localization.service.impl.ResourceBundleService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/resourceBundle")
public class ResourceBundleAction extends BaseCrudAction<NgaiResourceBundle, Long>{
	
	@InjectService(implementation=ResourceBundleService.class)
	private IResourceBundleService resourceBundleService;
	
	@ValidateNestedProperties({
		@Validate(on="save", field="type", required=true),
		@Validate(on="save", field="language", required=true, mask="[a-z]{2}"),
		@Validate(on="save", field="key", required=true)
	})
	private NgaiResourceBundle model;
	
	//========== AJAX Parameters ==========================//
	@Validate(on={"ajaxGetFieldValue","ajaxSaveFieldValue","ajaxClearFieldBundleCache"}
		, required=true,minlength=2,maxlength=3)
	private String language;
	
	private String actionBeanName;
	
	@Validate(on={"ajaxGetFieldValue","ajaxSaveFieldValue"},required=true)
	private String key;
	
	private boolean global;
	
	@Validate(on="ajaxSaveFieldValue",required=true)
	private String value;
	//======================================================//
	
	@Override
	public ICrudService<NgaiResourceBundle, Long> getCrudService() {
		return resourceBundleService;
	}
	
	@Override
	public NgaiResourceBundle getModel() {
		return model;
	}
	
	@Override
	public void setModel(NgaiResourceBundle model) {
		this.model = model;
	}
	
	@Override
	public Class<NgaiResourceBundle> getModelClass() {
		return NgaiResourceBundle.class;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/resource-bundle";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from ResourceBundle rb where 1=1 ");
		Messages.addLocalizedInfo(this, "admin.info.searchSuccessful", "1","2");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getLanguage()!=null){
			qto.appendQuery("and rb.language=?");
			qto.addParameter(getModel().getLanguage());
		}
		if(getModel().getActionBeanName()!=null){
			qto.appendQuery("and rb.actionBeanName like ?");
			qto.addParameter(getModel().getActionBeanName());
		}
		if(getModel().getKey()!=null){
			qto.appendQuery("and rb.key like ?");
			qto.addParameter(getModel().getKey());
		}
		if(getModel().getType()!=null){
			qto.appendQuery("and rb.type=?");
			qto.addParameter(getModel().getType());
		}
		if(getModel().getValue()!=null){
			qto.appendQuery("and rb.value like ?");
			qto.addParameter(getModel().getValue());
		}
		return qto;
	}
	
	@Before(on={"index","edit","create"})
	protected void populateActionBeanNameSet(){
		
		Set<String> actionBeanNameSet = new TreeSet<String>();
		for(Class<? extends ActionBean> actionBeanClass : UrlBindingFactory
				.getInstance().getActionBeanClasses()){
			actionBeanNameSet.add(actionBeanClass.getName());
		}
		getContext().getRequest().setAttribute("actionBeanNameSet", actionBeanNameSet);
	}
	
	public Resolution ajaxGetFieldValue(){
		NgaiResourceBundle msgBundle = null;
		msgBundle = resourceBundleService.findFieldBundle(getLanguage()
				, getActionBeanName(), getKey());
		if(msgBundle == null){
			msgBundle = resourceBundleService.findFieldBundle(getLanguage()
					, null, getKey());
		}
		if(msgBundle != null){
			return new StreamingResolution("text/html",msgBundle.getValue());
		}
		return new StreamingResolution("text/html","");
	}
	
	public Resolution ajaxSaveFieldValue(){
		NgaiResourceBundle msgBundle = new NgaiResourceBundle();
		msgBundle.setType(ResourceType.FIELD);
		msgBundle.setLanguage(getLanguage());
		if(! isGlobal()){
			msgBundle.setActionBeanName(getActionBeanName());
		}
		msgBundle.setKey(getKey());
		msgBundle.setValue(getValue());
		resourceBundleService.save(msgBundle);
		return new StreamingResolution("text/plain","Save successful! Refresh your page to see the changes.");
	}
	
	@DontValidate
	public Resolution ajaxClearFieldBundleCache(){
		try{
			resourceBundleService.clearFieldBundleCache(getLanguage());
			return new StreamingResolution("text/plain", "Cache was deleted!");
		}catch(Exception e){
			return new StreamingResolution("text/plain", "error: "
					+ExceptionUtils.getRootCauseMessage(e));
		}
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getActionBeanName() {
		return actionBeanName;
	}

	public void setActionBeanName(String actionBeanName) {
		this.actionBeanName = actionBeanName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
