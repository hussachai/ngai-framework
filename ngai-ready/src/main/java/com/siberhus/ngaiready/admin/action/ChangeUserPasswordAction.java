package com.siberhus.ngaiready.admin.action;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import com.siberhus.ngai.action.AbstractAction;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.action.PasswordValidatorUtil;
import com.siberhus.ngai.guardian.service.IUserService;
import com.siberhus.ngai.guardian.service.impl.UserService;

@UrlBinding("/action/admin/changeUserPassword")
public class ChangeUserPasswordAction extends AbstractAction {
	
	@InjectService(implementation=UserService.class)
	private IUserService userService;
	
	@Validate(required=true)
	private String username;
	
	@Validate(required=true)
	private String oldPassword;
	
	@Validate(required=true)
	private String newPassword;
	
	@Validate(expression="newPassword eq confirmPassword")
	private String confirmPassword;
	
	private boolean success;
	
	@Override
	public String getPathPrefix(){
		return "/pages/admin/user";
	}
	
	@Override
	public String getIndexPage() {
		return "/change-password.jsp";
	}
	
	@ValidationMethod(on="changePassword")
	public void validatePassword(){
		PasswordValidatorUtil.validate(this, 
				"newPassword", newPassword);
	}
	
	public Resolution changePassword(){
		userService.changePassword(getUsername()
		, getOldPassword(), getNewPassword());
		success = true;
		return index();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
