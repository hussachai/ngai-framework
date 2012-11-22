package com.siberhus.ngai.localization.action;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sourceforge.stripes.controller.StripesFilter;

import org.apache.commons.lang.StringUtils;

public class LocalizableError extends net.sourceforge.stripes.validation.LocalizableError{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String messageKey;
	private Object parameter[];
	
	public LocalizableError(String messageKey, Object... parameter) {
		super(messageKey, parameter);
		this.messageKey = messageKey;
		this.parameter = parameter;
	}
	
	@Override
    protected String getMessageTemplate(Locale locale) {
        ResourceBundle bundle = StripesFilter.getConfiguration().
                getLocalizationBundleFactory().getErrorMessageBundle(locale);
        try{
        	return bundle.getString(messageKey);
        }catch(MissingResourceException e){
        	if(parameter!=null){
        		return messageKey+" ("+StringUtils.join(parameter)+")";
        	}
        	return messageKey;
        }
    }
	
}
