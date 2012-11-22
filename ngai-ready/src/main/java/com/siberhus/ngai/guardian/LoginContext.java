package com.siberhus.ngai.guardian;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class LoginContext implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	private String password;
	
	private Date loginAt;
	
	private String ipAddress;
	
	private boolean secureLogin;
	
	private Locale locale;
	
	private String userAgent;
	
	private String acceptCharset;
	
	private String acceptEncoding;
	
	private String acceptLanguage;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isSecureLogin() {
		return secureLogin;
	}

	public void setSecureLogin(boolean secureLogin) {
		this.secureLogin = secureLogin;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptCharset() {
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}
	
	
}
