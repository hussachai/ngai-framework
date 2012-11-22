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

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.lang.AnnotatedAttributeUtils;
import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.InjectHttpObject;
import com.siberhus.ngai.annot.InjectJndi;
import com.siberhus.ngai.annot.InjectResource;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.annot.PostActivate;
import com.siberhus.ngai.annot.PrePassivate;
import com.siberhus.ngai.annot.Remove;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.annot.ServiceBean;
import com.siberhus.ngai.annot.Transactional;
import com.siberhus.ngai.exception.NgaiRuntimeException;

/**
 * 
 * @author hussachai
 *
 */
public class ServiceBeanRegistry {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBeanRegistry.class);
	
	public static final String ATTRIBUTE_PREFIX = "_NgaiService:";
	
	private static final Set<Class<?>> REGISTERED_SERVICE_SET = new HashSet<Class<?>>();
	
	public static synchronized void register(Class<?> serviceClass) {
		if(serviceClass.isInterface() && Modifier.isAbstract(serviceClass.getModifiers())){
			return;
		}
		LOGGER.info("Registering service: {}",serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectService.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectDao.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectResource.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectJndi.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectHttpObject.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(Transactional.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(PersistenceContext.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(PersistenceUnit.class, serviceClass);
		
		AnnotatedAttributeUtils.inspectAttribute(PostConstruct.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(PreDestroy.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(Remove.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(PrePassivate.class, serviceClass);
		AnnotatedAttributeUtils.inspectAttribute(PostActivate.class, serviceClass);
		REGISTERED_SERVICE_SET.add(serviceClass);
		Ngai.getInstance().getSingletonServiceMap().put(serviceClass,null);
	}
	
	public static Object get(Class<?> targetClass, Class<?> implClass)  {
		
		return get(WebContext.DUMMY_WEB_CONTEXT, targetClass, implClass);
	}
	
	public static Object get(WebContext webContext, Class<?> targetClass, Class<?> implClass)  {
		
		Object service = null;
		
		Scope scope = Scope.Singleton;
		ServiceBean serviceBeanAnnot = targetClass.getAnnotation(ServiceBean.class);
		if(serviceBeanAnnot!=null){
			scope = serviceBeanAnnot.scope();
		}
		try{
			if(implClass!=Object.class){
				if(!targetClass.isInterface()){
					throw new NgaiRuntimeException("@InjectService: if implementation variable " +
							"is defined the target variable must be interface only.");
				}
				if(implClass.isInterface() 
						|| Modifier.isAbstract(implClass.getModifiers())){
					// Implementation class must be able to instantiate.
					throw new NgaiRuntimeException("@InjectService#implementation: "
							+implClass.getName()+" must be concrete class");
				}
				if(!targetClass.isAssignableFrom(implClass)){
					throw new NgaiRuntimeException("@InjectService#implementation: "
							+implClass.getName()+" must be a type of annotated type: "+targetClass.getName());
				}
					
				//Override @ServiceBean annotation in interface
				serviceBeanAnnot = implClass.getAnnotation(ServiceBean.class);
				if(serviceBeanAnnot!=null){
					scope = serviceBeanAnnot.scope();
				}
				service = getServiceInScope(webContext, implClass, scope);
				service = ServiceBeanProxy.newInstance(service);
				DependencyManager.inject(webContext, implClass, service, scope);
			}else{
				if(targetClass.isInterface()){
					throw new NgaiRuntimeException("Declared service is interface. You must specify implementation class");
				}
					
				service = getServiceInScope(webContext, targetClass, scope);
				DependencyManager.inject(webContext, targetClass, service, scope);
			}
		}catch(NgaiRuntimeException e){
			throw e;
		}catch(Exception e){
			throw new NgaiRuntimeException(e.toString(), e);
		}
		return service;
	}
	
	private static Object getServiceInScope(WebContext webContext, Class<?> serviceClass, Scope scope){
		
		if(!REGISTERED_SERVICE_SET.contains(serviceClass)){
			throw new NgaiRuntimeException("Service : "+serviceClass.getName()+" was not registered!!");
		}
		
		Map<Class<?>, Object> singletonServiceMap = Ngai.getInstance().getSingletonServiceMap();
		Object service = null;
		try{
			if(Scope.Prototype==scope){
				service = createServiceInstance(serviceClass);
			}else if(Scope.Singleton==scope){
				service = singletonServiceMap.get(serviceClass);
				if(service == null){
					service = createServiceInstance(serviceClass);
					singletonServiceMap.put(serviceClass, service);
				}
			}else{
				String attribName = ServiceBeanRegistry.ATTRIBUTE_PREFIX+serviceClass.getName();
				HttpServletRequest request = webContext.getRequest();
				if(Scope.Request==scope){
					RequestScopeBeanWrapper serviceWrapper = (RequestScopeBeanWrapper)
						request.getAttribute(attribName);
					if(serviceWrapper != null){
						service = serviceWrapper.getBean();
					}else{
						service = createServiceInstance(serviceClass);
						serviceWrapper = new RequestScopeBeanWrapper(service);
						request.setAttribute(attribName, serviceWrapper);
					}
				}else if(Scope.Session==scope){
					SessionScopeBeanWrapper serviceWrapper = (SessionScopeBeanWrapper)
						request.getSession().getAttribute(attribName);
					if(serviceWrapper != null){
						service = serviceWrapper.getBean();
					}else{
						service = createServiceInstance(serviceClass);
						serviceWrapper = new SessionScopeBeanWrapper(service);
						request.getSession().setAttribute(attribName, serviceWrapper);
					}
					
				}
			}
			
		}catch(Exception e){
			throw new NgaiRuntimeException("Cannot instantiate service class: "+serviceClass+
					" due to " + e.toString());
		}
		
		if(service==null){
			throw new NgaiRuntimeException("Service : "+serviceClass.getName()+" not found in scope: "+scope);
		}
		
		return service;
	}
	
	private static Object createServiceInstance(Class<?> serviceClass) throws InstantiationException, IllegalAccessException{
		Object service = serviceClass.newInstance();
//		if(service instanceof IBeanLifecycle){
//			((IBeanLifecycle)service).afterCreated();
//		}
		return service;
	}
	
}
