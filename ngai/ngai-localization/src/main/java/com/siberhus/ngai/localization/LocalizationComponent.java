package com.siberhus.ngai.localization;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.localization.DefaultLocalizationBundleFactory;
import net.sourceforge.stripes.localization.LocalizationBundleFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.core.DaoBeanRegistry;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.core.WebContext;
import com.siberhus.ngai.localization.dao.impl.ResourceBundleDao;
import com.siberhus.ngai.localization.model.NgaiResourceBundle;
import com.siberhus.ngai.localization.model.ResourceType;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.localization.service.impl.ResourceBundleService;
import com.siberhus.ngai.util.JpaQueryUtil;

@Intercepts(LifecycleStage.ActionBeanResolution)
public class LocalizationComponent implements Interceptor, LocalizationBundleFactory {
	
	public static final String MESSAGE_BUNDLE_ENABLED = "localization.messageBundleEnabled";
	public static final String FIELD_BUNDLE_ENABLED = "localization.fieldBundleEnabled";
	public static final String JSTL_INTERCEPTOR_ENABLED = "localization.jstlInterceptorEnabled";
	public static final String INSTALL_MESSAGE_FILES = "localization.installMessageFiles";
	public static final String MESSAGES_FILE_BASENAME = "localization.messageFileBasename";
	/** If this variable is empty, languageMap will not be created. default value is 'languageMap' **/
	public static final String LANGUAGE_MAP_VARIABLE = "localization.languageMapVariable";
	
	private DefaultLocalizationBundleFactory defaultLocalizationBundleFactory;
	
	private boolean messageBundleEnabled = false; 
	private boolean fieldBundleEnabled = true;
	private boolean jstlInterceptorEnabled = true;
	
	private final Logger LOGGER = LoggerFactory.getLogger(LocalizationComponent.class);
	
	@Override
	public void init(Configuration config) throws Exception{
		
		//Registering Service and DAO Bean
		ServiceBeanRegistry.register(ResourceBundleService.class);
		DaoBeanRegistry.register(ResourceBundleDao.class);
		
		StringMap ngaiProps = Ngai.getInstance().getNgaiProperties();
		
		messageBundleEnabled = ngaiProps.get(Boolean.class, MESSAGE_BUNDLE_ENABLED, messageBundleEnabled);
		fieldBundleEnabled = ngaiProps.get(Boolean.class, FIELD_BUNDLE_ENABLED, fieldBundleEnabled);
		jstlInterceptorEnabled = ngaiProps.get(Boolean.class, JSTL_INTERCEPTOR_ENABLED, jstlInterceptorEnabled);
		
		defaultLocalizationBundleFactory = new DefaultLocalizationBundleFactory();
		defaultLocalizationBundleFactory.init(config);
		
		setLanguageMapVariable(config, ngaiProps);
		installMessageFiles(config, ngaiProps);
		
	}
	
	@Override
	public ResourceBundle getErrorMessageBundle(Locale locale)
			throws MissingResourceException {
		if(messageBundleEnabled){
			return getResourceBundleService().getMessageBundle(locale.getLanguage());
		}
		
		return defaultLocalizationBundleFactory.getErrorMessageBundle(locale);
	}
	
	@Override
	public ResourceBundle getFormFieldBundle(Locale locale)
			throws MissingResourceException {
		if(fieldBundleEnabled){
			return getResourceBundleService().getFieldBundle(locale.getLanguage());
		}
		
		return defaultLocalizationBundleFactory.getFormFieldBundle(locale);
	}
	
	@Override
	public Resolution intercept(ExecutionContext context) throws Exception {
		if(!jstlInterceptorEnabled){
			return context.proceed();
		}
		//intercept JSTL
		HttpServletRequest request = context.getActionBeanContext().getRequest();
		Locale locale = request.getLocale();
		ResourceBundle bundle = null;
		if(fieldBundleEnabled){
			bundle = getFormFieldBundle(locale);
		}else{
			bundle = defaultLocalizationBundleFactory.getFormFieldBundle(locale);
		}
		Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, new LocalizationContext(bundle, locale));
		return context.proceed();
	}
	
	protected IResourceBundleService getResourceBundleService(){
		
		return (IResourceBundleService)ServiceBeanRegistry
			.get(WebContext.DUMMY_WEB_CONTEXT, IResourceBundleService.class, 
					ResourceBundleService.class);
	}
	
	protected void setLanguageMapVariable(Configuration config, StringMap ngaiProps){
		LOGGER.info("Setting up language map variable");
		String languaugeMapVariable = "languageMap";
		languaugeMapVariable = ngaiProps.getString(LANGUAGE_MAP_VARIABLE, languaugeMapVariable);
		if(languaugeMapVariable==null){
			LOGGER.info("{} is null. No language map variable in web context!",LANGUAGE_MAP_VARIABLE);
		}else{
			LOGGER.info("{} value is {}",LANGUAGE_MAP_VARIABLE, languaugeMapVariable);
		}
		Map<String, String> langNameCodeMap = new TreeMap<String, String>();
		for(Locale locale : Locale.getAvailableLocales()){
			
			langNameCodeMap.put(locale.getDisplayLanguage(locale)
					, locale.getLanguage());
		}
		config.getServletContext().setAttribute(languaugeMapVariable, langNameCodeMap);
	}
	
	protected void installMessageFiles(Configuration config, StringMap ngaiProps) throws IOException {
		LOGGER.info("Installing localized message files");
		boolean installMessageFiles = true;
		String messagesFileBasename = "DefaultMessages";
		
		installMessageFiles = ngaiProps.get(Boolean.class, INSTALL_MESSAGE_FILES, installMessageFiles);
		messagesFileBasename = ngaiProps.getString(MESSAGES_FILE_BASENAME, messagesFileBasename);
		
		if(!installMessageFiles){
			LOGGER.info("Localized message file installation is disabed");
			return;
		}
		LOGGER.info("Message file basename is {}", messagesFileBasename);
		
		Ngai.requestInit();
		Ngai.getEntityManager().getTransaction().begin();//TODO: Manual Trx will be removed in 1.0
		if(!messagesFileBasename.startsWith("/")){
			messagesFileBasename = "/"+messagesFileBasename;
		}
		if(messagesFileBasename.endsWith(".properties")){
			throw new IllegalArgumentException(MESSAGES_FILE_BASENAME+" cannot ends with .properties");
		}
		
		InputStream resStream = LocalizationComponent.class
			.getResourceAsStream(messagesFileBasename+".properties");
		if(resStream==null){
			LOGGER.warn("Default message file '{}.properties' not found", messagesFileBasename);
		}else{
			storeMessageFile(resStream, null);
		}
		
		Set<String> langSet = new HashSet<String>();
		for(Locale locale : Locale.getAvailableLocales()){
			langSet.add(locale.getLanguage());
		}
		
		for(String language : langSet ){
			String messageFile = messagesFileBasename+"_"+language+".properties";
			LOGGER.debug("Looking up message file {} in classpath", messageFile);
			resStream = LocalizationComponent.class.getResourceAsStream(messageFile);
			if(resStream!=null){
				LOGGER.info("Message file: {} was FOUND !", messageFile);
				storeMessageFile(resStream, language);
			}else{
				LOGGER.debug("Message file: {} was *NOT* found!", messageFile);
			}
		}
		
		Ngai.getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
		Ngai.requestComplete();
	}
	
	protected void storeMessageFile(InputStream resStream, String language) throws IOException {
		
		EntityManager em = Ngai.getEntityManager();
		String query = "from ResourceBundle rb where rb.type='MESSAGE' and rb.key=? ";
		if(language==null){
			//in case of default message
			query += "and rb.language is null";
		}else{
			query += "and rb.language = '"+language+"'";
		}
		Properties msgProps = new Properties();
		msgProps.load(resStream);
		for(String propName : msgProps.stringPropertyNames()){
			NgaiResourceBundle rb = null;
			String key = propName;
			String value = msgProps.getProperty(key);
			rb = (NgaiResourceBundle)JpaQueryUtil.getFirstResultFromQuery(em, query , key);
			if(rb==null){
				rb = new NgaiResourceBundle();
				rb.setLanguage(language);
				rb.setType(ResourceType.MESSAGE);
				rb.setKey(key);
				rb.setValue(value);
				LOGGER.debug("Creating new message language={} key={} value={}", new Object[]{language, key, value});
				em.persist(rb);
			}else{
				if(!StringUtils.equals(value, rb.getValue())){
					LOGGER.debug("Updating existing message language={} key={} oldvalue={} newvalue={} "
							, new Object[]{language, key, rb.getValue(), value});
					rb.setValue(value);
					em.merge(rb);
				}
			}
		}
		resStream.close();
	}
	
}
