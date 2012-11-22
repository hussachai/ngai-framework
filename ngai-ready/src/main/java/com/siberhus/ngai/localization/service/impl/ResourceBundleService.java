package com.siberhus.ngai.localization.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.annot.ServiceBean;
import com.siberhus.ngai.dao.ICrudDao;
import com.siberhus.ngai.localization.MapResourceBundle;
import com.siberhus.ngai.localization.dao.IResourceBundleDao;
import com.siberhus.ngai.localization.dao.impl.ResourceBundleDao;
import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.model.ResourceType;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.service.AbstractCrudDaoService;


@ServiceBean(scope=Scope.Singleton)
public class ResourceBundleService extends AbstractCrudDaoService<NgaiResourceBundle, Long> 
	implements IResourceBundleService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleService.class);
	
	@InjectDao(implementation=ResourceBundleDao.class)
	private IResourceBundleDao resourceBundleDao;
	
	private Map<String, MapResourceBundle> fieldBundleCache = new ConcurrentHashMap<String, MapResourceBundle>();
	
	private Map<String, MapResourceBundle> messageBundleCache = new ConcurrentHashMap<String, MapResourceBundle>();
	
	@Override
	public ResourceBundle getFieldBundle(String lang) {
		return getResourceBundle(fieldBundleCache, ResourceType.FIELD, lang);
	}
	
	@Override
	public void clearFieldBundleCache(String lang) {
		fieldBundleCache.remove(lang);
	}
	
	@Override
	public ResourceBundle getMessageBundle(String lang) {
		return getResourceBundle(messageBundleCache, ResourceType.MESSAGE, lang);
	}
	
	@Override
	public void clearMessageBundleCache(String lang) {
		messageBundleCache.remove(lang);
	}
	
	@Override
	public ICrudDao<NgaiResourceBundle, Long> getCrudDao() {
		return resourceBundleDao;
	}
	
	@Override
	public void save(NgaiResourceBundle model) {
		
		getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		NgaiResourceBundle msgBundle = resourceBundleDao.getResourceBundle(model.getType()
				, model.getLanguage(), model.getActionBeanName()
				, model.getKey());
		if(msgBundle!=null){
			msgBundle.setType(model.getType());
			msgBundle.setLanguage(model.getLanguage());
			msgBundle.setActionBeanName(model.getActionBeanName());
			msgBundle.setKey(model.getKey());
			msgBundle.setValue(model.getValue());
			getEntityManager().merge(msgBundle);
		}else{
			getEntityManager().merge(model);
		}
		getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
		
		Map<String, MapResourceBundle> resourceBundleCache = null;
		if(ResourceType.MESSAGE==model.getType()){
			resourceBundleCache = messageBundleCache;
		}else if(ResourceType.FIELD==model.getType()){
			resourceBundleCache = fieldBundleCache;
		}else{
			//unknown 
			return;
		}
		
		MapResourceBundle resBundle = resourceBundleCache.get(model.getLanguage());
		if(resBundle!=null){
			if(!resBundle.containsKey(model.getKey())){
				LOGGER.warn("key: {} does not exist.",model.getKey());
			}
			resBundle.put(model.getKey(), model.getValue());
		}
	}
	
	@Override
	public void save(NgaiResourceBundle model, Serializable user) {
		save(model);
	}
	
	@Override
	public NgaiResourceBundle findFieldBundle(String language, String actionBeanName, String key){
		if(language==null){
			throw new IllegalArgumentException("language is required");
		}
		if(key==null){
			throw new IllegalArgumentException("key is required");
		}
		return resourceBundleDao.getResourceBundle(ResourceType.FIELD, language, actionBeanName, key);
	}
	
	@Override
	public NgaiResourceBundle findMessageBundle(String language, String actionBeanName, String key){
		if(language==null){
			throw new IllegalArgumentException("language is required");
		}
		if(key==null){
			throw new IllegalArgumentException("key is required");
		}
		return resourceBundleDao.getResourceBundle(ResourceType.MESSAGE, language, actionBeanName, key);
	}
	
	protected ResourceBundle getResourceBundle(Map<String, MapResourceBundle> resourceBundleCache, 
			ResourceType type, String language){
		
		MapResourceBundle resBundle = resourceBundleCache.get(language);
		
		if(resBundle==null){
			
			Map<String, String> resMap = new HashMap<String, String>();
			
			List<NgaiResourceBundle> rbList = resourceBundleDao.findResourceBundles(type, language);
			
			if(type==ResourceType.MESSAGE){
				if(rbList.size()==0){
					LOGGER.warn("Message Resource not found for language: {}", language);
					rbList = resourceBundleDao.getDefaultMessages();
				}
			}
			for(NgaiResourceBundle rb : rbList){
				String key = "";
				if(StringUtils.isNotBlank(rb.getActionBeanName())){
					key = rb.getActionBeanName()+".";
				}
				key += rb.getKey();
				resMap.put(key, rb.getValue());
			}
			
			resBundle = new MapResourceBundle(resMap);
			resourceBundleCache.put(language, resBundle);
		}
		
		return resBundle;
	}
	
	
}
