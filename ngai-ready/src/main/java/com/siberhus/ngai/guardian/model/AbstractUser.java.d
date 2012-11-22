package com.siberhus.ngai.guardian.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

import com.siberhus.ngai.guardian.config.GuardianConfig;
import com.siberhus.ngai.guardian.exception.GuardianException;
import com.siberhus.ngai.model.AbstractAuditable;

@MappedSuperclass
public abstract class AbstractUser<USER extends AbstractUser<?,?>,ROLE extends AbstractRole<USER>> extends AbstractAuditable<USER,Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="USERNAME", nullable=false, unique=true)
	private String username;
	
	@Column(name="PASSWORD", nullable=false)
	private String password;
	
//	@ManyToMany(mappedBy="userSet",fetch=FetchType.LAZY)
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USER_ROLES",
			joinColumns=@JoinColumn(name="USER_ID",referencedColumnName="ID"),
			inverseJoinColumns=@JoinColumn(name="ROLE_ID",referencedColumnName="ID"))
	private Set<ROLE> roleSet = new HashSet<ROLE>();
	
	@Column(name="AUDIT_TRAILS_ENABLED")
	private Boolean auditTrailEnabled = Boolean.FALSE;
	
	@PrePersist
	protected void checkRootUsernameChange(){
		if(GuardianConfig.get().isRootAccessEnabled()){
			if(!GuardianConfig.get().getRootUsername().equals(getUsername())){
				throw new GuardianException("Root user cannot change username."
						+" To change root's username you must change "+GuardianConfig.ROOT_USERNAME+" property"+
						" and alter root record in database directly.");
			}
		}
	}
	
	@PreRemove
	protected void checkRootUserDeletion(){
		if(GuardianConfig.get().isRootAccessEnabled()){
			if(GuardianConfig.get().getRootUsername().equals(getUsername())){
				throw new GuardianException("Cannot remove root user because "+
					GuardianConfig.ROOT_ACCESS_ENABLED+" is 'true'");
			}
		}
	}
	
	@Override
	public String toString(){
		return username;
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

	public Set<ROLE> getRoleSet() {
		return roleSet;
	}
	
	@SuppressWarnings("unchecked")
	public void setRoleCollection(Collection<ROLE> roleCollect) {
		if(roleCollect!=null){
			for(Object role : this.roleSet.toArray()){
				if(!roleCollect.contains(role)){
					((ROLE)role).getUserSet().remove((USER)this);
					this.roleSet.remove(role);
				}
			}
			for(ROLE role : roleCollect){
				role.getUserSet().add((USER)this);
				this.roleSet.add(role);
			}
		}else{
			removeAllRoles();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addRole(ROLE role){
		role.getUserSet().add((USER)this);
		this.roleSet.add(role);
	}
	
	@SuppressWarnings("unchecked")
	public void removeRole(ROLE role){
		role.getUserSet().remove((USER)this);
		this.roleSet.remove(role);
	}
	
	@SuppressWarnings("unchecked")
	public void removeAllRoles(){
		for(Object role : this.roleSet.toArray()){
			((ROLE)role).getUserSet().remove((USER)this);
			this.roleSet.remove(role);
		}
	}
	
	public Boolean getAuditTrailEnabled() {
		return auditTrailEnabled;
	}
	
	public void setAuditTrailEnabled(Boolean auditTrailEnabled) {
		this.auditTrailEnabled = auditTrailEnabled;
	}
	
}
