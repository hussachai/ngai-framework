package com.siberhus.ngai.guardian.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.annot.Scope;
import com.siberhus.ngai.annot.ServiceBean;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.config.AuthorizationConfig;
import com.siberhus.ngai.guardian.config.GuardianConfig;
import com.siberhus.ngai.guardian.dao.IRolePermissionDao;
import com.siberhus.ngai.guardian.dao.IUserPermissionDao;
import com.siberhus.ngai.guardian.dao.impl.RolePermissionDao;
import com.siberhus.ngai.guardian.dao.impl.UserPermissionDao;
import com.siberhus.ngai.guardian.exception.AuthenticationException;
import com.siberhus.ngai.guardian.exception.AuthorizationException;
import com.siberhus.ngai.guardian.model.AbstractPermission;
import com.siberhus.ngai.guardian.service.IAuthorizationService;

@ServiceBean(scope=Scope.Session)
public class AuthorizationService implements IAuthorizationService, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Map<String, Pattern> ACTION_URI_PATTERN_CACHE = new ConcurrentHashMap<String, Pattern>();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
	
	@InjectDao(implementation=UserPermissionDao.class)
	private transient IUserPermissionDao userPermissionDao;
	
	@InjectDao(implementation=RolePermissionDao.class)
	private transient IRolePermissionDao rolePermissionDao;
	
	private Map<String, Map<String, Boolean>> permCache = new HashMap<String, Map<String,Boolean>>(); 
	
	@Override
	public void clearCache(){
		permCache.clear();
	}
	
	@Override
	public void checkPermission(String requestActionUri, String requestEventName, SessionUser sessionUser) {
		
		AuthorizationConfig authzConfig = AuthorizationConfig.get();
		
		if(! authzConfig.isEnabled()){
			LOGGER.warn("AuthorizationService is disabled!!! "+
					"Anyone can access any parts of the system without restriction. ");
			return;
		}
		
		if(sessionUser == null){
			throw new AuthenticationException("User session not found");
		}
		
		if(GuardianConfig.get().isRootAccessEnabled()){
			if(GuardianConfig.get().getRootUsername()
					.equals(sessionUser.getUsername())){
				return;
			}
		}
		
		boolean allowAccess = false;
		
		boolean cacheHit = false;
		Map<String, Boolean> eventPermCache = null;
		
		if(authzConfig.isCacheEnabled()){
			
			eventPermCache = permCache.get(requestActionUri);
			if(eventPermCache==null){
				eventPermCache = new HashMap<String, Boolean>();
			}
			
			Boolean allowEvent = eventPermCache.get(requestEventName);
			if(allowEvent!=null){
				allowAccess = allowEvent.booleanValue();
				cacheHit = true;
			}
		}
		
		if(!cacheHit){
			//If permission cache not found, do this
			
			boolean skipRolePermCheck = false;
			
			if(authzConfig.isUserLevelEnabled()){
				List<? extends AbstractPermission> userPermList = null;
				if(authzConfig.isRegexEnabled()){
					userPermList = userPermissionDao.findByUserId(sessionUser.getUserId());
				}else{
					userPermList = userPermissionDao.findByUserIdAndActionUri(sessionUser.getUserId(), requestActionUri);
				}
				
				if(userPermList.size()!=0){
					allowAccess = hasPermission(userPermList, requestActionUri, requestEventName);
					skipRolePermCheck = true;
				}
			}else{
				LOGGER.debug("User Level Authorization is disabled");
			}
			
			if(!skipRolePermCheck){
				if(authzConfig.isRoleLevelEnabled()){
					List<? extends AbstractPermission> rolePermList = null;
					if(authzConfig.isRegexEnabled()){
						rolePermList = rolePermissionDao.findUniqueByRoleIdList(sessionUser.getRoleIdList());
					}else{
						rolePermList = rolePermissionDao.findUniqueByRoleIdListAndActionUri(sessionUser.getRoleIdList(), requestActionUri);
					}
					
					allowAccess = hasPermission(rolePermList, requestActionUri, requestEventName);
					
				}else{
					LOGGER.debug("Role Level Authorization is disabled");
				}
			}
			
			if(eventPermCache!=null){
				eventPermCache.put(requestEventName, allowAccess);
			}
			
		}
		
		if(allowAccess){
			LOGGER.debug("User: {} has permission to do {} on {}", new Object[]{sessionUser.getUsername(),requestEventName,requestActionUri});
		}else{
			LOGGER.debug("User: {} does not has permission to do {} on {}", new Object[]{sessionUser.getUsername(),requestEventName,requestActionUri});
			throw new AuthorizationException("User: "+sessionUser.getUsername()+
					" doesn't have a permission to call an event: '"+requestEventName+"'"
					+" on actionBean: '"+requestActionUri);
		}
		
	}
	
	protected boolean regexMatchesActionUri(AbstractPermission permission, String requestActionUri){
		
		Pattern actionUriPattern = ACTION_URI_PATTERN_CACHE.get(permission.getActionUri());
		if(actionUriPattern == null){
			actionUriPattern = Pattern.compile(permission.getActionUri());
			ACTION_URI_PATTERN_CACHE.put(permission.getActionUri(), actionUriPattern);
		}
		if(actionUriPattern.matcher(requestActionUri).matches()){
			return true;
		}
		return false;
	}
	
	
	protected boolean hasPermission(List<? extends AbstractPermission> permissionList, 
			String requestActionUri, String requestEventName){
		
		AuthorizationConfig config = AuthorizationConfig.get();
		
		if(config.isRegexEnabled()){
			for(AbstractPermission permission : permissionList ){
				if(regexMatchesActionUri(permission, requestActionUri)){
					if(permission.isAllowAllEvents() || 
							ArrayUtils.contains(permission.getEventNames(), requestEventName)){
						return true;
					}
				}
			}
		}else{
			for(AbstractPermission permission : permissionList ){
				if(StringUtils.equals(permission.getActionUri(), requestActionUri)){
					if(permission.isAllowAllEvents() || 
							ArrayUtils.contains(permission.getEventNames(), requestEventName)){
						return true;
					}
				}
			}
		}
		return false;
	}
}
