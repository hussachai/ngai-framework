package com.siberhus.ngai.example;

import com.siberhus.ngai.action.AbstractAction;

public abstract class BaseExampleAction extends AbstractAction{
	
	private String title;
	
	private String detail;
	
	public BaseExampleAction(String title){
		this.title = title;
	}
	
	public String getPathPrefix(){
		return "/";
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
	
}
