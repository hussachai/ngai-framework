package com.siberhus.ngai.guardian.tags;

import javax.servlet.jsp.JspTagException;

public class HasPermissionTag extends PermissionTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean showTagBody(String uri, String event)
			throws JspTagException {
		return isPermitted(uri, event);
	}
	
}