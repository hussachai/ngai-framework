package com.siberhus.ngai.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesServletException;
import net.sourceforge.stripes.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.lang.ClassUtils;
import com.siberhus.ngai.config.NgaiConfiguration;

public class NgaiInterceptorManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NgaiInterceptorManager.class);
	
	private List<INgaiInterceptor.RequestInitPhase> requestInitCompList;
	
	private List<INgaiInterceptor.ActionBeanResolutionPhase> actionBeanResolutionCompList;
	
	private List<INgaiInterceptor.HandlerResolutionPhase> handlerResolutionCompList;
	
	private List<INgaiInterceptor.BindingAndValidationPhase> bindingAndValidationCompList;
	
	private List<INgaiInterceptor.CustomValidationPhase> customValidationCompList;
	
	private List<INgaiInterceptor.EventHandlingPhase> eventHandlingCompList;
	
	private List<INgaiInterceptor.ResolutionExecutionPhase> resolutionExecutionCompList;
	
	private List<INgaiInterceptor.RequestCompletePhase> requestCompleteCompList;
	
	protected void initialize(Configuration config)throws Exception{
		
		LOGGER.debug("Intializing all Ngai interceptors");
		
		String ngaiInterceptorsParam = config.getBootstrapPropertyResolver()
			.getProperty(NgaiConfiguration.NGAI_INTERCEPTOR_CLASSES);
		
		ngaiInterceptorsParam = StringUtils.trimToNull(ngaiInterceptorsParam);
		
		if(ngaiInterceptorsParam==null){
			return;
		}
		
		String[] ngaiInterceptors = StringUtil.standardSplit(ngaiInterceptorsParam);
		
		for(String interceptorClassName : ngaiInterceptors){
			
			Class<?> interceptorClass = ClassUtils.loadClass(interceptorClassName);
			INgaiInterceptor interceptor = (INgaiInterceptor)interceptorClass.newInstance();
			interceptor.init(config);
			
			if(interceptor instanceof INgaiInterceptor.RequestInitPhase){
				
				if(requestInitCompList==null){
					requestInitCompList = new ArrayList<INgaiInterceptor.RequestInitPhase>();
				}
				requestInitCompList.add((INgaiInterceptor.RequestInitPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.ActionBeanResolutionPhase){
				
				if(actionBeanResolutionCompList==null){
					actionBeanResolutionCompList = new ArrayList<INgaiInterceptor.ActionBeanResolutionPhase>();
				}
				actionBeanResolutionCompList.add((INgaiInterceptor.ActionBeanResolutionPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.HandlerResolutionPhase){
				
				if(handlerResolutionCompList==null){
					handlerResolutionCompList = new ArrayList<INgaiInterceptor.HandlerResolutionPhase>();
				}
				handlerResolutionCompList.add((INgaiInterceptor.HandlerResolutionPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.BindingAndValidationPhase){
				
				if(bindingAndValidationCompList==null){
					bindingAndValidationCompList = new ArrayList<INgaiInterceptor.BindingAndValidationPhase>();
				}
				bindingAndValidationCompList.add((INgaiInterceptor.BindingAndValidationPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.CustomValidationPhase){
				
				if(customValidationCompList==null){
					customValidationCompList = new ArrayList<INgaiInterceptor.CustomValidationPhase>();
				}
				customValidationCompList.add((INgaiInterceptor.CustomValidationPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.EventHandlingPhase){
				
				if(eventHandlingCompList==null){
					eventHandlingCompList = new ArrayList<INgaiInterceptor.EventHandlingPhase>();
				}
				eventHandlingCompList.add((INgaiInterceptor.EventHandlingPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.ResolutionExecutionPhase){
				
				if(resolutionExecutionCompList==null){
					resolutionExecutionCompList = new ArrayList<INgaiInterceptor.ResolutionExecutionPhase>();
				}
				resolutionExecutionCompList.add((INgaiInterceptor.ResolutionExecutionPhase)interceptor);
			}
			if(interceptor instanceof INgaiInterceptor.RequestCompletePhase){
				
				if(requestCompleteCompList==null){
					requestCompleteCompList = new ArrayList<INgaiInterceptor.RequestCompletePhase>();
				}
				requestCompleteCompList.add((INgaiInterceptor.RequestCompletePhase)interceptor);
			}
		}
	}
	
	public Resolution onRequestInit(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(requestInitCompList!=null){
			for(INgaiInterceptor.RequestInitPhase i : requestInitCompList){
				i.beforeRequestInit(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.RequestInitPhase i : requestInitCompList){
				i.afterRequestInit(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	public Resolution onActionBeanResolution(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(actionBeanResolutionCompList!=null){
			for(INgaiInterceptor.ActionBeanResolutionPhase i : actionBeanResolutionCompList){
				i.beforeActionBeanResolution(context);
			}
			resolution = context.proceed();
			
			//Resolving event name early is necessary for Ngai extension
			resolveEventNameEarly(context);
			
			for(INgaiInterceptor.ActionBeanResolutionPhase i : actionBeanResolutionCompList){
				i.afterActionBeanResolution(context);
			}
			
			if(context.getResolution()!=null){
				resolution = context.getResolution();
			}
		}else{
			resolution = context.proceed();
		}
		
		return resolution;
	}
	
	public Resolution onHandlerResolution(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(handlerResolutionCompList!=null){
			for(INgaiInterceptor.HandlerResolutionPhase i : handlerResolutionCompList){
				i.beforeHandlerResolution(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.HandlerResolutionPhase i : handlerResolutionCompList){
				i.afterHandlerResolution(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	public Resolution onBindingAndValidation(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(bindingAndValidationCompList!=null){
			for(INgaiInterceptor.BindingAndValidationPhase i : bindingAndValidationCompList){
				i.beforeBindingAndValidation(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.BindingAndValidationPhase i : bindingAndValidationCompList){
				i.afterBindingAndValidation(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	public Resolution onCustomValidation(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(customValidationCompList!=null){
			for(INgaiInterceptor.CustomValidationPhase i : customValidationCompList){
				i.beforeCustomValidation(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.CustomValidationPhase i : customValidationCompList){
				i.afterCustomValidation(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	public Resolution onEventHandling(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(eventHandlingCompList!=null){
			for(INgaiInterceptor.EventHandlingPhase i : eventHandlingCompList){
				i.beforeEventHandling(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.EventHandlingPhase i : eventHandlingCompList){
				i.afterEventHandling(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	public Resolution onResolutionExecution(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(resolutionExecutionCompList!=null){
			for(INgaiInterceptor.ResolutionExecutionPhase i : resolutionExecutionCompList){
				i.beforeResolutionExecution(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.ResolutionExecutionPhase i : resolutionExecutionCompList){
				i.afterResolutionExecution(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	public Resolution onRequestComplete(ExecutionContext context) throws Exception{
		
		Resolution resolution = null;
		if(requestCompleteCompList!=null){
			for(INgaiInterceptor.RequestCompletePhase i : requestCompleteCompList){
				i.beforeRequestComplete(context);
			}
			resolution = context.proceed();
			for(INgaiInterceptor.RequestCompletePhase i : requestCompleteCompList){
				i.afterRequestComplete(context);
			}
		}else{
			resolution = context.proceed();
		}
		return resolution;
	}
	
	/**
	 * Stripes will resolve event name at the HandlerResolution stage which is too late
	 * for some Ngai extensions. Actually Stripes can resolve event name after ActionBeanResoluton stage
	 * was proceeded because the ActionBean object was created already. 
	 * 
	 * @param context
	 * @throws StripesServletException
	 */
	protected void resolveEventNameEarly(ExecutionContext context) throws StripesServletException{
		
		final Configuration config = StripesFilter.getConfiguration();
		ActionBean actionBean = context.getActionBean();
		Class<? extends ActionBean> actionBeanClass = actionBean.getClass();
		ActionBeanContext actionBeanContext = context.getActionBeanContext();
		ActionResolver resolver = config.getActionResolver();
		
		 // Then lookup the event name and handler method etc.
        String eventName = resolver.getEventName(actionBeanClass, actionBeanContext);
        actionBeanContext.setEventName(eventName);
        
        final Method handler;
        if (eventName == null) {
            handler = resolver.getDefaultHandler(actionBeanClass);
            if (handler != null) {
            	actionBeanContext.setEventName(resolver.getHandledEvent(handler));
            }
        }
	}
	
	
}
