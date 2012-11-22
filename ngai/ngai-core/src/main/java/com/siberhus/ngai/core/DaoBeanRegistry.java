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
import com.siberhus.ngai.annot.DaoBean;
import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.InjectHttpObject;
import com.siberhus.ngai.annot.InjectJndi;
import com.siberhus.ngai.annot.InjectResource;
import com.siberhus.ngai.annot.PostActivate;
import com.siberhus.ngai.annot.PrePassivate;
import com.siberhus.ngai.annot.Remove;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.exception.NgaiRuntimeException;
import com.siberhus.ngai.exception.ServiceException;

/**
 * 
 * @author hussachai
 *
 */
public class DaoBeanRegistry {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoBeanRegistry.class);
	
	public static final String ATTRIBUTE_PREFIX = "_NgaiDAO:";
	
	private static final Set<Class<?>> REGISTERED_DAO_SET = new HashSet<Class<?>>();
	
	public static synchronized void register(Class<?> daoClass) {
		if(daoClass.isInterface() && Modifier.isAbstract(daoClass.getModifiers())){
			return;
		}
		LOGGER.info("Registering DAO: {}",daoClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectDao.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectResource.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectJndi.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(InjectHttpObject.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(PersistenceContext.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(PersistenceUnit.class, daoClass);
		
		AnnotatedAttributeUtils.inspectAttribute(PostConstruct.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(PreDestroy.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(Remove.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(PrePassivate.class, daoClass);
		AnnotatedAttributeUtils.inspectAttribute(PostActivate.class, daoClass);
		REGISTERED_DAO_SET.add(daoClass);
		Ngai.getInstance().getSingletonDaoMap().put(daoClass,null);
	}
	
	public static Object get(Class<?> targetClass, Class<?> implClass)  {
		
		return get(WebContext.DUMMY_WEB_CONTEXT, targetClass, implClass);
	}
	
	public static Object get(WebContext webContext, Class<?> targetClass, Class<?> implClass) {
		
		Object dao = null;
		
		Scope scope = Scope.Singleton;
		DaoBean daoBeanAnnot = targetClass.getAnnotation(DaoBean.class);
		if(daoBeanAnnot!=null){
			scope = daoBeanAnnot.scope();
		}
		
		try{
			if(implClass!=Object.class){
				if(!targetClass.isInterface()){
					throw new NgaiRuntimeException("@InjectDao: if implementation variable " +
							"is defined the target variable must be interface only.");
				}
				if(implClass.isInterface()
						|| Modifier.isAbstract(implClass.getModifiers())){
					// Implementation class must be able to instantiate.
					throw new ServiceException("@InjectDao#implementation: "
							+implClass.getName()+" must be concrete class");
				}
				if(!targetClass.isAssignableFrom(implClass)){
					throw new ServiceException("@InjectDao#implementation: "
							+implClass.getName()+" must be a type of annotated type: "+targetClass.getName());
				}
				
				//Override @DaoBean annotation in interface
				daoBeanAnnot = implClass.getAnnotation(DaoBean.class);
				if(daoBeanAnnot!=null){
					scope = daoBeanAnnot.scope();
				}
				dao = getDaoInScope(webContext ,implClass, scope);
				dao = DaoBeanProxy.newInstance(dao);
				
				DependencyManager.inject(webContext, implClass, dao, scope);
				
			}else{
				if(targetClass.isInterface()){
					dao = DaoBeanProxy.newInstance(targetClass);
				}else{
					dao = getDaoInScope(webContext, targetClass, scope);
					//Proxy is disable here
					DependencyManager.inject(webContext, targetClass, dao, scope);
				}
			}
		}catch(NgaiRuntimeException e){
			throw e;
		}catch(Exception e){
			throw new NgaiRuntimeException(e.toString(), e);
		}
		return dao;
	}
	
	
	private static Object getDaoInScope(WebContext webContext, Class<?> daoClass, Scope scope) {
		
		if(!REGISTERED_DAO_SET.contains(daoClass)){
			throw new ServiceException("DAO : "+daoClass.getName()+" was not registered!!");
		}
		
		Map<Class<?>, Object> singletonDaoMap = Ngai.getInstance().getSingletonDaoMap();
		Object dao = null;
		try{
			if(Scope.Prototype==scope){
				dao = createDaoInstance(daoClass);
			}else if(Scope.Singleton==scope){
				dao = singletonDaoMap.get(daoClass);
				if(dao == null){
					dao = createDaoInstance(daoClass);
					singletonDaoMap.put(daoClass, dao);
				}
			}else{
				String attribName = ATTRIBUTE_PREFIX+daoClass.getName();
				HttpServletRequest request = webContext.getRequest();
				if(Scope.Request==scope){
					RequestScopeBeanWrapper daoWrapper = (RequestScopeBeanWrapper)
						request.getAttribute(attribName);
					if(daoWrapper == null){
						dao = createDaoInstance(daoClass);
						daoWrapper = new RequestScopeBeanWrapper(dao);
						request.setAttribute(attribName, daoWrapper);
					}
				}else if(Scope.Session==scope){
					SessionScopeBeanWrapper daoWrapper = (SessionScopeBeanWrapper)request.getSession().getAttribute(attribName);
					if(daoWrapper == null){
						dao = createDaoInstance(daoClass);
						daoWrapper = new SessionScopeBeanWrapper(dao);
						request.getSession().setAttribute(attribName, daoWrapper);
					}
				}
			}
		}catch(Exception e){
			throw new NgaiRuntimeException("Cannot instantiate DAO class: "+daoClass+
					" due to " + e.toString());
		}
		
		if(dao==null){
			throw new NgaiRuntimeException("DAO : "+daoClass.getName()+" not found in scope: "+scope);
		}
		
		return dao;
	}
	
	private static Object createDaoInstance(Class<?> daoClass) throws InstantiationException, IllegalAccessException{
		Object dao = daoClass.newInstance();
//		if(dao instanceof IBeanLifecycle){
//			((IBeanLifecycle)dao).afterCreated();
//		}
		return dao;
	}
	
	static interface A{
		public abstract void say();
	}
	static class B implements A{
		public void say(){
			System.out.println("Hello");
		}
	}
	public static void main(String[] args) {
		B b = new B();
		A a = (A)DaoBeanProxy.newInstance(b);
		a.say();
	}
}
