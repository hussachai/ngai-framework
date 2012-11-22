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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sourceforge.stripes.util.ReflectUtil;

import com.siberhus.ngai.QTO;
import com.siberhus.ngai.TemporalObject;
import com.siberhus.ngai.annot.DaoBean;
import com.siberhus.ngai.annot.JpaNativeQuery;
import com.siberhus.ngai.annot.JpaQuery;
import com.siberhus.ngai.annot.JpaSave;
import com.siberhus.ngai.dao.IAnnotatedQueryDao;
import com.siberhus.ngai.exception.DaoException;
import com.siberhus.ngai.model.IModel;
import com.siberhus.org.stripesstuff.stripersist.Stripersist;

/**
 * 
 * @author hussachai
 * @since 0.9
 */
public class DaoBeanProxy implements InvocationHandler{
	
	private Object object;
	
	private Class<?> objectClass;
	
	public static Object newInstance(Object object){
		
		return Proxy.newProxyInstance(
				object.getClass().getClassLoader(), 
				ReflectUtil.getImplementedInterfaces(object.getClass()).toArray(new Class[0]), 
				new DaoBeanProxy(object));
	}
	
	public static Object newInstance(Class<?> objectClass){
		
		return Proxy.newProxyInstance(
				objectClass.getClassLoader(), 
				ReflectUtil.getImplementedInterfaces(objectClass).toArray(new Class[0]), 
				new DaoBeanProxy(objectClass));
	}
	
	/**
	 * 
	 * @param proxy
	 * @return
	 * 
	 * @author hussachai
	 * @since 0.9
	 */
	public static Object getRealObject(Proxy proxy){
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
		if(invocationHandler instanceof DaoBeanProxy){
			DaoBeanProxy dp = ((DaoBeanProxy)invocationHandler);
			return dp.getObject();
		}
		return null;
	}
	
	private DaoBeanProxy(Class<?> objectClass){
		this.objectClass = objectClass;
	}
	
	private DaoBeanProxy(Object object){
		this.object = object;
		this.objectClass = object.getClass();
	}
	
	public Object getObject() {
		return object;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		Object result = null;
		try{
			result = executeDaoMethod(this, method, args);
		}catch(UndeclaredThrowableException e){
			Throwable cause = e;
			if(e.getUndeclaredThrowable()!=null){
				cause = e.getUndeclaredThrowable();
			}
			throw new DaoException(cause.getMessage(), cause);
		}catch(Throwable e){
			throw new DaoException(e.getMessage(), e);
		}	
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static Object executeDaoMethod(DaoBeanProxy proxy, Method method, Object[] args)
		throws Throwable {
		
		List<?> result = null;
		boolean ignoreImplementedCode = false;
		String persistenceUnitName = null;
		String entityName = null;
		String queryString = null;
		EntityManager entityManager = null;
		Query query = null;
		boolean update = false;
		
		if(!IAnnotatedQueryDao.class.isAssignableFrom(proxy.getObjectClass())){
			return invokeUserMethod(proxy.getObject(), method, args);
		}
		DaoBean daoClassAnnot = (DaoBean)proxy.getObjectClass().getAnnotation(DaoBean.class);
		
		JpaQuery jpaQuery = null;
		JpaNativeQuery jpaNativeQuery = null;
		JpaSave jpaSave = null;
		
		if( (jpaQuery=method.getAnnotation(JpaQuery.class)) != null ){
			
			ignoreImplementedCode = jpaQuery.ignoreImplementedCode();
			update = jpaQuery.update();
			persistenceUnitName = jpaQuery.persistenceUnitName();
			entityManager = getEntityManager(daoClassAnnot,persistenceUnitName);
			entityName = jpaQuery.entityName();
			if("".equals(entityName) && daoClassAnnot!=null){
				entityName = daoClassAnnot.entityName();
			}
			queryString = jpaQuery.query();
			
			if("".equals(queryString)){
				if("".equals(entityName)){
					query = entityManager.createNamedQuery(method.getName());
				}else{
					query = entityManager.createNamedQuery(entityName+"."+method.getName());
				}
			}else{
				query = entityManager.createQuery(queryString);
			}
			addAllParametersToQuery(query,args);
			
		}else if( (jpaNativeQuery = method.getAnnotation(JpaNativeQuery.class)) != null){
			
			ignoreImplementedCode = jpaNativeQuery.ignoreImplementedCode();
			update = jpaNativeQuery.update();
			persistenceUnitName = jpaNativeQuery.persistenceUnitName();
			entityManager = getEntityManager(daoClassAnnot, persistenceUnitName);
			queryString = jpaNativeQuery.query();
			Class<?> resultClass = jpaNativeQuery.resultClass();
			String resultSetMapping = jpaNativeQuery.resultSetMapping();
			if(resultClass!=Object.class){
				query = entityManager.createNativeQuery(queryString, resultClass);
			}else if(!"".equals(resultSetMapping)){
				query = entityManager.createNativeQuery(queryString, resultSetMapping);
			}else{
				query = entityManager.createNativeQuery(queryString);
			}
			addAllParametersToQuery(query, args);
		}else if( (jpaSave = method.getAnnotation(JpaSave.class)) != null){
			
			ignoreImplementedCode = jpaSave.ignoreImplementedCode();
			persistenceUnitName = jpaSave.persistenceUnitName();
			entityManager = getEntityManager(daoClassAnnot, persistenceUnitName);
			if(args.length==1){
				CrudHelper.save(entityManager, args[0]);
			}else if(args.length==2){
				CrudHelper.save(entityManager, args[0], (IModel<Serializable>)args[1]);
			}else{
				throw new IllegalArgumentException("@JpaPersist accept 1 or 2 arguments. " +
						"For example: save(model) or save(model,user)");
			}
			
		}else{
			
			if(args.length==1 && args[0] instanceof QTO){
				
				QTO qto = (QTO)args[0];
				entityManager = Stripersist.getEntityManager();
				
				if(qto.isNativeQuery()){
					
					query = entityManager.createNamedQuery(qto.buildQueryString());
				}else{
					
					query = entityManager.createQuery(qto.buildQueryString());
				}
				
				addAllParametersToQuery(query, args);
			}
			return invokeUserMethod(proxy.getObject(), method, args);
		}
		
		if(query!=null){
			
			if(update){
				
				boolean hasTrxAlreadyActive = entityManager.getTransaction().isActive();
				if(!hasTrxAlreadyActive){
					entityManager.getTransaction().begin();
				}
				query.executeUpdate();
				if(!hasTrxAlreadyActive){
					entityManager.getTransaction().commit();
				}
			}else{
				result = query.getResultList();
			}
		}
		
		if(!ignoreImplementedCode){
			
			if(proxy.getObject()!=null){
				
				method.invoke(proxy.getObject() , args);
			}
		}
		
		if( ! Collection.class.isAssignableFrom(method.getReturnType())){
			
			if(result!=null && result.size()>0){
				
				return result.get(0);
			}else{
				
				return null;
			}
		}
		
		return result;
	}
	
	private static Object invokeUserMethod(Object object, Method method, Object[] args) throws Throwable {
		
		if(object!=null){
			try{
				
				return method.invoke(object , args);
			}catch(InvocationTargetException e){
				
				throw e.getTargetException();
			}catch(Exception e){
				
				throw e;
			}
		}
		return null;
	}
	
	private static EntityManager getEntityManager(DaoBean daoClassAnnot, String persistenceUnitName){
		
		if("".equals(persistenceUnitName) && daoClassAnnot!=null){
			persistenceUnitName = daoClassAnnot.persistenceUnitName();
		}
		
		if("".equals(persistenceUnitName)){
			
			return Stripersist.getEntityManager();
		}else{
			
			return Stripersist.getEntityManager(persistenceUnitName);
		}
	}
	
	private static void addAllParametersToQuery(Query query, Object[] args){
		
		if(args!=null){
			
			if(args.length==1 && args[0] instanceof QTO){
				
				QTO qto = (QTO)args[0];
				
				for(int i=0; i< qto.getParameterList().size(); i++){
					
					Object param = qto.getParameterList().get(i);
					
					if(param instanceof TemporalObject){
						
						TemporalObject tempParam = ((TemporalObject)param);
						query.setParameter(i+1,tempParam.getTemporalObject()
								,tempParam.getTemporalType());
					}else{
						query.setParameter(i+1, param);
					}
				}
				
				if(qto.getFirstResult()!=null){
					
					query.setFirstResult(qto.getFirstResult());
				}
				
				if(qto.getMaxResult()!=null){
					
					query.setMaxResults(qto.getMaxResult());
				}
			}else{
				
				for(int i=0;i<args.length;i++){
					
					query.setParameter(i+1, args[i]);
				}
			}
		}
	}
	
}
