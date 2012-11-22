package com.siberhus.ngai;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class AppVariable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mode;

	private Locale locale;

	private Map<String, String> formatPattern;
	
	public Date getCurrentDate(){
		return new Date();
	}
	
	public String getMode() {
		return mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Map<String, String> getFormatPattern() {
		return formatPattern;
	}

	public void setFormatPattern(Map<String, String> formatPattern) {
		this.formatPattern = formatPattern;
	}
	
}
