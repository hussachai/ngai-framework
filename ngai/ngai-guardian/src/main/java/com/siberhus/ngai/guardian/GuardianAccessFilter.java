package com.siberhus.ngai.guardian;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.core.WebContext;
import com.siberhus.ngai.guardian.config.AccessFilterConfig;
import com.siberhus.ngai.guardian.service.IAccessDeniedLogService;
import com.siberhus.ngai.guardian.service.impl.AccessDeniedLogService;

public class GuardianAccessFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GuardianAccessFilter.class);
	
	@Override
	public void init(FilterConfig config) throws ServletException {}
	
	@Override
	public void doFilter(ServletRequest genericRequest, ServletResponse genericResponse,
			FilterChain chain) throws IOException, ServletException {
		
		AccessFilterConfig accessFilterConfig = AccessFilterConfig.get();
		if(!accessFilterConfig.isEnabled()){
			chain.doFilter(genericRequest, genericResponse);
			return;
		}
		
		HttpServletRequest request = (HttpServletRequest)genericRequest;
		HttpServletResponse response = (HttpServletResponse)genericResponse;
		
		String ipAddress = request.getRemoteAddr();
		String requestURI = request.getRequestURI();
		String userAgent = request.getHeader("User-Agent");
		
		LOGGER.debug("Request IP Address: {}",ipAddress);
		LOGGER.debug("Request URI: {}",requestURI);
		LOGGER.debug("Request UserAgent: {}",userAgent);
		
		Pattern bannedAddressPatterns[] = accessFilterConfig.getBannedAddressPatterns();
		if(bannedAddressPatterns!=null){
			for(Pattern bannedAddressPattern : bannedAddressPatterns){
				if(bannedAddressPattern.matcher(ipAddress).matches()){
					denyAccess(request, response);
					return;
				}
			}
		}
		Pattern allowedAddressPatterns[] = accessFilterConfig.getAllowedAddressPatterns();
		if(allowedAddressPatterns!=null){
			for(Pattern allowedAddressPattern : allowedAddressPatterns){
				if(!allowedAddressPattern.matcher(ipAddress).matches()){
					denyAccess(request, response);
					return;
				}
			}
		}
		
		Pattern bannedResourcePatterns[] = accessFilterConfig.getBannedResourcePatterns();
		if(bannedResourcePatterns!=null){
			for(Pattern bannedResourcePattern : bannedResourcePatterns){
				if(bannedResourcePattern.matcher(requestURI).matches()){
					denyAccess(request, response);
					return;
				}
			}
		}
		Pattern allowedResourcePatterns[] = accessFilterConfig.getAllowedResourcePatterns();
		if(allowedResourcePatterns!=null){
			for(Pattern allowedResourcePattern : allowedResourcePatterns){
				if(!allowedResourcePattern.matcher(requestURI).matches()){
					denyAccess(request, response);
					return;
				}
			}
		}
		
		Pattern bannedUserAgentPatterns[] = accessFilterConfig.getBannedUserAgentPatterns();
		if(bannedUserAgentPatterns!=null){
			for(Pattern bannedUserAgentPattern : bannedUserAgentPatterns){
				if(bannedUserAgentPattern.matcher(userAgent).matches()){
					denyAccess(request, response);
					return;
				}
			}
		}
		Pattern allowedUserAgentPatterns[] = accessFilterConfig.getAllowedUserAgentPatterns();
		if(allowedUserAgentPatterns!=null){
			for(Pattern allowedUserAgentPattern : allowedUserAgentPatterns){
				if(!allowedUserAgentPattern.matcher(userAgent).matches()){
					denyAccess(request, response);
					return;
				}
			}
		}
		
		LOGGER.debug("Allow access from address: {}",ipAddress);
		
		chain.doFilter(genericRequest, genericResponse);
		
	}
	
	@Override
	public void destroy() {}
	
	protected void denyAccess(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		AccessFilterConfig accessFilterConfig = AccessFilterConfig.get();
		
		String accessDeniedPage = accessFilterConfig.getAccessDeniedPage();
		
		String ipAddress = request.getRemoteAddr();
		String requestURI = request.getRequestURI();
		String userAgent = request.getHeader("User-Agent");
		
		LOGGER.warn("Denied access from address: {}",ipAddress);
		
		if(accessFilterConfig.isAccessDeniedLogEnabled()){
			
			WebContext webContext = new WebContext(request,response);
			
			IAccessDeniedLogService accessDeniedLogService = (IAccessDeniedLogService)ServiceBeanRegistry
				.get(webContext, IAccessDeniedLogService.class, AccessDeniedLogService.class);
			
			LOGGER.debug("Saving not allowed access to log");
			accessDeniedLogService.logRequest(ipAddress, requestURI, userAgent);
			
		}
		
		LOGGER.debug("Redirecting to accessDeniedPage: {}",accessDeniedPage);
		response.sendRedirect(accessDeniedPage);
		
	}
}




