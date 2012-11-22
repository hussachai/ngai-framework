package com.siberhus.ngai.example.group2;

import javax.persistence.NoResultException;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.example.BaseExampleAction;


/**
 * Action + Concrete Service
 * @author hussachai
 *
 */
@UrlBinding("/action/g2e1")
public class G2E1Action extends BaseExampleAction {
	
	@InjectService
	private G2E1Service g2e1Service;
	
	@Validate(required=true)
	private String username;
	
	private String password;
	
	public G2E1Action() {
		super("Group2 - Example1");
		setDetail("Action + Concrete Service");
	}
	
	@Override
	public String getIndexPage() {
		return "/pages/reveal-password.jsp";
	}
	
	public Resolution revealPassword(){
		try{
			password = g2e1Service.revealPassword(username);
		}catch(NoResultException e){
			getContext().getValidationErrors().add("username"
				, new SimpleError("Username was not found"));
		}
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
	
	
}
