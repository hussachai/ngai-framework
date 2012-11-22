package com.siberhus.ngai.guardian.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;

import com.siberhus.ngai.model.AbstractAuditable;

@MappedSuperclass
public abstract class AbstractRole<USER extends AbstractUser<?,?>> 
	extends AbstractAuditable<USER,Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="ROLE_NAME", nullable=false, unique=true)
	private String roleName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
//	@ManyToMany(fetch=FetchType.LAZY)
//	@JoinTable(name="USER_ROLES",
//			joinColumns=@JoinColumn(name="ROLE_ID",referencedColumnName="ID"),
//			inverseJoinColumns=@JoinColumn(name="USER_ID",referencedColumnName="ID"))
	@ManyToMany(mappedBy="roleSet",fetch=FetchType.LAZY)
	private Set<USER> userSet = new HashSet<USER>();
	
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
	
	public Set<USER> getUserSet() {
		return userSet;
	}

	public void setUserSet(Set<USER> userSet) {
		this.userSet = userSet;
	}
	
}
