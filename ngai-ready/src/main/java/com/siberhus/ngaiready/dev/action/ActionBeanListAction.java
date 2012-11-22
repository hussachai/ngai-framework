package com.siberhus.ngaiready.dev.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.action.AbstractSimpleAction;
import com.siberhus.ngai.core.Ngai;

@UrlBinding("/action/dev/actionBeanList")
public class ActionBeanListAction extends AbstractSimpleAction {

	
	@DefaultHandler
	public Resolution index(){
		getContext().getRequest().setAttribute(
				"actionBeanPathMap", Ngai.getInstance()
				.getActionBeanPathMap());
		return new ForwardResolution("/pages/dev/actionbean-list.jsp");
	}
	
}
