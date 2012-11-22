package com.siberhus.ngai.example.login;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.guardian.LoginContext;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.action.AbstractAuthenticationAction;

@UrlBinding("/action/login")
public class LoginAction extends AbstractAuthenticationAction {

	private String title;
	private String detail;
	
//	@Override
//	public String getMainPage() {
//		return "somewhere";
//	}
	
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

	@Override
	public void loginFail(LoginContext loginContext, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginSucceed(LoginContext loginContext, SessionUser sessionUser) {
		// TODO Auto-generated method stub
		
	}
}
