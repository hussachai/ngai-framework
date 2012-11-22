package com.siberhus.ngai.guardian.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

import com.siberhus.ngai.model.AbstractModel;

@MappedSuperclass
public abstract class AbstractPermission extends AbstractModel<Long> {
	
	public static final String ALL_EVENTS = "ALL";
	
	public static final String EVENT_SEPARATOR = "|";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ACTION_URI", nullable = false)
	private String actionUri;
	
	@Column(name = "EVENT_NAMES")
	private String eventNames;
	
	public String getActionUri() {
		return actionUri;
	}

	public void setActionUri(String actionUri) {
		this.actionUri = actionUri;
	}
	
	public String[] getEventNames() {
		return StringUtils.split(this.eventNames, EVENT_SEPARATOR);
	}

	public void setEventNames(String... eventName) {
		this.eventNames = StringUtils.join(eventName, EVENT_SEPARATOR);
	}
	
	public boolean isAllowAllEvents(){
		
		return ALL_EVENTS.equals(eventNames);
	}
}
