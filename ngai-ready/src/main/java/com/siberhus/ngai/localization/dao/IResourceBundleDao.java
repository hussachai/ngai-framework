package com.siberhus.ngai.localization.dao;

import java.util.List;

import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.model.ResourceType;


public interface IResourceBundleDao extends ICrudDao<NgaiResourceBundle, Long>{
	
	public List<NgaiResourceBundle> getDefaultMessages();
	
	public List<NgaiResourceBundle> findResourceBundles(ResourceType type, String language);
	
	public NgaiResourceBundle getResourceBundle(ResourceType type, String language
			, String actionBeanName, String key);
	
	
}
