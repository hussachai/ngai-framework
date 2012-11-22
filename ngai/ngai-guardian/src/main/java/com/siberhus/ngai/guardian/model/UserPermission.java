package com.siberhus.ngai.guardian.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name="UserPermission.findByUserId",
			query="from UserPermission up where up.userId=?"),
	@NamedQuery(name="UserPermission.findByUserIdAndActionUri",
			query="from UserPermission up where up.userId=? and up.actionUri=?"),
	@NamedQuery(name="UserPermission.deleteByUserId",
			query="delete from UserPermission up where up.userId=?")
})
@Entity
@Table(name="USER_PERMISSIONS")
public class UserPermission extends AbstractPermission {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="USER_ID", nullable=false)
	private Long userId;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
