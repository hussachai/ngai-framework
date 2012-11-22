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
	@NamedQuery(name="LoginFailureLog.findByIpAddress"
		,query="from LoginFailureLog lfl where lfl.ipAddress=?")
})

@Entity
@Table(name="LOGIN_FAILURE_LOGS")
public class LoginFailureLog extends AbstractModel<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoginFailureLog(){}
	
	public LoginFailureLog(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	@Column(name="IP_ADDRESS", unique=true)
	private String ipAddress;
	
	@Column(name="ATTEMPT_COUNT")
	private Integer attemptCount = new Integer(0);
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_ATTEMPT_AT")
	private Date lastAttemptAt;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}

	public Date getLastAttemptAt() {
		return lastAttemptAt;
	}

	public void setLastAttemptAt(Date lastAttemptAt) {
		this.lastAttemptAt = lastAttemptAt;
	}

	
	
}
