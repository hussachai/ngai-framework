package com.siberhus.ngai.example.group1;

import javax.persistence.NoResultException;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

import com.siberhus.ngai.annot.InjectDao;
import com.siberhus.ngai.example.BaseExampleAction;
import com.siberhus.ngai.example.model.User;


/**
 * Action + (DAO Interface + DAO Implementation)
 * 
 * @author hussachai
 *
 */
@UrlBinding("/action/g1e2")
public class G1E2Action extends BaseExampleAction {
	
	@InjectDao(implementation=G1E2DaoImpl.class)
	private G1E2DaoIf g1e2Dao;
	
	@Validate(required=true)
	private String username;
	
	private String password;
	
	public G1E2Action() {
		super("Group1 - Example2");
		setDetail("Action + (DAO Interface + DAO Implementation)");
	}
	
	@Override
	public String getIndexPage() {
		return "/pages/reveal-password.jsp";
	}

	public Resolution revealPassword(){
		try{
			User user = g1e2Dao.getUserByUsername(username);
			password = user.getPassword();
		}catch(Exception e){
			if(e.getCause() instanceof NoResultException){
				getContext().getValidationErrors().add("username"
						, new SimpleError("Username was not found"));
			}else{
				getContext().getValidationErrors().add("username"
						, new SimpleError(e.toString()));
			}
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
