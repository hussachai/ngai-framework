package com.siberhus.ngai.guardian.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.siberhus.ngai.exception.ServiceException;
import com.siberhus.ngai.guardian.exception.GuardianException;
import com.siberhus.ngai.guardian.model.AbstractPermission;

public abstract class PermissionTag extends SecureTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uri;

	private String event;

	@Override
	public int doStartTag() throws JspException {

		boolean show = showTagBody(uri, event);
		if (show) {
			return TagSupport.EVAL_BODY_INCLUDE;
		} else {
			return TagSupport.SKIP_BODY;
		}
	}

	@Override
	public void release() {
		super.release();
		uri = null;
		event = null;
	}

	protected boolean isPermitted(String uri, String event)
			throws JspTagException {
		try {
			if (event == null) {
				event = AbstractPermission.ALL_EVENTS;
			}
			getAuthorizationService().checkPermission(uri, event,
					getSessionUser());
		} catch (ServiceException e) {
			Throwable t = e.getCause();
			if (t instanceof GuardianException) {
				return false;
			}
			throw new JspTagException(e.getCause());
		}
		return true;
	}

	protected abstract boolean showTagBody(String uri, String event)
			throws JspTagException;
	
	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
