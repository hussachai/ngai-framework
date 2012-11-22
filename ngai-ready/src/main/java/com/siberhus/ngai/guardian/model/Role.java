package com.siberhus.ngai.guardian.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractAuditableModel;;

@Entity
@Table(name="ROLES")
public class Role extends AbstractAuditableModel<Long, User> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="ROLE_NAME", nullable=false, unique=true)
	private String roleName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@ManyToMany(mappedBy="roleSet",fetch=FetchType.LAZY)
	private Set<User> userSet = new HashSet<User>();
	
	@Override
	public String[] toNameKeys(){
		return new String[]{
			"roleName", "description"
		};
	}
	
	@Override
	public String[] toNames(){
		return new String[]{
			"Role Name", "Description"
		};
	}
	
	@Override
	public Object[] toValues(){
		return new Object[]{roleName, description};
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString(){
		return roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<User> getUserSet() {
		return userSet;
	}
	
	public void setUserSet(Set<User> userSet) {
		this.userSet = userSet;
	}
	
	
}
