package com.siberhus.ngai.guardian.action;

import java.util.regex.Pattern;

import net.sourceforge.stripes.action.ActionBean;

import com.siberhus.ngai.guardian.config.AuthenticationConfig;
import com.siberhus.ngai.localization.action.Messages;

public class PasswordValidatorUtil {
	
	public static final void validate(ActionBean actionBean, 
			String fieldName, String password){
		
		Pattern passwdPattern = AuthenticationConfig.get().getPasswdPattern();
		if(!passwdPattern.matcher(password).matches()){
			Messages.addLocalizedFieldError(actionBean, fieldName, 
					"password.invalidPattern", password, 
					passwdPattern.pattern());
		}
	}
	
}
