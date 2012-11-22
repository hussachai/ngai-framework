package com.siberhus.ngai.localization.tags;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;

import com.siberhus.ngai.core.ServiceBeanRegistry;
import com.siberhus.ngai.localization.SessionLocalePicker;
import com.siberhus.ngai.localization.model.ResourceType;
import com.siberhus.ngai.localization.service.IResourceBundleService;
import com.siberhus.ngai.localization.service.impl.ResourceBundleService;
import com.siberhus.web.taglib.Scope;

public abstract class LocalizableTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IResourceBundleService resourceBundleService;
	
	private String key;
	
	private String var;
	
	private Scope scope = Scope.REQUEST;

	private String title;
	
	private boolean escape = true;
	
	public abstract ResourceType getType();

	@Override
	public int doStartTag() throws JspException {
		if (getKey() == null) {
			throw new JspTagException("key is required field");
		}
		
		resourceBundleService = (IResourceBundleService) ServiceBeanRegistry
				.get(IResourceBundleService.class, ResourceBundleService.class);
		String preferredLang = getPreferredLanguage(pageContext);
		ResourceBundle resBundle = null;
		if (getType() == ResourceType.FIELD) {
			resBundle = resourceBundleService.getFieldBundle(preferredLang);
		} else {
			resBundle = resourceBundleService.getMessageBundle(preferredLang);
		}
		if (resBundle.containsKey(getKey())) {
			String value = addTitleIfExist(resBundle.getString(getKey()));
			if(isEscape()){
				value = StringEscapeUtils.escapeHtml(value);
			}
			if(getVar()!=null){
				setScopedValue(value);
			}else{
				try {
					pageContext.getOut().append(value);
				} catch (IOException e) {
					throw new JspTagException(e);
				}
			}
			return SKIP_BODY;
		} else {
			return EVAL_BODY_INCLUDE;
		}
	}
	
	
	@Override
	public void release() {
		super.release();
		resourceBundleService = null;
		key = null;
		var = null;
		scope = Scope.REQUEST;
		title = null;
		escape = true;
	}

	protected void setScopedValue(String value) {
		switch (getScope()){
		case REQUEST:
			pageContext.getRequest().setAttribute(getVar(), value);
			break;
		case SESSION:
			pageContext.getSession().setAttribute(getVar(), value);
			break;
		case PAGE:
			pageContext.setAttribute(getVar(), value);
			break;
		default:
			pageContext.getServletContext().setAttribute(getVar(), value);
		}
	}

	protected String addTitleIfExist(String value) {
		if (getTitle() != null) {
			return "<span title='" + title + "'>" + value + "</span>";
		} else {
			return value;
		}
	}

	public void setScope(String scope) {
		this.scope = Scope.valueOf(scope.toUpperCase());
	}

	public static Locale getPreferredLocale(PageContext pageContext) {

		Locale preferredLocale = (Locale) pageContext.getSession()
				.getAttribute(SessionLocalePicker.LOCALE_SESSION_ATTR);
		if (preferredLocale == null) {
			preferredLocale = pageContext.getRequest().getLocale();
		}
		return preferredLocale;
	}

	public static String getPreferredLanguage(PageContext pageContext) {

		return getPreferredLocale(pageContext).getLanguage();
	}
	
	//============== Tag Attributes ================//
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEscape() {
		return escape;
	}

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

}
