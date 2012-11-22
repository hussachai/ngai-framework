package com.siberhus.ngai.localization;

import java.nio.charset.Charset;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.DefaultLocalePicker;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.exception.NgaiRuntimeException;

public class SessionLocalePicker extends DefaultLocalePicker {
	
	private final Logger LOGGER = LoggerFactory.getLogger(SessionLocalePicker.class);  
	
	public static final String LOCALE_SESSION_ATTR = "_NgaiLocalization.Locale";
	
	public static final String LOCALE_PICKER_ENABLED = "localization.localePicker.enabled";
	public static final String CHARACTER_ENCODING = "localization.localePicker.characterEncoding";
	
	private boolean enabled = true;
	private String characterEncoding = "UTF-8";
	
	public static Locale setUserPreferredLocale(HttpServletRequest request, String language){
		Locale preferredLocale = request.getLocale();;
		if(language!=null){
			preferredLocale = new Locale(language, preferredLocale.getCountry());
		}
		request.getSession().setAttribute(LOCALE_SESSION_ATTR, preferredLocale);
		return preferredLocale;
	}
	
	@Override
	public void init(Configuration configuration) throws Exception {
		StringMap ngaiProps = Ngai.getInstance().getNgaiProperties();
		
		enabled = ngaiProps.get(Boolean.class, LOCALE_PICKER_ENABLED, enabled);
		characterEncoding = ngaiProps.getString(CHARACTER_ENCODING, characterEncoding);
		if(!Charset.isSupported(characterEncoding)){
			throw new NgaiRuntimeException("Unsupported character encoding: "+characterEncoding);
		}
		
		if(!enabled){
			LOGGER.info("SessionLocalePicker is disabled. Activating DefaultLocalePicker instead.");
			super.init(configuration);
		}else{
			LOGGER.info("SessionLocalePicker is enabled");
			LOGGER.info("Character encoding: {}", characterEncoding);
		}
	}
	
	@Override
	public Locale pickLocale(HttpServletRequest request) {
		
		if(!enabled){
			return super.pickLocale(request);
		}
		Locale locale = (Locale)request.getSession()
			.getAttribute(LOCALE_SESSION_ATTR);
		
		if(locale!=null){
			return locale;
		}
		return request.getLocale();
	}
	
	@Override
	public String pickCharacterEncoding(HttpServletRequest request,
			Locale locale) {
		if(!enabled){
			return super.pickCharacterEncoding(request, locale);
		}
		return characterEncoding;
	}
	
	public static void main(String[] args) {
		StringMap s = new StringMap();
		System.out.println(s.getString("aaa","hello"));
	}
}
