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
	@NamedQuery(name="LoginLog.findLastLogByUserId",
			query="from LoginLog ll where ll.userId=? and ll.logoutAt is null order by ll.loginAt desc")	
})

@Entity
@Table(name="LOGIN_LOGS")
public class LoginLog extends AbstractModel<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="USERNAME")
	private String username;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOGIN_AT")
	private Date loginAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOGOUT_AT")
	private Date logoutAt;
	
	@Column(name="IP_ADDRESS")
	private String ipAddress;
	
	@Column(name="IS_SECURE_LOGIN")
	private boolean secureLogin; //HTTPS or not
	
	@Column(name="LOCALE")
	private String locale;
	
	/**
	 * Headers[User-Agent]
	 */
	@Column(name="USER_AGENT")
	private String userAgent;
	
	/**
	 * Headers[Accept-Charset]
	 */
	@Column(name="ACCEPT_CHARSET")
	private String acceptCharset;
	
	/**
	 * Headers[Accept-Encoding]
	 */
	@Column(name="ACCEPT_ENCODING")
	private String acceptEncoding;
	
	/**
	 * Headers[Accept-Language]
	 */
	@Column(name="ACCEPT_LANGUAGE")
	private String acceptLanguage;

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

	public Date getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}

	public Date getLogoutAt() {
		return logoutAt;
	}

	public void setLogoutAt(Date logoutAt) {
		this.logoutAt = logoutAt;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isSecureLogin() {
		return secureLogin;
	}

	public void setSecureLogin(boolean secureLogin) {
		this.secureLogin = secureLogin;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptCharset() {
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}
	
	
	
}
