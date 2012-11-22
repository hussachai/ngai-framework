package com.siberhus.ngai.guardian.config;

import java.io.Serializable;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.guardian.Guardian;

public class AuthorizationConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PREFIX = GuardianConfig.PREFIX+"authorization.";
	public static final String ENABLED = PREFIX+"enabled";
	public static final String UNAUTHORIZED_PAGE = PREFIX+"unauthorizedPage";
	public static final String CACHE_ENABLED = PREFIX+"cacheEnabled";
	public static final String REGEX_ENABLED = PREFIX+"regexEnabled";
	public static final String USER_LEVEL_ENABLED = PREFIX+"userLevelEnabled";
	public static final String ROLE_LEVEL_ENABLED = PREFIX+"roleLevelEnabled";
	
	private boolean enabled = true;
	
	private String unauthorizedPage;
	
	private boolean cacheEnabled = true;
	
	private boolean regexEnabled = true;
	
	private boolean userLevelEnabled = true;
	
	private boolean roleLevelEnabled = true;
	
	public AuthorizationConfig(StringMap ngaiProps){
		if(ngaiProps.containsKey(ENABLED)){
			enabled = ngaiProps.get(Boolean.class, ENABLED);
		}
		
		this.unauthorizedPage = ngaiProps.getString(UNAUTHORIZED_PAGE);
		
		if(ngaiProps.containsKey(CACHE_ENABLED)){
			cacheEnabled = ngaiProps.get(Boolean.class, CACHE_ENABLED);
		}
		if(ngaiProps.containsKey(REGEX_ENABLED)){
			regexEnabled = ngaiProps.get(Boolean.class, REGEX_ENABLED);
		}
		if(ngaiProps.containsKey(USER_LEVEL_ENABLED)){
			userLevelEnabled = ngaiProps.get(Boolean.class, USER_LEVEL_ENABLED);
		}
		if(ngaiProps.containsKey(ROLE_LEVEL_ENABLED)){
			roleLevelEnabled = ngaiProps.get(Boolean.class, ROLE_LEVEL_ENABLED);
		}
	}
	
	public static AuthorizationConfig get(){
		
		GuardianConfig config = Guardian.getInstance().getConfiguration();
		return config.getAuthorizationConfig();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getUnauthorizedPage() {
		return unauthorizedPage;
	}
	
	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public boolean isRegexEnabled() {
		return regexEnabled;
	}

	public boolean isUserLevelEnabled() {
		return userLevelEnabled;
	}

	public boolean isRoleLevelEnabled() {
		return roleLevelEnabled;
	}
	
}
