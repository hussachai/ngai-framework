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
 * Action + Concrete DAO
 * @author hussachai
 *
 */
@UrlBinding("/action/g1e1")
public class G1E1Action extends BaseExampleAction {

	@InjectDao
	private G1E1Dao g1e1Dao;
	
	@Validate(required=true)
	private String username;
	
	private String password;
	
	public G1E1Action() {
		super("Group1 - Example1");
		setDetail("Action + Concrete DAO");
	}
	
	@Override
	public String getIndexPage() {
		return "/pages/reveal-password.jsp";
	}

	public Resolution revealPassword(){
		try{
			User user = g1e1Dao.getUserByUsername(username);
			password = user.getPassword();
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
