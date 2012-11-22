package com.siberhus.ngai.example.group2;

import javax.persistence.NoResultException;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import com.siberhus.ngai.annot.InjectService;
import com.siberhus.ngai.example.BaseExampleAction;


/**
 * Action + (Service Interface + Service Implementation)
 * 
 * @author hussachai
 *
 */
@UrlBinding("/action/g2e2")
public class G2E2Action extends BaseExampleAction {
	
	@InjectService(implementation=G2E2ServiceImpl.class)
	private G2E2ServiceIf g2e2Service;
	
	@Validate(required=true)
	private String username;
	
	private String password;
	
	public G2E2Action() {
		super("Group2 - Example2");
		setDetail("Action + (Service Interface + Service Implementation)");
	}
	
	@Override
	public String getIndexPage() {
		return "/pages/reveal-password.jsp";
	}

	public Resolution revealPassword(){
		try{
			password = g2e2Service.revealPassword(username);
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
