package com.siberhus.ngai.localization.action;


import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;

public class Messages {
	
	public static void addFieldError(ActionBean actionBean, 
			String field, String messageKey, Object...parameter){
		actionBean.getContext().getValidationErrors()
			.add(field, new SimpleError(messageKey, parameter));
	}
	
	public static void addLocalizedFieldError(ActionBean actionBean, 
			String field, String messageKey, Object...parameter){
		actionBean.getContext().getValidationErrors()
			.add(field, new LocalizableError(messageKey, parameter));
	}
	
	public static void addError(ActionBean actionBean, 
			String messageKey, Object...parameter){
		actionBean.getContext().getValidationErrors()
			.addGlobalError(new SimpleError(messageKey, parameter));
	}
	
	public static void addLocalizedError(ActionBean actionBean, 
			String messageKey, Object...parameter){
		actionBean.getContext().getValidationErrors()
			.addGlobalError(new LocalizableError(messageKey, parameter));
	}
	
	public static void addInfo(ActionBean actionBean, 
			String messageKey, Object...parameter){
		actionBean.getContext().getMessages()
			.add(new SimpleMessage(messageKey, parameter));
	}
	
	public static void addLocalizedInfo(ActionBean actionBean, 
			String messageKey, Object...parameter){
		actionBean.getContext().getMessages()
			.add(new LocalizableMessage(messageKey, parameter));
	}
	
}
