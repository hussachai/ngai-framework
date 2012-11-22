package com.siberhus.ngaiready.dev.action;

import java.util.Enumeration;

import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action/dev/sessionManager")
public class SessionAttributeManagerAction extends BaseWebAttributeManagerAction{
	
	@Override
	public String getTitle() {
		return "HttpSession";
	}
	
	@Override
	public Enumeration<?> getAttributeNames() {
		return getContext().getRequest().getSession().getAttributeNames();
	}

	@Override
	public Object getAttributeValue(String name) {
		return getContext().getRequest().getSession().getAttribute(name);
	}
	
	
	
}
