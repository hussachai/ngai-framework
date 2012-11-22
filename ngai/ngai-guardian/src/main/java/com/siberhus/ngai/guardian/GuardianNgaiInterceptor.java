package com.siberhus.ngai.guardian;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.UrlBindingFactory;
import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.commons.util.ElapsedTimeUtils;
import com.siberhus.commons.util.StringMap;
import com.siberhus.ngai.core.DaoBeanRegistry;
import com.siberhus.ngai.core.INgaiInterceptor;
import com.siberhus.ngai.core.Ngai;
import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.core.WebContext;
import com.siberhus.ngai.exception.ServiceException;
import com.siberhus.ngai.guardian.action.IAuthenticationAction;
import com.siberhus.ngai.guardian.config.AuthenticationConfig;
import com.siberhus.ngai.guardian.config.AuthorizationConfig;
import com.siberhus.ngai.guardian.config.GuardianConfig;
import com.siberhus.ngai.guardian.dao.impl.AccessDeniedLogDao;
import com.siberhus.ngai.guardian.dao.impl.AuthenticationDao;
import com.siberhus.ngai.guardian.dao.impl.LoginFailureLogDao;
import com.siberhus.ngai.guardian.dao.impl.LoginLogDao;
import com.siberhus.ngai.guardian.dao.impl.RolePermissionDao;
import com.siberhus.ngai.guardian.dao.impl.UserAuditLogDao;
import com.siberhus.ngai.guardian.dao.impl.UserPermissionDao;
import com.siberhus.ngai.guardian.exception.AuthenticationException;
import com.siberhus.ngai.guardian.exception.AuthorizationException;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.guardian.model.UserAuditLog;
import com.siberhus.ngai.guardian.service.IAuthorizationService;
import com.siberhus.ngai.guardian.service.IUserAuditLogService;
import com.siberhus.ngai.guardian.service.impl.AccessDeniedLogService;
import com.siberhus.ngai.guardian.service.impl.AuthenticationService;
import com.siberhus.ngai.guardian.service.impl.AuthorizationService;
import com.siberhus.ngai.guardian.service.impl.LoginFailureLogService;
import com.siberhus.ngai.guardian.service.impl.RolePermissionService;
import com.siberhus.ngai.guardian.service.impl.UserAuditLogService;
import com.siberhus.ngai.guardian.service.impl.UserPermissionService;


public class GuardianNgaiInterceptor implements INgaiInterceptor.ActionBeanResolutionPhase  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuardianNgaiInterceptor.class);
	
	public GuardianNgaiInterceptor(){}
	
	@Override
	public void init(Configuration config) throws Exception {
		
		new Guardian(Ngai.getInstance().getNgaiProperties()){
			private static final long serialVersionUID = 1L;
		};
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>"+Guardian.getInstance());
		LOGGER.debug("Initializing Ngai Guardian DAO");
		DaoBeanRegistry.register(AccessDeniedLogDao.class);
		DaoBeanRegistry.register(AuthenticationDao.class);
		DaoBeanRegistry.register(LoginFailureLogDao.class);
		DaoBeanRegistry.register(RolePermissionDao.class);
		DaoBeanRegistry.register(UserAuditLogDao.class);
		DaoBeanRegistry.register(LoginLogDao.class);
		DaoBeanRegistry.register(UserPermissionDao.class);
		LOGGER.debug("All Ngai Guardian DAOs were registered successfully");
		
		LOGGER.debug("Initializing Ngai Guardian Service");
		ServiceBeanRegistry.register(AccessDeniedLogService.class);
		ServiceBeanRegistry.register(AuthenticationService.class);
		ServiceBeanRegistry.register(AuthorizationService.class);
		ServiceBeanRegistry.register(LoginFailureLogService.class);
		ServiceBeanRegistry.register(RolePermissionService.class);
		ServiceBeanRegistry.register(UserAuditLogService.class);
		ServiceBeanRegistry.register(UserPermissionService.class);
		LOGGER.debug("All Ngai Guardian Services were registered successfully");
		
		initActionBeanDetailMap(config);
		initRootUser();
	}
	
	@Override
	public void beforeActionBeanResolution(ExecutionContext context) {}
	
	@Override
	public void afterActionBeanResolution(ExecutionContext context) {
        
		//login action page doesn't need to be checked user permission
		if(context.getActionBean() instanceof IAuthenticationAction){
			return;
		}
		
		Class<? extends ActionBean> actionBeanClass = context.getActionBean().getClass();
		String eventName = context.getActionBeanContext().getEventName();
		
		String actionBeanName = actionBeanClass.getName();
		String actionUri = Guardian.getInstance().getActionUriMap().get(actionBeanName);
		
		HttpServletRequest request = context.getActionBeanContext().getRequest();
		
		HttpServletResponse response = context.getActionBeanContext().getResponse();
		HttpSession session = request.getSession();
		
		SessionUser sessionUser = (SessionUser)session.getAttribute(
				SessionUser.USER_SESSION_ATTR);
		
		WebContext webContext = new WebContext(request, response);
		
		IAuthorizationService authorizationService = (IAuthorizationService)ServiceBeanRegistry
			.get(webContext, IAuthorizationService.class, AuthorizationService.class);
		
		Exception unauthorizedException = null;
		
		try{
			
			authorizationService.checkPermission(actionUri, eventName, sessionUser);
			
		}catch(ServiceException e){
			
			Throwable cause = e.getCause();
			LOGGER.info(cause.toString());
			context.getActionBeanContext().getValidationErrors()
				.addGlobalError(new SimpleError(e.getMessage()));
			
			String loginPage = AuthenticationConfig.get().getLoginPage();
			if(!loginPage.contains("?")){
				loginPage += "?";
			}else{
				loginPage += "&";
			}
			loginPage += "targetAction="+actionBeanName;
			
			String unauthorizedPage = AuthorizationConfig.get().getUnauthorizedPage();
			if(unauthorizedPage==null){
				unauthorizedPage = loginPage;
			}
			
			if(cause instanceof AuthorizationException){
				
				unauthorizedException = e;
				context.setResolution(new RedirectResolution(unauthorizedPage));
			}else if(cause instanceof AuthenticationException){
				
				context.setResolution(new RedirectResolution(loginPage));
			}else{
				//rethrow exception, if it's not AuthorizationException
				throw e; 
			}
		}finally{
			
			boolean forbiddenAction = unauthorizedException!=null;
			auditTrails(sessionUser, actionUri, eventName, forbiddenAction);
			
		}
		
	}
	
	
	protected void auditTrails(SessionUser sessionUser,String actionUri, String eventName, boolean isForbiddenAction){
		
		IUserAuditLogService userActionLogService = (IUserAuditLogService)ServiceBeanRegistry
			.get(IUserAuditLogService.class, UserAuditLogService.class);
		
		//Log user action
		if(sessionUser!=null && sessionUser.isAuditTrailEnabled()){
			
			UserAuditLog userActionLog = new UserAuditLog();
			userActionLog.setActionUri(actionUri);
			userActionLog.setEventName(eventName);
			userActionLog.setUserId(sessionUser.getUserId());
			userActionLog.setUsername(sessionUser.getUsername());
			userActionLog.setForbiddenAction(isForbiddenAction);
			userActionLog.setExecutedAt(new Date());
			
			userActionLogService.save(userActionLog);
		}
	}
	
	
	protected void initActionBeanDetailMap(Configuration config){
		
		ElapsedTimeUtils.start("scanActionBeanForDetail");
		
		Collection<Class<? extends ActionBean>> actionBeanClassCollect = 
			UrlBindingFactory.getInstance().getActionBeanClasses();
		
		for(Class<? extends ActionBean> actionBeanClass : actionBeanClassCollect){
			
			String actionBeanURI = config.getActionResolver()
				.getUrlBinding(actionBeanClass);
			
			if(actionBeanURI.startsWith("/controller")){
				continue;
			}
			actionBeanURI = StringUtils.substringBefore(actionBeanURI,"/{");
			String actionBeanFQCN = actionBeanClass.getName();
			Set<String> eventNameSet = new TreeSet<String>();
			String helperEvents[] = new String[]{"handleValidationErrors","forward","redirect"}; 
			Collection<Method> actionBeanMethodCollect = ReflectUtil.getMethods(actionBeanClass);
			for(Method actionBeanMethod : actionBeanMethodCollect){
				if(Resolution.class.isAssignableFrom(
						actionBeanMethod.getReturnType())){
					if(Modifier.isPublic(actionBeanMethod.getModifiers())){
						String eventName = actionBeanMethod.getName();
						if(ArrayUtils.indexOf(helperEvents, eventName)==-1){
							if(!actionBeanMethod.isAnnotationPresent(Before.class)
									&& !actionBeanMethod.isAnnotationPresent(After.class)){
								eventNameSet.add(actionBeanMethod.getName());
							}
						}
					}
				}
			}
			
			ActionBeanDetail actionBeanDetail = new ActionBeanDetail();
			actionBeanDetail.setActionBeanFQCN(actionBeanFQCN);
			actionBeanDetail.setActionBeanURI(actionBeanURI);
			actionBeanDetail.setEventNameSet(eventNameSet);
			
			Guardian.getInstance().getActionBeanDetailMap().put(actionBeanURI, actionBeanDetail);
			Guardian.getInstance().getActionUriMap().put(actionBeanFQCN, actionBeanURI);
		}
		
		LOGGER.debug("Total scanned ActionBean is {}",
				Guardian.getInstance().getActionBeanDetailMap().size());
		LOGGER.debug("ActionBean scanning elapse: {}",
				ElapsedTimeUtils.showElapsedTime("scanActionBeanForDetail"));
		ElapsedTimeUtils.remove("scanActionBeanForDetail");
		
	}
	
	protected void initRootUser(){
		
		StringMap ngaiProps = Ngai.getInstance().getNgaiProperties();
		GuardianConfig gConfig = GuardianConfig.get();
		if(!gConfig.isRootAccessEnabled()){
			LOGGER.info("Root Access is disabled");
		}
		LOGGER.info("Root Access is enabled");
		
		Ngai.requestInit();
		EntityManager em = Ngai.getEntityManager();
		em.getTransaction().begin();
		
		try{
			em.createQuery("from User u where u.username=?")
			 	.setParameter(1, gConfig.getRootUsername()).getSingleResult();
		}catch(NoResultException e){
			try {
				User user = new User();
				String username = gConfig.getRootUsername();
				String password = ngaiProps.getString(GuardianConfig.ROOT_INIT_PASSWORD);
				password = AuthenticationService.encryptPassword(username, password);
				
				user.setUsername(username);
				user.setPassword(password);
				user.setAuditTrailEnabled(false);
				em.persist(user);
			} catch (Exception e1) {
				LOGGER.error(e1.getMessage(), e1);
			}
		}
		
		em.getTransaction().commit();
		Ngai.requestComplete();
	}
	
}
