package com.siberhus.ngai.guardian.config;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.guardian.Guardian;

public class AccessFilterConfig implements Serializable{
	
	public static final String PREFIX = GuardianConfig.PREFIX+"accessFilter.";
	public static final String ENABLED = PREFIX+"enabled";
	public static final String ACCESS_DENIED_PAGE = PREFIX+"accessDeniedPage";
	public static final String ACCESS_DENIED_LOG_ENABLED = PREFIX+"accessDeniedLogEnabled";
	public static final String BANNED_RESOURCES = PREFIX+"bannedResources";
	public static final String ALLOWED_RESOURCES = PREFIX+"allowedResources";
	public static final String BANNED_ADDRESSES = PREFIX+"bannedAddresses";
	public static final String ALLOWED_ADDRESSES = PREFIX+"allowedAddresses";
	public static final String BANNED_USER_AGENTS = PREFIX+"bannedUserAgents";
	public static final String ALLOWED_USER_AGENTS = PREFIX+"allowedUserAgents";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean enabled = true;
	
	private String accessDeniedPage;
	
	private boolean accessDeniedLogEnabled = true;
	
	private String bannedResources;
	
	private Pattern[] bannedResourcePatterns;
	
	private String allowedResources;
	
	private Pattern[] allowedResourcePatterns;
	
	private String bannedAddresses;
	
	private Pattern[] bannedAddressPatterns;
	
	private String allowedAddresses;
	
	private Pattern[] allowedAddressPatterns;
	
	private String bannedUserAgents;
	
	private Pattern[] bannedUserAgentPatterns;
	
	private String allowedUserAgents;
	
	private Pattern[] allowedUserAgentPatterns;
	
	public AccessFilterConfig(StringMap ngaiProps){
		if(ngaiProps.containsKey(ENABLED)){
			this.enabled = ngaiProps.get(Boolean.class, ENABLED);
		}
		
		this.accessDeniedPage = ngaiProps.getString(ACCESS_DENIED_PAGE);
		
		if(ngaiProps.containsKey(ACCESS_DENIED_LOG_ENABLED)){
			this.accessDeniedLogEnabled = ngaiProps.get(Boolean.class, ACCESS_DENIED_LOG_ENABLED);
		}
		if(ngaiProps.containsKey(BANNED_RESOURCES)){
			this.bannedResources = ngaiProps.getString(BANNED_RESOURCES);
			this.bannedResourcePatterns = ngaiProps.getSplitValue(Pattern.class, BANNED_RESOURCES, ',');
		}
		if(ngaiProps.containsKey(ALLOWED_RESOURCES)){
			this.allowedResources = ngaiProps.getString(ALLOWED_RESOURCES);
			this.allowedResourcePatterns = ngaiProps.getSplitValue(Pattern.class, ALLOWED_RESOURCES, ',');
		}
		if(ngaiProps.containsKey(BANNED_ADDRESSES)){
			this.bannedAddresses = ngaiProps.getString(BANNED_ADDRESSES);
			this.bannedAddressPatterns = ngaiProps.getSplitValue(Pattern.class, BANNED_ADDRESSES, ',');
		}
		if(ngaiProps.containsKey(ALLOWED_ADDRESSES)){
			this.allowedAddresses = ngaiProps.getString(ALLOWED_ADDRESSES);
			this.allowedAddressPatterns = ngaiProps.getSplitValue(Pattern.class, ALLOWED_ADDRESSES, ',');
		}
		if(ngaiProps.containsKey(BANNED_USER_AGENTS)){
			this.bannedUserAgents = ngaiProps.getString(BANNED_USER_AGENTS);
			this.bannedUserAgentPatterns = ngaiProps.getSplitValue(Pattern.class, BANNED_USER_AGENTS, ',');
		}
		if(ngaiProps.containsKey(ALLOWED_USER_AGENTS)){
			this.allowedUserAgents = ngaiProps.getString(ALLOWED_USER_AGENTS);
			this.allowedUserAgentPatterns = ngaiProps.getSplitValue(Pattern.class, ALLOWED_USER_AGENTS, ',');
		}
	}

	public static AccessFilterConfig get(){
		
		GuardianConfig config = Guardian.getInstance().getConfiguration();
		return config.getAccessFilterConfig();
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public String getAccessDeniedPage() {
		return accessDeniedPage;
	}
	
	public boolean isAccessDeniedLogEnabled() {
		return accessDeniedLogEnabled;
	}

	public String getBannedResources() {
		return bannedResources;
	}

	public Pattern[] getBannedResourcePatterns() {
		return bannedResourcePatterns;
	}

	public String getAllowedResources() {
		return allowedResources;
	}

	public Pattern[] getAllowedResourcePatterns() {
		return allowedResourcePatterns;
	}

	public String getBannedAddresses() {
		return bannedAddresses;
	}

	public Pattern[] getBannedAddressPatterns() {
		return bannedAddressPatterns;
	}

	public String getAllowedAddresses() {
		return allowedAddresses;
	}

	public Pattern[] getAllowedAddressPatterns() {
		return allowedAddressPatterns;
	}

	public String getBannedUserAgents() {
		return bannedUserAgents;
	}

	public Pattern[] getBannedUserAgentPatterns() {
		return bannedUserAgentPatterns;
	}

	public String getAllowedUserAgents() {
		return allowedUserAgents;
	}

	public Pattern[] getAllowedUserAgentPatterns() {
		return allowedUserAgentPatterns;
	}
	
	
}
