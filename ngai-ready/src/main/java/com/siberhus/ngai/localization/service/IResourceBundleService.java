package com.siberhus.ngai.localization.service;

import java.util.ResourceBundle;

import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.service.ICrudService;

public interface IResourceBundleService extends ICrudService<NgaiResourceBundle, Long>{
	
	public ResourceBundle getFieldBundle(String lang);
	
	public ResourceBundle getMessageBundle(String lang);
	
	public void clearFieldBundleCache(String lang);
	
	public void clearMessageBundleCache(String lang);
	
	public NgaiResourceBundle findFieldBundle(String language, String actionBeanName, String key);
	
	public NgaiResourceBundle findMessageBundle(String language, String actionBeanName, String key);
	
}
