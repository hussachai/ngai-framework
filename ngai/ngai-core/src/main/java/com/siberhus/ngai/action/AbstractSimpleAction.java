package com.siberhus.ngai.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

public abstract class AbstractSimpleAction implements ActionBean {

	private ActionBeanContext context;
	
	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

}
