package com.siberhus.ngai.guardian.action;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.siberhus.commons.lang.ClassUtils;
import com.siberhus.ngai.action.AbstractAction;
import com.siberhus.ngai.action.DefaultUserAware;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.exception.ServiceException;
import com.siberhus.ngai.guardian.LoginContext;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.config.AuthenticationConfig;
import com.siberhus.ngai.guardian.config.AuthenticationConfig.RememberMe;
import com.siberhus.ngai.guardian.service.IAuthenticationService;
import com.siberhus.ngai.guardian.service.impl.AuthenticationService;
import com.siberhus.ngai.localization.SessionLocalePicker;
import com.siberhus.ngai.localization.action.Messages;
import com.siberhus.web.util.CookieUtils;


public abstract class AbstractAuthenticationAction extends AbstractAction 
	implements IAuthenticationAction{
	
	public static final String PREFERRED_LANG_ATTR = "preferredLanguage";
	
	@InjectService(implementation=AuthenticationService.class)
	private IAuthenticationService authenticationService;
	
	private RememberMe rememberMeConfig;
	
	@Validate(required=true)
	private String username;
	
	@Validate(required=true)
	private String password;
	
	private Boolean rememberMe;
	
	private String loginPage;
	
	private String successPage;
	
	private String failPage;
	
	private String targetAction;
	
	private String language;
	
	public AbstractAuthenticationAction(){
		
		AuthenticationConfig authConfig = AuthenticationConfig.get();
		loginPage = authConfig.getLoginPage();
		successPage = authConfig.getSuccessPage();
		failPage = authConfig.getFailPage();
		rememberMeConfig = authConfig.getRememberMe();
	}

	public void loginSucceed(LoginContext loginContext, SessionUser sessionUser){}
	
	public void loginFail(LoginContext loginContext, Throwable e){}
	
	@Override
	public final String getPathPrefix(){
		return "";
	}
	
	public String getIndexPage(){
		return loginPage;
	}
	
	@DefaultHandler
	@DontValidate
	public Resolution index(){
		
		if(rememberMeConfig.isEnabled()){
			Cookie userCookie = CookieUtils.get(getContext().getRequest(), 
					rememberMeConfig.getUserCookieName());
			Cookie passwdCookie = CookieUtils.get(getContext().getRequest(), 
					rememberMeConfig.getPasswdCookieName());
			Cookie langCookie = CookieUtils.get(getContext().getRequest(), 
					PREFERRED_LANG_ATTR);
			if(userCookie==null || passwdCookie==null){
				return super.index();
			}
			this.username = CryptoUtil.decrypt(userCookie.getValue());
			this.password = CryptoUtil.decrypt(passwdCookie.getValue());
			this.rememberMe = false;
			if(langCookie!=null){
				this.language = langCookie.getValue();
			}else{
				this.language = getContext().getRequest()
					.getLocale().getLanguage();
			}
			return login();
		}
		return super.index();
	}
	
	@ValidationMethod(on="login")
	public void validatePassword(){
		PasswordValidatorUtil.validate(this, "password", password);
	}
	
	@SuppressWarnings("unchecked")
	public Resolution login(){
		
		SessionUser sessionUser = null;
		
		HttpServletRequest request = getContext().getRequest();
		HttpSession session = request.getSession();
		
		sessionUser = (SessionUser)session.getAttribute(
				SessionUser.USER_SESSION_ATTR);
		
		if(sessionUser!=null){
			session.invalidate();
		}
		
		session = request.getSession();
		
		LoginContext loginContext = createLoginContext();
		
		try{
			
			sessionUser = authenticationService.login(loginContext);
			session.setAttribute(SessionUser.USER_SESSION_ATTR, sessionUser);
			
			Object user = sessionUser.getRealUserObject();
			session.setAttribute(DefaultUserAware.USER_ATTRIBUTE_NAME, user);
			
			Locale preferredLocale = SessionLocalePicker.setUserPreferredLocale(request, getLanguage());
			if(language==null){
				language = preferredLocale.getLanguage();
			}
			session.setAttribute(PREFERRED_LANG_ATTR, language);
			
			if(rememberMeConfig.isEnabled()){
				int maxAge = rememberMeConfig.getMaxAge()*3600*24;
				String cookiePath = request.getContextPath();
				if(getRememberMe()!=null && getRememberMe().equals(Boolean.TRUE)){
					String encrypedUsername = CryptoUtil.encrypt(username);
					String encrypedPasswd = CryptoUtil.encrypt(password);
					Cookie userCookie = new Cookie(rememberMeConfig
							.getUserCookieName(), encrypedUsername);
					userCookie.setMaxAge(maxAge);
					userCookie.setPath(cookiePath);
					Cookie passwdCookie = new Cookie(rememberMeConfig
							.getPasswdCookieName(), encrypedPasswd);
					passwdCookie.setMaxAge(maxAge);
					passwdCookie.setPath(cookiePath);
					Cookie langCookie = new Cookie(PREFERRED_LANG_ATTR, language);
					langCookie.setMaxAge(maxAge);
					langCookie.setPath(cookiePath);
					getContext().getResponse().addCookie(userCookie);
					getContext().getResponse().addCookie(passwdCookie);
					getContext().getResponse().addCookie(langCookie);
				}
			}
			loginSucceed(loginContext, sessionUser);
			
		}catch(ServiceException e){
			
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			Messages.addError(this, rootCause.getLocalizedMessage());
			
			loginFail(loginContext, rootCause);
			
			if(failPage!=null){
				return new ForwardResolution(failPage);
			}
			return new ForwardResolution(loginPage);
		}
		
		if(getTargetAction()!=null){
			Class<? extends ActionBean> actionClass = null;
			try {
				actionClass = ClassUtils.loadClass(getTargetAction());
			} catch (ClassNotFoundException e) {
				getContext().getValidationErrors()
					.addGlobalError(new SimpleError(e.getMessage()));
			}
			return new RedirectResolution(actionClass);
		}
		
		return new ForwardResolution(successPage);
	}
	
	@DontBind
	public Resolution logout(){
		SessionUser sessionUser = SessionUser.getSessionUser(this);
		if(sessionUser!=null){
			authenticationService.logout(sessionUser);
		}
		
		getContext().getRequest().getSession().invalidate();
		
		CookieUtils.delete(getContext().getRequest(), rememberMeConfig.getUserCookieName());
		CookieUtils.delete(getContext().getRequest(),rememberMeConfig.getPasswdCookieName());
		CookieUtils.delete(getContext().getRequest(), PREFERRED_LANG_ATTR);
		getContext().getRequest().setAttribute("logout", true);
		return new ForwardResolution(loginPage);
	}
	
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
	
	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	
	public String getTargetAction() {
		return targetAction;
	}

	public void setTargetAction(String targetAction) {
		this.targetAction = targetAction;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	protected LoginContext createLoginContext(){
		
		HttpServletRequest request = getContext().getRequest();
		
		LoginContext loginContext = new LoginContext();
		
		loginContext.setUsername(getUsername());
		loginContext.setPassword(getPassword());
		loginContext.setLoginAt(new Date());
		loginContext.setIpAddress(request.getRemoteAddr());
		loginContext.setSecureLogin(request.isSecure());
		loginContext.setLocale(request.getLocale());
		loginContext.setUserAgent(request.getHeader("User-Agent"));
		loginContext.setAcceptCharset(request.getHeader("Accept-Charset"));
		loginContext.setAcceptEncoding(request.getHeader("Accept-Encoding"));
		loginContext.setAcceptLanguage(request.getHeader("Accept-Language"));
		
		return loginContext;
	}
}
