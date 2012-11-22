package com.siberhus.ngai.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.RuntimeConfiguration;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.UrlBindingFactory;
import net.sourceforge.stripes.exception.ExceptionHandler;
import net.sourceforge.stripes.format.FormatterFactory;
import net.sourceforge.stripes.tag.BeanFirstPopulationStrategy;
import net.sourceforge.stripes.tag.PopulationStrategy;
import net.sourceforge.stripes.util.ResolverUtil;
import net.sourceforge.stripes.util.StringUtil;
import net.sourceforge.stripes.validation.TypeConverterFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.lang.AnnotatedAttributeUtils;
import com.siberhus.commons.properties.PropertiesUtil;
import com.siberhus.commons.util.StringMap;
import com.siberhus.commons.util.StrKeyMap.TRIM;
import com.siberhus.ngai.AppVariable;
import com.siberhus.ngai.annot.DaoBean;
import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.annot.ServiceBean;
import com.siberhus.ngai.converter.NgaiTypeConverterFactory;
import com.siberhus.ngai.core.DaoBeanRegistry;
import com.siberhus.ngai.core.INgaiBootstrap;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.core.NgaiCoreInterceptor;
import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.dao.IDao;
import com.siberhus.ngai.exception.NgaiRuntimeException;
import com.siberhus.ngai.exception.TraceableExceptionHandler;
import com.siberhus.ngai.format.NgaiFormatterFactory;
import com.siberhus.ngai.service.IService;
import com.siberhus.org.stripesstuff.stripersist.EntityFormatter;
import com.siberhus.org.stripesstuff.stripersist.EntityTypeConverter;

public class NgaiConfiguration extends RuntimeConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NgaiConfiguration.class);
	
	public static final String NGAI_INLINE_PROPERTIES = "Ngai.InlineProperties";	
	public static final String NGAI_PROPERTIES = "Ngai.Properties";	
	public static final String NGAI_BOOTSTRAP_CLASSES = "NgaiBootstrap.Classes";	
	public static final String SERVICE_RESOLVER_PACKAGES = "ServiceResolver.Packages";	
	public static final String DAO_RESOLVER_PACKAGES = "DaoResolver.Packages";	
	public static final String NGAI_INTERCEPTOR_CLASSES = "NgaiInterceptor.Classes";
	
	//=====================================================================//
	
	static {
		Package pkg = NgaiConfiguration.class.getPackage();
		LOGGER.info("\r\n##################################################"+
				"\r\n# Stripersist Version: {}, Build: {}"+
				"\r\n# Ngai Version: {}"+
				"\r\n##################################################",
				new Object[]{1.0,105,pkg.getImplementationVersion()});
	}
	
	private StringMap ngaiProperties;
	
	private NgaiCoreInterceptor ngaiCoreInterceptor;
	
	@Override
    public void init() {
    	
		try {
			ngaiProperties = loadNgaiProperties();
			ngaiProperties.setUnmodifiable();
			new Ngai(ngaiProperties){
				//create a single Ngai instance
			};
			
			CoreConfig coreConfig = CoreConfig.get();
			AppVariable appVar = new AppVariable();
			appVar.setLocale(coreConfig.getLocale());
			appVar.setMode(coreConfig.getMode());
			appVar.setFormatPattern(coreConfig.getFormatPattern());
			getServletContext().setAttribute(coreConfig.getAppVariableName(), appVar);
			
			//===================================//
			ngaiCoreInterceptor = new NgaiCoreInterceptor();
			URL url = Thread.currentThread().getContextClassLoader().getResource("/META-INF/persistence.xml");
			// url may be null if using ant/junit. if it is null we'll try a
			// different classloader - thanks freddy!
			if (url == null)
				url = getClass().getResource("/META-INF/persistence.xml");
			LOGGER.debug("Reading persistence.xml from ", url, ".");
			ngaiCoreInterceptor.init(url);
			//===================================//
			
			super.init();
			initTypeFormatterAndConverter();
			
			registerDaoBeans();
			registerServiceBeans();
			scanActionBeans();
			initBootstrapClasses();
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		} 
    }
    
	
	@Override
	protected Map<LifecycleStage, Collection<Interceptor>> initCoreInterceptors() {
		Map<LifecycleStage, Collection<Interceptor>> map = super.initCoreInterceptors();
		NgaiCoreInterceptor ngaiCoreInterceptor = new NgaiCoreInterceptor();
		addInterceptor(map, ngaiCoreInterceptor);
		return map;
	}
	
	protected void initTypeFormatterAndConverter(){
		
		getFormatterFactory().add(Entity.class, EntityFormatter.class);
		getFormatterFactory().add(MappedSuperclass.class, EntityFormatter.class);
		
		getTypeConverterFactory().add(Entity.class, EntityTypeConverter.class);
		getTypeConverterFactory().add(MappedSuperclass.class, EntityTypeConverter.class);
	}
	
    protected StringMap loadNgaiProperties() throws IOException{
    	
    	StringMap cfgMap = new StringMap();
		cfgMap.setKeyCaseMode(null);//disable key transformation
		cfgMap.setKeyTrimMode(TRIM.TO_NULL);
    	cfgMap.setValueTrimMode(TRIM.TO_NULL);
    	
    	Properties props = null;
    	String propsString = getBootstrapPropertyResolver()
			.getProperty(NGAI_INLINE_PROPERTIES);
    	if(propsString!=null){
    		props = new Properties();
			props.load(new ByteArrayInputStream(propsString.getBytes()));
    	}else{
    		String propsFile = getBootstrapPropertyResolver().getProperty(NGAI_PROPERTIES);
    		if(propsFile!=null){
    			props = PropertiesUtil.create(propsFile);
    		}else{
    			throw new NgaiRuntimeException("Stripes Filter Parameter '"+
    					NGAI_INLINE_PROPERTIES+"' or '"+NGAI_PROPERTIES+
    					"' must be defined in web.xml");
    		}
    	}
    	for(Map.Entry<Object, Object> entry : props.entrySet()){
			String name = (String)entry.getKey();
			String value = (String)entry.getValue();
			cfgMap.put(name, value);
		}
    	return cfgMap;
    }
    
    protected void registerDaoBeans() throws Exception{
		
		String daoPackagesParam = getBootstrapPropertyResolver()
				.getProperty(DAO_RESOLVER_PACKAGES);
		daoPackagesParam = StringUtils.trimToNull(daoPackagesParam);
		
		String[] daoPackages = StringUtil.standardSplit(daoPackagesParam);
		
		ResolverUtil<Object> daoResolver = new ResolverUtil<Object>();
		
		LOGGER.debug("Resolving all DAO classes that are implemented by INgaiDao interface in pacakages: {}",
					daoPackagesParam);
		daoResolver.findImplementations(IDao.class, daoPackages);
		
		LOGGER.debug("Resolving all DAO classes that are annotated by NgaiDao annotation in packages: {}",
						daoPackagesParam);
		daoResolver.findAnnotated(DaoBean.class, daoPackages);
		
		try {
			
			for (Class<?> daoClass : daoResolver.getClasses()) {
				
				DaoBeanRegistry.register(daoClass);
			}
		} catch (Throwable e) {
			
			throw new NgaiRuntimeException("DAO Initializing Failed",e);
		}
	}
	
    protected void registerServiceBeans() throws Exception{
    	
    	String servicePackagesParam = getBootstrapPropertyResolver()
		.getProperty(SERVICE_RESOLVER_PACKAGES);
		servicePackagesParam = StringUtils.trimToNull(servicePackagesParam);
		
		String[] servicePackages = StringUtil.standardSplit(servicePackagesParam);
		
		ResolverUtil<Object> serviceResolver = new ResolverUtil<Object>();
		LOGGER.debug("Resolving all service classes that are implemented by INgaiService interface in packages: {}",
				servicePackagesParam);
		serviceResolver.findImplementations(IService.class, servicePackages);
		
		LOGGER.debug("Resolving all service classes that are annotated by NgaiService annotation in packages: {}",
				servicePackagesParam);
		serviceResolver.findAnnotated(ServiceBean.class, servicePackages);
		
		try {
			for (Class<?> serviceClass : serviceResolver.getClasses()) {
				
				ServiceBeanRegistry.register(serviceClass);
			}
			
		} catch (Throwable e) {
			
			throw new NgaiRuntimeException("Service Initializing Failed",e);
		}
    }
    
    protected void scanActionBeans(){
    	
    	Map<String, Class<? extends ActionBean>> pathMap = UrlBindingFactory
			.getInstance().getPathMap();
    	
		for (Map.Entry<String, Class<? extends ActionBean>> entry : pathMap.entrySet()) {
			
			String path = entry.getKey();
			Class<? extends ActionBean> actionBeanClass = entry.getValue();
		
			String contextPath = getServletContext().getContextPath();
			int variablePos = path.indexOf("{");
			
			if (variablePos != -1) {
				path = path.substring(0, variablePos);
			}
			if (path.endsWith("/")) {
				path = contextPath + path.substring(0, path.length() - 1);
			}else{
				path = contextPath + path;
			}
			
			Ngai.getInstance().getActionBeanPathMap().put(actionBeanClass, path);
			
			AnnotatedAttributeUtils.inspectAttribute(InjectService.class,actionBeanClass);
			AnnotatedAttributeUtils.inspectAttribute(InjectDao.class,actionBeanClass);
		}
    }
    
    protected void initBootstrapClasses() throws Exception{
    	String bootstrapParam = getBootstrapPropertyResolver()
		.getProperty(NGAI_BOOTSTRAP_CLASSES);
		bootstrapParam = StringUtils.trimToNull(bootstrapParam);
		
		if(bootstrapParam!=null){
			
			String[] bootstrapClassNames = StringUtil.standardSplit(bootstrapParam);
			
			try {
				Ngai.requestInit();
				for (String bootstrapClassName : bootstrapClassNames) {
					
					Class<?> bootstrapClass = Class.forName(bootstrapClassName);
					INgaiBootstrap bootstrap = (INgaiBootstrap) bootstrapClass.newInstance();
					bootstrap.execute(this);
				}
			}finally{
				Ngai.requestComplete();
			}
		}
    }
    
	/** Looks for a class name in config and uses that to create the component.
	 * Ngai prefer BeanFirstPopulationStrategy over DefaultPopulationStrategy
	 * */
	@Override
	protected PopulationStrategy initPopulationStrategy() {
		
		PopulationStrategy component = null;
		component = initializeComponent(PopulationStrategy.class, POPULATION_STRATEGY);
		if(component==null){
			component = new BeanFirstPopulationStrategy();
			try{
				component.init(this);
			}catch(Exception e){}
		}
		if( !(component instanceof BeanFirstPopulationStrategy) ){
			LOGGER.warn("Ngai may work improperly if PopulationStrategy.Class is not BeanFirstPopulationStrategy");
		}
		return component;
	}


	@Override
	protected TypeConverterFactory initTypeConverterFactory() {
		TypeConverterFactory factory = super.initTypeConverterFactory();
		if(factory!=null){
			return factory;
		}
		factory = new NgaiTypeConverterFactory();
		try {
			factory.init(this);
		} catch (Exception e) {
			throw new NgaiRuntimeException(e);
		}
		return factory;
	}


	@Override
	protected FormatterFactory initFormatterFactory() {
		FormatterFactory factory = super.initFormatterFactory();
		if(factory!=null){
			return factory;
		}
		factory = new NgaiFormatterFactory();
		try {
			factory.init(this);
		} catch (Exception e) {
			throw new NgaiRuntimeException(e);
		}
		return factory;
	}
	
	/** Looks for a class name in config and uses that to create the component. */
    @Override 
    protected ExceptionHandler initExceptionHandler() {
        ExceptionHandler handler = super.initExceptionHandler();
        if(handler!=null){
        	return handler;
        }
        handler = new TraceableExceptionHandler();
        try {
        	handler.init(this);
		} catch (Exception e) {
			throw new NgaiRuntimeException(e);
		}
        return handler;
    }
    
}
