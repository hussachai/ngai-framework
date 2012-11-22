package com.siberhus.ngai.guardian.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP tag that renders the tag body only if the current user has executed a
 * <b>successful</b> authentication attempt
 * <em>during their current session</em>.
 * 
 * <p>
 * This is more restrictive than the {@link UserTag}, which only ensures the
 * current user is known to the system, either via a current login or from
 * Remember Me services, which only makes the assumption that the current user
 * is who they say they are, and does not guarantee it like this tag does.
 * 
 * <p>
 * The logically opposite tag of this one is the {@link NotAuthenticatedTag}
 */
public class AuthenticatedTag extends SecureTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {

		if (getSessionUser() != null) {
			return TagSupport.EVAL_BODY_INCLUDE;
		}
		return TagSupport.SKIP_BODY;
	}
	
}