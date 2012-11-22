package com.siberhus.ngai.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.exception.StripesRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.annot.InjectJndi;
import com.siberhus.ngai.config.CoreConfig;
import com.siberhus.ngai.exception.NgaiException;
import com.siberhus.org.stripesstuff.stripersist.Stripersist;

public class Ngai {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Ngai.class);
	
	private StringMap ngaiProperties;
	
	private CoreConfig configuration;
	
	private Map<Class<? extends ActionBean>, String> actionBeanPathMap 
		= new HashMap<Class<? extends ActionBean>, String>();
	
	private Context namingContext;
	
	private Map<Class<?>, Object> singletonServiceMap = new HashMap<Class<?>, Object>();

	private Map<Class<?>, Object> singletonDaoMap = new HashMap<Class<?>, Object>();

	private Map<Class<? extends IResourceFactory>, IResourceFactory> resourceFactoryMap 
		= new ConcurrentHashMap<Class<? extends IResourceFactory>, IResourceFactory>();

	private Map<String, Object> resourceMap = new ConcurrentHashMap<String, Object>();
	
	private static Ngai instance;
	
	protected Ngai(StringMap ngaiProps) throws NgaiException {
		
		this.ngaiProperties = ngaiProps;
		
		configuration = new CoreConfig(ngaiProps);	
		
		try {
			namingContext = new InitialContext();
		} catch (NamingException e) {
			LOGGER.error("Fail to instantiated InitialContext. "+
					InjectJndi.class+" is disabled.", e);
		}
		
		synchronized(this){
			if(Ngai.instance == null){
				Ngai.instance = this;
			}else{
				throw new IllegalStateException("User cannot create this instance directly");
			}
		}
	}
	
	public static Ngai getInstance(){
		return instance;
	}
	
	public static void requestInit(){
		Stripersist.requestInit();
	}
	
	public static void requestComplete(){
		Stripersist.requestComplete();
	}
	
	public static EntityManager getEntityManager(String persistenceUnit) {
		if (persistenceUnit != null) {
			return Stripersist.getEntityManager(persistenceUnit);
		} else {
			return Ngai.getEntityManager();
		}
	}
	
	public static EntityManager getEntityManager() {
		String defaultPu = CoreConfig.get().getDefaultPersistenceUnit();
		if (defaultPu == null) {
			return Stripersist.getEntityManager();
		}
		return Stripersist.getEntityManager(defaultPu);
	}

	public static EntityManagerFactory getEntityManagerFactory(
			String persistenceUnit) {
		if (persistenceUnit != null) {
			return Stripersist.getEntityManagerFactory(persistenceUnit);
		} else {
			return Ngai.getEntityManagerFactory();
		}
	}

	public static EntityManagerFactory getEntityManagerFactory() {

		String defaultPu = CoreConfig.get().getDefaultPersistenceUnit();
		if (defaultPu == null) {
			throw new StripesRuntimeException(
					"In order to get EntityManagerFactory without persistenceUnitName you must"
							+ " set the value for Stripersitence.defaultPersistenceUnitName");
		}
		return Stripersist.getEntityManagerFactory(defaultPu);
	}
	
	public static void beginTransactionSafely() {

		beginTransactionSafely(CoreConfig.get().getDefaultPersistenceUnit());
	}

	public static void beginTransactionSafely(String persistenceUnit) {

		EntityManager entityManager = Ngai.getEntityManager();
		beginTransactionSafely(entityManager);
	}

	public static void beginTransactionSafely(EntityManager entityManager) {
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
	}
	
	public StringMap getNgaiProperties(){
		return ngaiProperties;
	}
	
	public CoreConfig getConfiguration(){
		return configuration;
	}
	
	public Map<Class<? extends ActionBean>, String> getActionBeanPathMap() {
		return actionBeanPathMap;
	}

	public Context getNamingContext() {
		return namingContext;
	}
	
	public Map<Class<?>, Object> getSingletonServiceMap() {
		return singletonServiceMap;
	}
	
	public Map<Class<?>, Object> getSingletonDaoMap() {
		return singletonDaoMap;
	}
	
	public Map<Class<? extends IResourceFactory>, IResourceFactory> getResourceFactoryMap() {
		return resourceFactoryMap;
	}

	public Map<String, Object> getResourceMap() {
		return resourceMap;
	}
	
	
}
