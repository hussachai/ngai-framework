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
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.lang.AnnotatedAttributeUtils;
import com.siberhus.commons.lang.AnnotatedAttributeUtils.AnnotatedAttribute;
import com.siberhus.ngai.EntityQueryMap;
import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.org.stripesstuff.stripersist.Stripersist;

/**
 * 
 * @author hussachai
 * @since 0.9
 */
@Intercepts( {
	LifecycleStage.RequestInit,
	LifecycleStage.ActionBeanResolution,
	LifecycleStage.HandlerResolution,
	LifecycleStage.BindingAndValidation,
	LifecycleStage.CustomValidation,
	LifecycleStage.EventHandling,
	LifecycleStage.ResolutionExecution,
	LifecycleStage.RequestComplete })
public class NgaiCoreInterceptor extends Stripersist {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NgaiCoreInterceptor.class);
	
	public static final String VAR_ACTION_BEAN_CLASS = "actionBeanClass";

	public static final String VAR_ACTION_BEAN_FQCN = "actionBeanFQCN";

	public static final String VAR_ACTION_BEAN_PATH = "actionBeanPath";
	
	private NgaiInterceptorManager interceptorManager;
	
	public NgaiCoreInterceptor(){}
	
	/* Constructor for testing only */
	public NgaiCoreInterceptor(String persistenceXmlPath) throws Exception {
		this(NgaiCoreInterceptor.class.getResource(persistenceXmlPath));
	}
	
	public NgaiCoreInterceptor(URL persistenceXmlUrl) throws Exception {
		init(persistenceXmlUrl);
	}
	/**/
	
	@Override
	public void init(Configuration config)throws Exception {
		
		interceptorManager = new NgaiInterceptorManager();
		//initialize all Ngai extensions after Ngai is ready.
//		super.init(config);
		interceptorManager.initialize(config);
	}
	
	@Override
	public Resolution intercept(ExecutionContext context) throws Exception {

		Resolution resolution = null;
		
		switch (context.getLifecycleStage()) {
		case RequestInit:
			LOGGER.trace("Initializing EntityManager");
			requestInit();
			
			resolution = interceptorManager.onRequestInit(context);
			
			HttpServletRequest request = context.getActionBeanContext().getRequest();
			EntityQueryMap eqm = new EntityQueryMap(request);
			request.setAttribute("entityQuery", eqm);
			
			return resolution;
		case ActionBeanResolution:
			
			resolution = interceptorManager.onActionBeanResolution(context);
			
			setupActionBean(context);
			
			return resolution;
		case HandlerResolution:
			
			resolution = interceptorManager.onHandlerResolution(context);
			
			return resolution;
		case BindingAndValidation:
			
			resolution = interceptorManager.onBindingAndValidation(context);
			
			return resolution;
		case CustomValidation:
			
			resolution = interceptorManager.onCustomValidation(context);
			
			return resolution;
		case EventHandling:
			
			resolution = interceptorManager.onEventHandling(context);
			
			return resolution;
		case ResolutionExecution:
			
			resolution = interceptorManager.onResolutionExecution(context);
			
			return resolution;
		case RequestComplete:
			
			resolution = interceptorManager.onRequestComplete(context);
			
			LOGGER.trace("Cleaning up EntityManager");
			requestComplete();
			
			return resolution;
		}
		
		return context.proceed();
	}
	
	
	protected void setupActionBean(ExecutionContext context) throws Exception {

		LOGGER.trace("Setting up ActionBean resource");
		
		Class<? extends ActionBean> actionBeanClass = context.getActionBean().getClass();
		String actionBeanClassName = actionBeanClass.getName();
		
		HttpServletRequest request = context.getActionBeanContext().getRequest();
		HttpServletResponse response = context.getActionBeanContext().getResponse();
		
		request.setAttribute(VAR_ACTION_BEAN_CLASS, actionBeanClass);
		request.setAttribute(VAR_ACTION_BEAN_FQCN, actionBeanClassName);
		request.setAttribute(VAR_ACTION_BEAN_PATH, Ngai.getInstance()
				.getActionBeanPathMap().get(actionBeanClass));
		
		LOGGER.debug("Injecting dependencies of instance {}",actionBeanClassName);
		
		List<AnnotatedAttribute> annotatedAttribList = AnnotatedAttributeUtils
				.getAnnotatedAttributes(actionBeanClass);
		
		if (annotatedAttribList != null) {
			
			WebContext webContext = new WebContext(request, response);
			
			for (AnnotatedAttribute annotatedAttrib : annotatedAttribList) {
				
				Annotation annot = annotatedAttrib.getAnnotation();
				Class<?> annotatedType = annotatedAttrib.getType();
				
				if (annot instanceof InjectService) {
					
					Object serviceObject = ServiceBeanRegistry.get(webContext,
							annotatedType, ((InjectService) annot).implementation());
					annotatedAttrib.set(context.getActionBean(), serviceObject);
					
				} else if (annot instanceof InjectDao) {
					
					Object daoObject = DaoBeanRegistry.get(webContext, 
							annotatedType, ((InjectDao) annot).implementation());
					annotatedAttrib.set(context.getActionBean(), daoObject);
				}

			}
		}
	}
	
}
