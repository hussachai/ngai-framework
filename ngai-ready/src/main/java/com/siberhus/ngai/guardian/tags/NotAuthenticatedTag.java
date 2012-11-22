package com.siberhus.ngai.guardian.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP tag that renders the tag body only if the current user has <em>not</em>
 * executed a successful authentication attempt
 * <em>during their current session</em>.
 * 
 * <p>
 * The logically opposite tag of this one is the
 * {@link org.apache.shiro.web.tags.AuthenticatedTag}.
 */
public class NotAuthenticatedTag extends SecureTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {

		if (getSessionUser() == null) {
			return TagSupport.EVAL_BODY_INCLUDE;
		}
		return TagSupport.SKIP_BODY;
	}

}