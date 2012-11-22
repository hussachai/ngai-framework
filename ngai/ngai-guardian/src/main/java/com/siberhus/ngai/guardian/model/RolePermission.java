package com.siberhus.ngai.guardian.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name="RolePermission.findUniqueByRoleIdList"
			,query="select distinct rp from RolePermission rp where rp.roleId in (?1)"),
	@NamedQuery(name="RolePermission.findUniqueByRoleIdListAndActionUri"
			,query="select distinct rp from RolePermission rp where rp.roleId in (?1) and rp.actionUri=?2"),
	@NamedQuery(name="RolePermission.deleteByRoleId"
			,query="delete from RolePermission rp where rp.roleId=?1")
})

@Entity
@Table(name="ROLE_PERMISSIONS")
public class RolePermission extends AbstractPermission {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="ROLE_ID", nullable=false)
	private Long roleId;
	
	public Long getRoleId() {
		return roleId;
	}
	
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
}
