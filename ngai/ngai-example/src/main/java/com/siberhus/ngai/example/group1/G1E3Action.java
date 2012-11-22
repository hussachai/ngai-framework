package com.siberhus.ngai.example.group1;

import javax.persistence.NoResultException;

import net.sourceforge.stripes.action.DontBind;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.example.BaseExampleAction;
import com.siberhus.ngai.example.model.User;


/**
 * Action + QueryAware DAO Interface
 * 
 * @author hussachai
 *
 */
@UrlBinding("/action/g1e3")
public class G1E3Action extends BaseExampleAction {
	
	@InjectDao
	private G1E3DaoIf g1e3Dao;
	
	@Validate(required=true)
	private String username;
	
	private String password;
	
	private Number number;
	
	public G1E3Action() {
		super("Group1 - Example3");
		setDetail("Action + QueryAware DAO Interface");
	}
	
	@Override
	public String getIndexPage() {
		return "/pages/reveal-password2.jsp";
	}

	public Resolution revealPassword(){
		try{
			User user = g1e3Dao.getUserByUsername(username);
			password = user.getPassword();
		}catch(Exception e){
			if(e.getCause() instanceof NoResultException){
				getContext().getValidationErrors().add("username"
						, new SimpleError("Username was not found"));
			}else{
				getContext().getValidationErrors().add("username"
						, new SimpleError(e.getMessage()));
			}
		}
		return index();
	}
	
	@DontBind
	@DontValidate
	public Resolution countNormalUser(){
		number = g1e3Dao.countAllNormalUser();
		return index();
	}
	
	@DontBind
	@DontValidate
	public Resolution countAdminUser(){
		number = g1e3Dao.countAllAdminUser();
		return index();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Number getNumber() {
		return number;
	}

	public void setNumber(Number number) {
		this.number = number;
	}
	
	
}
