package com.siberhus.ngai.guardian.config;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import com.siberhus.commons.properties.ConfigurationException;
import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.guardian.Guardian;


public class AuthenticationConfig implements Serializable{
	
	public static final String PREFIX = GuardianConfig.PREFIX+"authentication.";
	public static final String ENABLED = PREFIX+"enabled";
	public static final String LOGIN_LOG_ENABLED = PREFIX+"loginLogEnabled";
	public static final String PASSWD_HASH_ALGO = PREFIX+"passwdHashAlgo";
	public static final String APPEND_USER_IN_PASSWD = PREFIX+"appendUserInPasswd";
	public static final String PASSWD_PATTERN = PREFIX+"passwdPattern"; 
	public static final String LOGIN_PAGE = PREFIX+"loginPage";
	public static final String SUCCESS_PAGE = PREFIX+"successPage";
	public static final String FAIL_PAGE = PREFIX+"failPage"; 
	public static final String MULTI_SESSION_LOGIN_ENABLED = PREFIX+"multiSessionLoginEnabled";
	public static final String RETRY_LIMIT = PREFIX+"retryLimit";
	public static final String REMEMBER_ME_ENABLED = PREFIX+"rememberMe.enabled";
	public static final String REMEMBER_ME_MAX_AGE = PREFIX+"rememberMe.maxAge";
	public static final String REMEMBER_ME_USER_COOKIE_NAME = PREFIX+"rememberMe.userCookieName";
	public static final String REMEMBER_ME_PASSWD_COOKIE_NAME = PREFIX+"rememberMe.passwdCookieName";
	public static final String REMEMBER_ME_SECURE = PREFIX+"rememberMe.secure";
	
	public static final int UNLIMITED_RETRY = -1;
	
	public static class RememberMe implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private boolean enabled = true;
		private int maxAge = 5;
		private String userCookieName = "4435asda5nfgtqw54";
		private String passwdCookieName = "4435asda5nfgtqw54";
		public boolean isEnabled() {
			return enabled;
		}
		public int getMaxAge() {
			return maxAge;
		}
		public String getUserCookieName() {
			return userCookieName;
		}
		public String getPasswdCookieName() {
			return passwdCookieName;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean enabled = true;
	
	private boolean loginLogEnabled = true;
	
	private HashAlgo passwdHashAlgo = HashAlgo.SHA;
	
	private boolean appendUserInPasswd = true;
	
	private Pattern passwdPattern;
	
	private String loginPage = "/login.jsp";
	
	private String successPage;
	
	private String failPage;
	
	private boolean multiSessionLoginEnabled = false;
	
	private int retryLimit = UNLIMITED_RETRY;
	
	private RememberMe rememberMe = new RememberMe();
	
	public AuthenticationConfig(StringMap ngaiProps){
		if(ngaiProps.containsKey(ENABLED)){
			this.enabled = ngaiProps.get(Boolean.class, ENABLED);
		}
		
		if(ngaiProps.containsKey(LOGIN_LOG_ENABLED)){
			this.loginLogEnabled = ngaiProps.get(Boolean.class, LOGIN_LOG_ENABLED);
		}
		if(ngaiProps.containsKey(PASSWD_HASH_ALGO)){
			this.passwdHashAlgo = ngaiProps.get(HashAlgo.class, PASSWD_HASH_ALGO);
		}
		if(ngaiProps.containsKey(APPEND_USER_IN_PASSWD)){
			this.appendUserInPasswd = ngaiProps.get(Boolean.class,APPEND_USER_IN_PASSWD);
		}
		if(ngaiProps.containsKey(PASSWD_PATTERN)){
			this.passwdPattern = ngaiProps.get(Pattern.class, PASSWD_PATTERN, 
					Pattern.compile("[a-zA-Z0-9_]{4,32}"));
		}
		this.loginPage = ngaiProps.getString(LOGIN_PAGE, loginPage);
		this.successPage = ngaiProps.getString(SUCCESS_PAGE);
		if(successPage==null){
			throw new ConfigurationException(SUCCESS_PAGE+" cannot be empty");
		}
		this.failPage = ngaiProps.getString(FAIL_PAGE);
		
		if(ngaiProps.containsKey(REMEMBER_ME_ENABLED)){
			rememberMe.enabled = ngaiProps.get(Boolean.class, REMEMBER_ME_ENABLED);
		}
		if(ngaiProps.containsKey(REMEMBER_ME_MAX_AGE)){
			rememberMe.maxAge = ngaiProps.get(Integer.class, REMEMBER_ME_MAX_AGE);
		}
		Pattern cookieNamePattern = Pattern.compile("[a-zA-Z0-9]{6,31}");
		if(ngaiProps.containsKey(REMEMBER_ME_USER_COOKIE_NAME)){
			String userCookieName = ngaiProps.getString(REMEMBER_ME_USER_COOKIE_NAME);
			if(!cookieNamePattern.matcher( userCookieName).matches()){
				throw new ConfigurationException(REMEMBER_ME_USER_COOKIE_NAME+
						" must contains only alphanumeric characters between 6 to 31");
			}
			rememberMe.userCookieName = userCookieName;
		}
		if(ngaiProps.containsKey(REMEMBER_ME_PASSWD_COOKIE_NAME)){
			String passwdCookieName = ngaiProps.getString(REMEMBER_ME_PASSWD_COOKIE_NAME);
			if(!cookieNamePattern.matcher( passwdCookieName).matches()){
				throw new ConfigurationException(REMEMBER_ME_PASSWD_COOKIE_NAME+
						" must contains only alphanumeric characters between 6 to 31");
			}
			rememberMe.passwdCookieName = passwdCookieName;
		}
		if(ngaiProps.containsKey(MULTI_SESSION_LOGIN_ENABLED)){
			this.multiSessionLoginEnabled = ngaiProps.get(Boolean.class, MULTI_SESSION_LOGIN_ENABLED);
		}
		if(ngaiProps.containsKey(RETRY_LIMIT)){
			this.retryLimit = ngaiProps.get(Integer.class, RETRY_LIMIT);
		}
	}
	
	public static AuthenticationConfig get(){
		
		GuardianConfig config = Guardian.getInstance().getConfiguration();
		return config.getAuthenticationConfig();
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public boolean isLoginLogEnabled() {
		return loginLogEnabled;
	}

	public HashAlgo getPasswdHashAlgo() {
		return passwdHashAlgo;
	}
	
	public boolean isAppendUserInPasswd() {
		return appendUserInPasswd;
	}

	public Pattern getPasswdPattern() {
		return passwdPattern;
	}

	public String getLoginPage() {
		return loginPage;
	}
	
	public String getSuccessPage() {
		return successPage;
	}

	public String getFailPage() {
		return failPage;
	}

	public boolean isMultiSessionLoginEnabled() {
		return multiSessionLoginEnabled;
	}

	public int getRetryLimit() {
		return retryLimit;
	}
	
	public RememberMe getRememberMe() {
		return rememberMe;
	}
	
	public static void main(String[] args) {
		System.out.println(DigestUtils.md5Hex("password"));
		System.out.println(DigestUtils.md5Hex("password1"));
	}
}
