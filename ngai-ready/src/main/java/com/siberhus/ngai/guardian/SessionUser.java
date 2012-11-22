package com.siberhus.ngai.guardian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.util.Log;

import com.siberhus.ngai.guardian.config.AuthenticationConfig;
import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.model.User;

public class SessionUser implements Serializable, HttpSessionBindingListener{
	
	public static final String USER_SESSION_ATTR = "_NgaiGuardian.User";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Log LOG = Log.getInstance(SessionUser.class);
	
	private String ipAddress;
	
	private User realUserObject;
	
	private Long userId;
	
	private List<Long> roleIdList;
	
	private String username;
	
	private boolean auditTrailEnabled = false;
	
	public SessionUser(String ipAddress, User user){
		this.ipAddress = ipAddress;
		this.userId  = user.getId();
		this.username = user.getUsername();
		if(user.getRoleSet()!=null){
			roleIdList = new ArrayList<Long>();
			for(Role role : user.getRoleSet()){
				roleIdList.add(role.getId());
			}
		}
		this.auditTrailEnabled = user.getAuditTrailEnabled();
		this.realUserObject = user;
	}
	
	public static SessionUser getSessionUser(ActionBean actionBean){
		return (SessionUser)actionBean.getContext().getRequest()
			.getSession().getAttribute(USER_SESSION_ATTR);
	}
	
	public User getRealUserObject(){
		return realUserObject;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}
	
	public List<Long> getRoleIdList(){
		return roleIdList;
	}
	
	public boolean isAuditTrailEnabled() {
		return auditTrailEnabled;
	}
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		
		SessionUser.boundUser(getIpAddress(), getUserId(), getUsername());
	}
	
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		
		SessionUser.unboundUser(getIpAddress(), getUserId(), getUsername());
	}
	
	private static void boundUser(String ipAddress, Long userId, String username){
		
		LOG.info("Username: ", username," logged in successfully.");
		
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		
		if(!authConfig.isMultiSessionLoginEnabled()){
			Guardian.getInstance().addLoginUser(username, ipAddress);
		}
		
	}
	
	private static void unboundUser(String ipAddress, Long userId, String username){
		
		LOG.info("Username: ", username, " was logged out.");
		
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		
		if(!authConfig.isMultiSessionLoginEnabled()){
			Guardian.getInstance().removeLoginUser(username);
		}
		
	}
	
	
}
