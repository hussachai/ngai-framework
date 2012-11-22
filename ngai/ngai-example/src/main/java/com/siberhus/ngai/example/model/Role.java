package com.siberhus.ngai.example.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractModel;
import com.siberhus.ngai.model.IUnremovableModel;

@Entity
@Table(name="ROLES")
public class Role extends AbstractModel<Long> implements IUnremovableModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="DESCRIPTION")
	private String description;

	@ManyToMany(mappedBy="roleList")
	private List<User> userList = new ArrayList<User>();
	
	private Boolean active = Boolean.TRUE;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
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
