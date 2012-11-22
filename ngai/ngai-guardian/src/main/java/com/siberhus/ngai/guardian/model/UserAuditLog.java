package com.siberhus.ngai.guardian.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.siberhus.ngai.model.AbstractModel;

@NamedQueries({
	@NamedQuery(name="UserAuditLog.deleteByUserId",
		query="delete from UserAuditLog ual where ual.userId=?")
})

@Entity
@Table(name="USER_AUDIT_LOGS")
public class UserAuditLog extends AbstractModel<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="USERNAME")
	private String username;
	
	@Column(name="ACTION_URI")
	private String actionUri;
	
	@Column(name="EVENT_NAME")
	private String eventName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EXECUTED_AT")
	private Date executedAt;
	
	@Column(name="IS_FORBIDDEN_ACTION")
	private Boolean forbiddenAction;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getActionUri() {
		return actionUri;
	}

	public void setActionUri(String actionUri) {
		this.actionUri = actionUri;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getExecutedAt() {
		return executedAt;
	}

	public void setExecutedAt(Date executedAt) {
		this.executedAt = executedAt;
	}
	
	public Boolean getForbiddenAction() {
		return forbiddenAction;
	}
	
	public void setForbiddenAction(Boolean forbiddenAction) {
		this.forbiddenAction = forbiddenAction;
	}
	
	
}
