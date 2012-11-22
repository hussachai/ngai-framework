package com.siberhus.ngai.example.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractModel;
import com.siberhus.ngai.model.IUnremovableModel;

@NamedQueries({
	@NamedQuery(name="User.getUserByUsername",query="from User u where u.username=?")
})
@Entity
@Table(name="USERS")
public class User extends AbstractModel<Long> implements IUnremovableModel{
	
	public static final String ENTITY_NAME = "User";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="USERNAME")
	private String username;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="IS_ADMIN")
	private Boolean admin = Boolean.FALSE;
	
	@Column(name="IS_ACTIVE")
	private Boolean active = Boolean.TRUE;
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(name="USER_ROLES"
		,joinColumns=@JoinColumn(name="USER_ID",referencedColumnName="ID")
		,inverseJoinColumns=@JoinColumn(name="ROLE_ID",referencedColumnName="ID"))
	private List<Role> roleList = new ArrayList<Role>();
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	public Boolean getAdmin() {
		return admin;
	}
	
	public void setAdmin(Boolean isAdmin) {
		this.admin = isAdmin;
	}
	
	@Override
	public Boolean getActive() {
		return active;
	}

	@Override
	public void setActive(Boolean isActive) {
		this.active = isActive;
	}
	
	
}
