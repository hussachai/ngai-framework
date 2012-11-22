package com.siberhus.ngai.guardian.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractModel;

@NamedQueries({
	@NamedQuery(name="AccessDeniedLog.findMatchedRequest",
		query="from AccessDeniedLog adl where adl.ipAddress=?1 and adl.requestURI=?2 and adl.userAgent=?3")
})

@Entity
@Table(name="ACCESS_DENIED_LOGS")
public class AccessDeniedLog extends AbstractModel<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="IP_ADDRESS")
	private String ipAddress;
	
	@Column(name="REQUEST_URI")
	private String requestURI;
	
	@Column(name="USER_AGENT")
	private String userAgent;

	@Column(name="RETRY_COUNT")
	private Integer retryCount = new Integer(0);
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	
	
}
