package com.siberhus.ngaiready.action;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.siberhus.commons.properties.PropertiesUtil;
import com.siberhus.ngai.exception.NgaiRuntimeException;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action")
public class ForwardAction implements ActionBean {
	
	private ActionBeanContext context;
	
	private static Properties pageMapProps;
	
	static{
		try {
			pageMapProps = PropertiesUtil.create("classpath:forward.properties");
		} catch (IOException e) {
			throw new NgaiRuntimeException(e);
		}
	}
	
	@Override
	public ActionBeanContext getContext() {
		return context;
	}
	
	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
	
	@DefaultHandler
	public Resolution forward(){
		System.out.println(getContext().getRequest().getQueryString());
		System.out.println(getContext().getRequest().getServletPath());
		System.out.println(getContext().getRequest().getRequestURI());
		String requestUri = getContext().getRequest().getRequestURI();
		String requestPath = StringUtils.substringAfter(requestUri, getContext().getRequest().getContextPath());
		return new ForwardResolution(pageMapProps.getProperty(requestPath));
		
	}
}
