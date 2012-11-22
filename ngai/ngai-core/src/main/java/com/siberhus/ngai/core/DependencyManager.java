/* Copyright 2009 Hussachai Puripunpinyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siberhus.ngai.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.lang.AnnotatedAttributeUtils;
import com.siberhus.commons.lang.AnnotatedAttributeUtils.AnnotatedAttribute;
import com.siberhus.ngai.annot.DaoBean;
import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.InjectHttpObject;
import com.siberhus.ngai.annot.InjectJndi;
import com.siberhus.ngai.annot.InjectResource;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.annot.ServiceBean;
import com.siberhus.ngai.dao.IDao;
import com.siberhus.ngai.exception.DaoException;
import com.siberhus.ngai.exception.NgaiRuntimeException;
import com.siberhus.ngai.exception.ServiceException;
import com.siberhus.ngai.service.IService;

/**
 * 
 * @author hussachai
 * @since 0.9
 */
public class DependencyManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DependencyManager.class);
	
	protected static void inject(WebContext webContext, Class<?> targetClass
			, Object implObject ,Scope scope) throws Exception{
		
		List<AnnotatedAttribute> annotatedAttribList = AnnotatedAttributeUtils.getAnnotatedAttributes(targetClass);
		
		if(annotatedAttribList==null){
			return;
		}
		
		for(AnnotatedAttribute annotatedAttrib : annotatedAttribList){
			Annotation annot = annotatedAttrib.getAnnotation();
			Class<?> annotType = annot.annotationType();
			Class<?> annotatedType = annotatedAttrib.getType();
			
			if(implObject instanceof Proxy){
				if(implObject instanceof IDao || targetClass.getAnnotation(DaoBean.class)!=null){
					implObject = DaoBeanProxy.getRealObject((Proxy)implObject);
				}else if(implObject instanceof IService || targetClass.getAnnotation(ServiceBean.class)!=null){
					implObject = ServiceBeanProxy.getRealObject((Proxy)implObject);
				}
			}
			
			if(InjectDao.class==annotType){
				
				Object referencedObject = DaoBeanRegistry.get(webContext, annotatedType, 
						((InjectDao)annot).implementation());
				if(referencedObject==null){
					throw new DaoException("DAO class: "+annotatedType+ " was not registered!");
				}
				if(!annotatedType.isInterface()){
					//if annotatedType is not an interface, proxy object will not be able to be set.
					if(referencedObject instanceof Proxy){
						LOGGER.warn("DAO attribute: {} of DAO: {} is not an interface type. Ngai still allow to inject this kind of attribute " +
								"but it's not recommened because query aware feature will be disabled and may cause the bug",
								annotatedType,implObject);
						referencedObject = DaoBeanProxy.getRealObject((Proxy)referencedObject);
						if(referencedObject==null){
							throw new ServiceException("Unkown problem cannot deproxify daoObject");
						}
					}
				}
				annotatedAttrib.set(implObject, referencedObject);
				
				DependencyManager.inject(webContext, referencedObject.getClass(), referencedObject, scope);
				
			}else if(InjectService.class==annotType){
				
				//Is DAO service has a reference to @InjectService?
				if( (implObject instanceof IDao) || targetClass.getAnnotation(DaoBean.class)!=null ){
					throw new DaoException("DAO class: "+targetClass+" cannot refer to service class");
				}
				
				Object referencedService = ServiceBeanRegistry.get(webContext, annotatedType, 
						((InjectService)annot).implementation());
				if(referencedService==null){
					throw new ServiceException("Service class: "+annotatedType+ " was not registered!");
				}
				annotatedAttrib.set(implObject, referencedService);
				
				DependencyManager.inject(webContext, referencedService.getClass(), referencedService, scope);
				
			}else if(InjectJndi.class==annotType){
				
				InjectJndi jndiAnnot = (InjectJndi)annot;
				String jdniName = jndiAnnot.name();
				
				Object resource = Ngai.getInstance().getNamingContext().lookup(jdniName);
				if(jndiAnnot.isRemoteObject()){
					resource = PortableRemoteObject.narrow(resource, annotatedType);
				}
				annotatedAttrib.set(implObject, resource);
				
			}else if(InjectResource.class==annotType){
				
				InjectResource injectAnnot = (InjectResource)annot;
				Class<? extends IResourceFactory> resourceFactoryClass = injectAnnot.factory();
				Map<Class<? extends IResourceFactory>, IResourceFactory> resourceMap = Ngai.getInstance().getResourceFactoryMap();
				IResourceFactory resourceFactory = resourceMap.get(resourceFactoryClass);
				if(resourceFactory==null){
					resourceFactory = resourceFactoryClass.newInstance();
					resourceMap.put(resourceFactoryClass, resourceFactory);
				}
				Object resource = null;
				if(!"".equals(injectAnnot.name())){
					resource = Ngai.getInstance().getResourceMap().get(injectAnnot.name());
					if(resource==null){
						resource = resourceFactory.createResource(injectAnnot.args());
						Ngai.getInstance().getResourceMap().put(injectAnnot.name(),resource);
					}
				}else{
					resource = resourceFactory.createResource(injectAnnot.args());
				}
				annotatedAttrib.set(implObject, resource);
				
			}else if(PersistenceUnit.class==annotType){
					 
				PersistenceUnit puAnnot = (PersistenceUnit)annot;
				String unitName = puAnnot.unitName();
				if("".equals(unitName)){
					annotatedAttrib.set(implObject, Ngai.getEntityManagerFactory());
				}else{
					annotatedAttrib.set(implObject, Ngai.getEntityManagerFactory(unitName));
				}
			}else if(PersistenceContext.class==annotType){
				
				PersistenceContext pcAnnot = (PersistenceContext)annot;
				String unitName = pcAnnot.unitName();
				if("".equals(unitName)){
					EntityManager entityManager = new SharedEntityManagerWrapper();
					annotatedAttrib.set(implObject, entityManager);
				}else{
					EntityManager entityManager = new SharedEntityManagerWrapper(unitName);
					annotatedAttrib.set(implObject, entityManager);
				}
			}else if(InjectHttpObject.class==annotType){
				
				boolean error = false;
				if(ServletRequest.class.isAssignableFrom(targetClass)){
					if(scope!=Scope.Request){
						error = true;
					}
					annotatedAttrib.set(implObject, webContext.getRequest());
				}else if(HttpSession.class.isAssignableFrom(targetClass)){
					if(scope!=Scope.Request && scope!=Scope.Session){
						error = true;
					}
					annotatedAttrib.set(implObject, webContext.getRequest().getSession());
				}else if(ServletResponse.class.isAssignableFrom(targetClass)){
					if(scope!=Scope.Request){
						error = true;
					}
					annotatedAttrib.set(implObject, webContext.getResponse());
				}else if(ServletConfig.class.isAssignableFrom(targetClass)){
					annotatedAttrib.set(implObject, webContext.getRequest()
							.getSession().getServletContext());
				}
				if(error){
					throw new NgaiRuntimeException("@InjectHttpObject is not available in this scope: "+scope);
				}
				
			}
		}
		
	}
}
