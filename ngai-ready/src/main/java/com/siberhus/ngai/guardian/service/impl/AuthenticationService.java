package com.siberhus.ngai.guardian.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.exception.NgaiRuntimeException;
import com.siberhus.ngai.guardian.Guardian;
import com.siberhus.ngai.guardian.LoginContext;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.config.AuthenticationConfig;
import com.siberhus.ngai.guardian.dao.IAuthenticationDao;
import com.siberhus.ngai.guardian.dao.ILoginFailureLogDao;
import com.siberhus.ngai.guardian.dao.ILoginLogDao;
import com.siberhus.ngai.guardian.dao.impl.AuthenticationDao;
import com.siberhus.ngai.guardian.dao.impl.LoginFailureLogDao;
import com.siberhus.ngai.guardian.dao.impl.LoginLogDao;
import com.siberhus.ngai.guardian.exception.AuthenticationException;
import com.siberhus.ngai.guardian.exception.GuardianException;
import com.siberhus.ngai.guardian.model.LoginFailureLog;
import com.siberhus.ngai.guardian.model.LoginLog;
import com.siberhus.ngai.guardian.model.Role;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.guardian.service.IAuthenticationService;

public class AuthenticationService implements IAuthenticationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
	
	@InjectDao(implementation=AuthenticationDao.class)
	private IAuthenticationDao authenticationDao;
	
	@InjectDao(implementation=LoginLogDao.class)
	private ILoginLogDao userLoginLogDao;
	
	@InjectDao(implementation=LoginFailureLogDao.class)
	private ILoginFailureLogDao loginFailureDao;
	
	/**
	 * Caution! Don't use this method in initialized state of framework.
	 * @param password
	 * @return
	 */
	public static String encryptPassword(String username, String password){
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		boolean isAppendUser = authConfig.isAppendUserInPasswd();
		if(isAppendUser){
			password = password+username;
		}
		switch(authConfig.getPasswdHashAlgo()){
		case SHA:
			password = DigestUtils.shaHex(password);
			break;
		case MD5:
			password = DigestUtils.shaHex(password);
			break;
		}
		return password;
	}
	
	@Override
	public SessionUser login(LoginContext loginContext) {
		
		Ngai.beginTransactionSafely();//TODO: Manual Trx will be removed in 1.0
		
		try{
			
			SessionUser sessUser = authenticate(loginContext);
			
			createLoginLog(sessUser, loginContext);
			
			updateLoginFailure(loginContext.getIpAddress()
					,loginContext.getLoginAt(),true);
			
			return sessUser;
		}catch(GuardianException e){
			
			updateLoginFailure(loginContext.getIpAddress()
					,loginContext.getLoginAt(),false);
			throw e;
		}catch(Exception e){
			throw new NgaiRuntimeException(e.getMessage(), e);
		}finally{
			
			Ngai.getEntityManager().getTransaction().commit();//TODO: Manual Trx will be removed in 1.0
		}
		
	}
	
	@Override
	public void logout(SessionUser sessionUser){
		
		if(sessionUser==null){
			throw new IllegalArgumentException("SessionUser cannot be null");
		}
		
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		
		if(!authConfig.isLoginLogEnabled()){
			LOGGER.debug("LoginLog is disabled");
			return;
		}
		
		Ngai.beginTransactionSafely();//Will be removed in 1.0
		
		LoginLog loginLog = userLoginLogDao.findLastLogByUserId(sessionUser.getUserId());
		if(loginLog!=null){
			loginLog.setLogoutAt(new Date());
		}else{
			throw new NgaiRuntimeException("This couldn't possible");
		}
		
		Ngai.getEntityManager().getTransaction().commit();//Will be removed in 1.0
	}
	
	@Override
	public User getAuthenticatedUser(SessionUser sessionUser) {
		
		return authenticationDao.findUserByUsername(sessionUser.getUsername());
	}
	
	protected SessionUser authenticate(LoginContext loginContext){
		
		final AuthenticationConfig authConfig = AuthenticationConfig.get();
		
		String ipAddress = loginContext.getIpAddress();
		String username = loginContext.getUsername();
		String password = loginContext.getPassword();
		
		if(!authConfig.isEnabled()){
			LOGGER.warn("AuthenticationService is disabled!!! "+
			"Anyone can login to the system without username/password validation.");
			return null;
		}
		
		if(!authConfig.isMultiSessionLoginEnabled()){
			
			if(Guardian.getInstance().isMultiAddressesLogin(username, ipAddress)){
				throw new AuthenticationException("This account\'ve already logged in in another session "+
						Guardian.getInstance().getLastLoginAddress(username));
			}
		}
		
		User user = authenticationDao.findUserByUsername(username);
		
		if(!user.getActive()){
			throw new AuthenticationException("User is not active");
		}
		
		String inputPassword = encryptPassword(username, password);
		
		if(!inputPassword.equals(user.getPassword())){
			throw new AuthenticationException("Password is incorrect");
		}
		
		List<Long> roleIdList = null;
		if(user.getRoleSet()!=null){
			roleIdList = new ArrayList<Long>();
			for(Role role : user.getRoleSet()){
				roleIdList.add(role.getId());
			}
		}
		
		SessionUser sessionUser = new SessionUser(ipAddress, user);
		
		return sessionUser;
	}
	
	protected void createLoginLog(SessionUser sessionUser, LoginContext loginContext){
		
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		
		if(!authConfig.isLoginLogEnabled()){
			LOGGER.debug("LoginLog is disabled");
			return;
		}
		LoginLog loginLog = new LoginLog();
		loginLog.setUserId(sessionUser.getUserId());
		loginLog.setUsername(sessionUser.getUsername());
		loginLog.setLoginAt(loginContext.getLoginAt());
		loginLog.setIpAddress(loginContext.getIpAddress());
		loginLog.setSecureLogin(loginContext.isSecureLogin());
		loginLog.setLocale(loginContext.getLocale().toString());
		loginLog.setUserAgent(loginContext.getUserAgent());
		loginLog.setAcceptCharset(loginContext.getAcceptCharset());
		loginLog.setAcceptEncoding(loginContext.getAcceptEncoding());
		loginLog.setAcceptLanguage(loginContext.getAcceptLanguage());
		
		userLoginLogDao.save(loginLog);
		
	}
	
	protected void updateLoginFailure(String ipAddress, Date loginDate, boolean clear){
		
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		
		if(authConfig.getRetryLimit()==AuthenticationConfig.UNLIMITED_RETRY){
			LOGGER.debug("LoginFailure checker is disabled");
			return;
		}
		
		LoginFailureLog loginFailure = loginFailureDao.findByIpAddress(ipAddress);
		
		if(clear){
			if(loginFailure!=null){
				loginFailureDao.delete(loginFailure);
			}
			return;
		}
		
		if(loginFailure==null){
			loginFailure = new LoginFailureLog(ipAddress);
		}
		loginFailure.setLastAttemptAt(loginDate);
		
		int attemptCount = loginFailure.getAttemptCount();
		
		if(attemptCount >= authConfig.getRetryLimit()){
			throw new AuthenticationException("Login attempt exceed the retryLimit: "
					+authConfig.getRetryLimit());
		}
		
		loginFailure.setAttemptCount(attemptCount+1);
		
		loginFailureDao.save(loginFailure);
		
	}

	
}
