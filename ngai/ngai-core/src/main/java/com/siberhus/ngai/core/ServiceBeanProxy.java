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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

import net.sourceforge.stripes.util.ReflectUtil;

import com.siberhus.ngai.annot.Transactional;
import com.siberhus.ngai.exception.ServiceException;

public class ServiceBeanProxy implements InvocationHandler{

	private Object object;
	
	public static Object newInstance(Object object) {
		return Proxy.newProxyInstance(object.getClass().getClassLoader(),
				ReflectUtil.getImplementedInterfaces(object.getClass())
						.toArray(new Class[0]), new ServiceBeanProxy(object));
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
		if(invocationHandler instanceof ServiceBeanProxy){
			ServiceBeanProxy sp = ((ServiceBeanProxy)invocationHandler);
			return sp.getObject();
		}
		return null;
	}
	
	private ServiceBeanProxy(Object object){
		this.object = object;
	}
	
	public Object getObject() {
		return object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		try{
			result = executeServiceMethod(this, method, args);
		}catch(UndeclaredThrowableException e){
			Throwable cause = e;
			if(e.getUndeclaredThrowable()!=null){
				cause = e.getUndeclaredThrowable();
			}
			throw new ServiceException(cause.getMessage(), cause);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
		return result;
	}
	
	private static Object executeServiceMethod(ServiceBeanProxy proxy, Method method, Object[] args)
		throws Throwable {
		//Ngai does not yet support Transaction Management
		return invokeUserMethod(proxy.getObject(), method, args);
	}
	
	@SuppressWarnings("unused")
	private static void applyLocalEntityTransaction(Class<?> objectClass,Method method){
		Transactional trxAttrib = method.getAnnotation(Transactional.class);
		if(trxAttrib == null){
			trxAttrib = objectClass.getAnnotation(Transactional.class);
		}
		
		switch(trxAttrib.propagation()){
		case REQUIRED:
			break;
		case REQUIRES_NEW:
			break;
		case SUPPORTS:
			break;
		case MANDATORY:
			break;
		case NOT_SUPPORTED:
			break;
		case NEVER:
			break;
		}
	}
	
	@SuppressWarnings("unused")
	private static void applyManagedEntityTransaction(){
		throw new UnsupportedOperationException("Not implemented yet");
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
	
	
}
