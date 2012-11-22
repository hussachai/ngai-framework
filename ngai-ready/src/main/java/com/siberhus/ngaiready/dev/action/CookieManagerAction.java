package com.siberhus.ngaiready.dev.action;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.action.AbstractAction;

@UrlBinding("/action/dev/cookieManager")
public class CookieManagerAction extends AbstractAction {
	
	@Override
	public String getPathPrefix() {
		return "/pages/dev";
	}
	
	@Override
	public String getIndexPage() {
		return "/cookie-manager.jsp";
	}
	
	
}
