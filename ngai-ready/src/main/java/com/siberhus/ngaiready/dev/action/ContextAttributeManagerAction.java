package com.siberhus.ngaiready.dev.action;

import java.util.Enumeration;

import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/action/dev/contextManager")
public class ContextAttributeManagerAction extends BaseWebAttributeManagerAction{
	
	@Override
	public String getTitle() {
		return "Servlet Context";
	}
	
	@Override
	public Enumeration<?> getAttributeNames() {
		return getContext().getServletContext().getAttributeNames();
	}

	@Override
	public Object getAttributeValue(String name) {
		return getContext().getServletContext().getAttribute(name);
	}

	
	
}
