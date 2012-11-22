package com.siberhus.ngai.core;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;

/**
 * 
 * @author hussachai
 *
 */
public interface INgaiInterceptor {
	
	public void init(Configuration config) throws Exception;
	
	public static interface RequestInitPhase extends INgaiInterceptor{
		public void beforeRequestInit(ExecutionContext context);
		public void afterRequestInit(ExecutionContext context);
	}
	
	public static interface ActionBeanResolutionPhase extends INgaiInterceptor{
		public void beforeActionBeanResolution(ExecutionContext context);
		public void afterActionBeanResolution(ExecutionContext context);
	}
	
	public static interface HandlerResolutionPhase extends INgaiInterceptor{
		public void beforeHandlerResolution(ExecutionContext context);
		public void afterHandlerResolution(ExecutionContext context);
	}
	
	public static interface BindingAndValidationPhase extends INgaiInterceptor{
		public void beforeBindingAndValidation(ExecutionContext context);
		public void afterBindingAndValidation(ExecutionContext context);
	}
	
	public static interface CustomValidationPhase extends INgaiInterceptor{
		public void beforeCustomValidation(ExecutionContext context);
		public void afterCustomValidation(ExecutionContext context);
	}
	
	public static interface EventHandlingPhase extends INgaiInterceptor{
		public void beforeEventHandling(ExecutionContext context);
		public void afterEventHandling(ExecutionContext context);
	}
	
	public static interface ResolutionExecutionPhase extends INgaiInterceptor{
		public void beforeResolutionExecution(ExecutionContext context);
		public void afterResolutionExecution(ExecutionContext context);
	}
	
	public static interface RequestCompletePhase extends INgaiInterceptor{
		public void beforeRequestComplete(ExecutionContext context);
		public void afterRequestComplete(ExecutionContext context);
	}
}
