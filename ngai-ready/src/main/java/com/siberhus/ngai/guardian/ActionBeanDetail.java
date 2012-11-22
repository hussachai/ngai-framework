package com.siberhus.ngai.guardian;

import java.io.Serializable;
import java.util.Set;

public class ActionBeanDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String actionBeanURI;
	
	private String actionBeanFQN;
	
	private Set<String> eventNameSet;
	
	public String getActionBeanURI() {
		return actionBeanURI;
	}
	
	public void setActionBeanURI(String actionBeanURI) {
		this.actionBeanURI = actionBeanURI;
	}

	public String getActionBeanFQN() {
		return actionBeanFQN;
	}

	public void setActionBeanFQCN(String actionBeanFQN) {
		this.actionBeanFQN = actionBeanFQN;
	}

	public Set<String> getEventNameSet() {
		return eventNameSet;
	}

	public void setEventNameSet(Set<String> eventNameSet) {
		this.eventNameSet = eventNameSet;
	}
	
}
