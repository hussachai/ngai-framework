package com.siberhus.ngaiready.admin.action;

import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.apache.commons.lang.StringUtils;

import com.siberhus.commons.validator.regex.EmailValidator;
import com.siberhus.ngai.QTO;
import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.guardian.model.User;
import com.siberhus.ngai.guardian.service.IUserService;
import com.siberhus.ngai.guardian.service.impl.UserService;
import com.siberhus.ngai.service.ICrudService;
import com.siberhus.ngaiready.action.BaseCrudAction;

@UrlBinding("/action/admin/user")
public class UserAction extends BaseCrudAction<User, Long>{

	@InjectService(implementation=UserService.class)
	private IUserService userService;
	
	@ValidateNestedProperties({
		@Validate(on="save,create",field="username",required=true,minlength=3),
		@Validate(on="save,create",field="password",required=true,minlength=6,maxlength=32,mask="[a-zA-Z0-9_]*"),
		@Validate(on="save",field="firstName",required=true),
		@Validate(on="save",field="lastName",required=true),
		@Validate(on="save",field="email",mask=EmailValidator.EMAIL_PATTERN_STR),
	})
	private User model;
	
	private String password;
	
	private boolean wipeUser = false;
	
	@Override
	public User getModel(){
		return model;
	}
	
	@Override
	public void setModel(User model){
		this.model = model;
	}
	
	@Override
	public Class<User> getModelClass(){
		return User.class;
	}
	
	@Override
	public ICrudService<User, Long> getCrudService() {
		
		return userService;
	}
	
	@Override
	public String getPathPrefix() {
		return "/pages/admin/user";
	}
	
	@Override
	public QTO createQueryTransferObjectForSearch() {
		QTO qto = new QTO("from User u where 1=1 ");
		if(getModel()==null){
			return qto;
		}
		if(getModel().getUsername()!=null){
			qto.appendQuery("and u.username like ?");
			qto.addParameter(getModel().getUsername());
		}
		if(getModel().getFirstName()!=null){
			qto.appendQuery("and u.firstName like ?");
			qto.addParameter(getModel().getFirstName());
		}
		if(getModel().getLastName()!=null){
			qto.appendQuery("and u.lastName like ?");
			qto.addParameter(getModel().getLastName());
		}
		if(getModel().getEmail()!=null){
			qto.appendQuery("and u.email like ?");
			qto.addParameter(getModel().getEmail());
		}
		if(getModel().getContactNumber()!=null){
			qto.appendQuery("and u.contactNumber like ?");
			qto.addParameter(getModel().getContactNumber());
		}
		if(getModel().getAlias()!=null){
			qto.appendQuery("and u.alias like ?");
			qto.addParameter(getModel().getAlias());
		}
//		if(getModel().getActive()!=null){
//			qto.appendQuery("and u.active = ?");
//			qto.addParameter(getModel().getActive());
//		}
		return qto;
	}
	
	@DontBind
	public Resolution checkUser(){
		
		return forward(getPathPrefix()+"/check-user.jsp");
	}
	
	@DontValidate
	public Resolution ajaxCheckUser(){
		if(getModel()==null || StringUtils.isBlank(getModel().getUsername())){
			return new StreamingResolution("text/html","error:Username is required field");
		}
		boolean available = userService.isUsernameAvailable(getModel().getUsername());
		if(available){
			return new StreamingResolution("text/html","OK");
		}
		return new StreamingResolution("text/html","error:Username has already been taken");
	}
	
	public Resolution create(){
		
		Resolution page = forward(getPathPrefix()+"check-user.jsp");
		if(getModel()!=null){
			if(!userService.isUsernameAvailable(getModel().getUsername())){
				getContext().getValidationErrors().add("username",new SimpleError("Username has already been taken"));
				return page;
			}
			if(!StringUtils.equals(getPassword(),getModel().getPassword())){
				getContext().getValidationErrors().add("password2",new SimpleError("Password does not match the confirm password"));
				return page;
			}
		}else{
			getContext().getValidationErrors().addGlobalError(new SimpleError("Form is blank"));
			return page;
		}
		
		return forward(getPathPrefix()+"/edit.jsp");
	}
	
	
	@DontValidate
	@Override
	public Resolution delete(){
		
		userService.delete(getModel());
		getContext().getRequest().setAttribute(MODEL_NAME, getModel());
		
		return forward(getPathPrefix()+"/confirm-delete.jsp");
	}
	
	@DontValidate
	public Resolution confirmDeletion(){
		
		if(isWipeUser()){
			userService.wipe(getModel());
		}else{
			userService.delete(getModel());
		}
		
		return index();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isWipeUser() {
		return wipeUser;
	}

	public void setWipeUser(boolean wipeUser) {
		this.wipeUser = wipeUser;
	}
	
}
