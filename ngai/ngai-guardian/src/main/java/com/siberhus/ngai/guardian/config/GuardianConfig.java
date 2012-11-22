package com.siberhus.ngai.guardian.config;

import java.io.Serializable;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.guardian.Guardian;

public class GuardianConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PREFIX = "guardian.";
	
	public static final String ADMIN_EMAIL = PREFIX+"adminEmail";
	
	public static final String ROOT_ACCESS_ENABLED = PREFIX+"rootAccessEnabled";
	
	public static final String ROOT_USERNAME = PREFIX+"rootUsername";
	
	public static final String ROOT_INIT_PASSWORD = PREFIX+"rootInitPassword";
	
	private AuthenticationConfig authenticationConfig;

	private AuthorizationConfig authorizationConfig;
	
	private AccessFilterConfig accessFilterConfig;

	private String adminEmail;

	private boolean rootAccessEnabled = true; 
	
	private String rootUsername = "root";
	
	protected GuardianConfig(StringMap ngaiProps) {
		
		authenticationConfig = new AuthenticationConfig(ngaiProps);
		authorizationConfig = new AuthorizationConfig(ngaiProps);
		accessFilterConfig = new AccessFilterConfig(ngaiProps);

		adminEmail = ngaiProps.getString(ADMIN_EMAIL);
		rootAccessEnabled = ngaiProps.get(Boolean.class, ROOT_ACCESS_ENABLED, rootAccessEnabled);
		rootUsername = ngaiProps.getString(ROOT_USERNAME, rootUsername);
	}
	
	public static GuardianConfig get(){
		
		return Guardian.getInstance().getConfiguration();
	}
	
	public AuthenticationConfig getAuthenticationConfig() {
		return authenticationConfig;
	}
	
	public AuthorizationConfig getAuthorizationConfig() {
		return authorizationConfig;
	}
	
	public AccessFilterConfig getAccessFilterConfig() {
		return accessFilterConfig;
	}
	
	public String getAdminEmail() {
		return adminEmail;
	}

	public boolean isRootAccessEnabled() {
		return rootAccessEnabled;
	}
	
	public String getRootUsername(){
		return rootUsername;
	}
	
}
