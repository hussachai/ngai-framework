package com.siberhus.ngai.guardian;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.exception.NgaiException;
import com.siberhus.ngai.guardian.config.GuardianConfig;

public class Guardian {
	
	/**
	 * Map of Username and IP Address
	 */
	private Map<String, String> loginUserMap;
	
	/**
	 * Map of ActionURI and ActionBeanDetail
	 */
	private Map<String, ActionBeanDetail> actionBeanDetailMap; 
	
	private Map<String, String> actionUriMap;
	
	private GuardianConfig configuration;
	
	private static Guardian instance;
	
	protected Guardian(StringMap ngaiProps) throws NgaiException {
		
		this.configuration = new GuardianConfig(ngaiProps){
			private static final long serialVersionUID = 1L;
		};
		
		loginUserMap = new ConcurrentHashMap<String, String>();
		
		actionBeanDetailMap = new HashMap<String, ActionBeanDetail>();
		
		actionUriMap = new HashMap<String, String>();
		
		synchronized(this){
			if(Guardian.instance == null){
				Guardian.instance = this;
			}else{
				throw new IllegalStateException("User cannot create this instance directly");
			}
		}
	}
	
	public static Guardian getInstance(){
		return instance;
	}
	
	public GuardianConfig getConfiguration(){
		return configuration;
	}
	
	public String getLastLoginAddress(String username){
		return loginUserMap.get(username);
	}
	
	public void addLoginUser(String username, String loginIpAddress){
		loginUserMap.put(username, loginIpAddress);
	}
	
	public void removeLoginUser(String username){
		loginUserMap.remove(username);
	}
	
	public boolean isMultiAddressesLogin(String username, String loginIpAddress){
		String oldIpAddress = loginUserMap.get(username);
		if(oldIpAddress!=null){
			//This user has logged in, next step is comparing ipAddress
			if(loginIpAddress.equals(oldIpAddress)){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	public Map<String, ActionBeanDetail> getActionBeanDetailMap() {
		return actionBeanDetailMap;
	}

	public Map<String, String> getActionUriMap() {
		return actionUriMap;
	}
	
	
}
