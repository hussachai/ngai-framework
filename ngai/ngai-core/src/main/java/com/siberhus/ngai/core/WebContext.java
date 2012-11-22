package com.siberhus.ngai.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebContext {
	
	public static final WebContext DUMMY_WEB_CONTEXT = new WebContext();
	
	public WebContext(){}
	
	public WebContext(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	
}
