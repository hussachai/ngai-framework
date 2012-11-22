/* Copyright 2009 Hussachai Puripunpinyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siberhus.ngai.action;

import java.io.StringReader;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The abstract action bean that class that provides some useful method.
 * @author hussachai
 * @since 0.9
 * 
 */
public abstract class AbstractAction implements ActionBean,ValidationErrorHandler {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private ActionBeanContext context;
	
	protected final String CLASS_NAME = getClass().getName();
	
	public abstract String getIndexPage();
	
	public abstract String getPathPrefix();
	
	public String getErrorPage(){
		return null;
	}
	
	@Override
	public ActionBeanContext getContext() {
		return context;
	}
	
	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}
	
	@Override
	public Resolution handleValidationErrors(ValidationErrors errors)
			throws Exception {
		StringBuilder message = new StringBuilder();
		message.append("<ul>");
		for (List<ValidationError> fieldErrors : errors.values()) {
			for (ValidationError error : fieldErrors) {
				message.append("<li>");
				message.append(error.getMessage(getContext().getLocale()));
				message.append("</li>");
			}
		}
		message.append("</ul>");
		
		if(getContext().getEventName().startsWith("ajax")){
			return new StreamingResolution("text/html", new StringReader("error:"
					+ message.toString()));
		}else if(getErrorPage()!=null){
			return forward(getPathPrefix()+getErrorPage());
		}
		return getContext().getSourcePageResolution();
	}
	
	@DefaultHandler
	@DontValidate
	public Resolution index() {
		return forward(getPathPrefix()+getIndexPage());
	}
	
	// =================== PROTECTED ========================== //
	
	protected void setPageAttribute(String name, Object value){
		getContext().getRequest().getSession()
			.setAttribute(toPageAtributeName(name), value);
	}
	
	protected Object getPageAttribute(String name){
		return getContext().getRequest().getSession()
			.getAttribute(toPageAtributeName(name));
	}
	
	protected void removePageAttribute(String name){
		getContext().getRequest().getSession()
			.removeAttribute(toPageAtributeName(name));
	}
	
	protected static ForwardResolution forward(String path) {

		return new ForwardResolution(path);
	}
	
	protected static ForwardResolution forward(Class<? extends ActionBean> beanType) {

		return new ForwardResolution(beanType);
	}
	
	protected static ForwardResolution forward(Class<? extends ActionBean> beanType,
			String event) {
		
		return new ForwardResolution(beanType, event);
	}

	protected static RedirectResolution redirect(String path) {
		// Defaults to prepending the context path to the url supplied before
		// redirecting.
		return new RedirectResolution(path, true);
	}
	
	protected static RedirectResolution redirect(Class<? extends ActionBean> beanType) {
		
		return new RedirectResolution(beanType);
	}
	
	protected static RedirectResolution redirect(Class<? extends ActionBean> beanType,
			String event) {
		
		return new RedirectResolution(beanType, event);
	}
	
	// =================== PRIVATE ========================== //
	private String toPageAtributeName(String name){
		return CLASS_NAME+"$"+name;
	}
	
}
