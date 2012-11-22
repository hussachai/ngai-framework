package com.siberhus.ngai.localization.dao.impl;

import java.util.List;

import com.siberhus.ngai.dao.AbstractCrudDao;
import com.siberhus.ngai.localization.dao.IResourceBundleDao;
import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.model.ResourceType;
import com.siberhus.ngai.util.JpaQueryUtil;

public class ResourceBundleDao extends AbstractCrudDao<NgaiResourceBundle, Long> implements IResourceBundleDao {

	@Override
	public Class<NgaiResourceBundle> getModelClass() {
		return NgaiResourceBundle.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NgaiResourceBundle> getDefaultMessages(){
		return (List<NgaiResourceBundle>)JpaQueryUtil.getResultListFromNamedQuery(getEntityManager()
				, "ResourceBundle.getDefaultMessages");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NgaiResourceBundle> findResourceBundles(ResourceType type, String language) {
		
		return (List<NgaiResourceBundle>)JpaQueryUtil.getResultListFromNamedQuery(getEntityManager()
				, "ResourceBundle.findByTypeAndLanguage", type, language);
	}
	
	@Override
	public NgaiResourceBundle getResourceBundle(ResourceType type, String language,
			String actionBeanName, String key) {
		if(actionBeanName==null){
			return (NgaiResourceBundle)JpaQueryUtil.getFirstResultFromNamedQuery(getEntityManager()
					, "ResourceBundle.getGlobalResourceBundle", type, language, key);
		}
		return (NgaiResourceBundle)JpaQueryUtil.getFirstResultFromNamedQuery(getEntityManager()
				, "ResourceBundle.getResourceBundle", type, language, actionBeanName, key);
	}
	
}
