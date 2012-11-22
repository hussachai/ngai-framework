package com.siberhus.ngaiready.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.UrlBinding;

import com.siberhus.ngai.guardian.LoginContext;
import com.siberhus.ngai.guardian.SessionUser;
import com.siberhus.ngai.guardian.action.AbstractAuthenticationAction;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.localization.SessionLocalePicker;
import com.siberhus.ngai.util.DefaultDataFormat;

@UrlBinding("/action/authen/{$event}")
public class AuthenticationAction extends AbstractAuthenticationAction{

	@Override
	public void loginSucceed(LoginContext loginContext, SessionUser sessionUser) {
		HttpSession session = getContext().getRequest().getSession();
		Map<String, String> loginInfo = new HashMap<String, String>();
		User user = sessionUser.getRealUserObject();
		loginInfo.put("firstName", user.getFirstName());
		loginInfo.put("lastName", user.getLastName());
		loginInfo.put("email", user.getEmail());
		loginInfo.put("username", sessionUser.getUsername());
		loginInfo.put("roles", sessionUser.getRealUserObject().getRoleSet().toString());
		loginInfo.put("ipAddress", sessionUser.getIpAddress());
		loginInfo.put("preferredLocale", session.getAttribute(
				SessionLocalePicker.LOCALE_SESSION_ATTR).toString());
		loginInfo.put("systemLocale", loginContext.getLocale().toString());
		loginInfo.put("loginAt", DefaultDataFormat.formatDate(
				loginContext.getLoginAt()));
		session.setAttribute("loginInfo", loginInfo);
	}
	
	
}
