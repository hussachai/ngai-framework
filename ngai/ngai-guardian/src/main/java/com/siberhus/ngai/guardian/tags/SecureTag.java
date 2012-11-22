package com.siberhus.ngai.guardian.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagSupport;

import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.core.WebContext;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.service.IAuthorizationService;
import com.siberhus.ngai.guardian.service.impl.AuthorizationService;

public abstract class SecureTag extends TagSupport {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    protected IAuthorizationService getAuthorizationService() {
    	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    	HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
    	WebContext webContext = new WebContext(request, response);
    	return (IAuthorizationService)ServiceBeanRegistry
		.get(webContext, IAuthorizationService.class, AuthorizationService.class);
    }
    
    protected SessionUser getSessionUser(){
    	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    	return (SessionUser)request.getSession()
    		.getAttribute(SessionUser.USER_SESSION_ATTR);
    }
}



