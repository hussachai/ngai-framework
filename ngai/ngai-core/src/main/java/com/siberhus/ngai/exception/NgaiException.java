package com.siberhus.ngai.exception;


public class NgaiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Throwable specificCause;
	
	private boolean localizableMessage;
	
	public NgaiException() {
		super();
	}

	public NgaiException(String message, Throwable specificCause) {
		super(message, specificCause);
		this.specificCause = specificCause;
	}

	public NgaiException(String message) {
		super(message);
	}

	public NgaiException(Throwable specificCause) {
		super(specificCause);
		this.specificCause = specificCause;
	}

	public Throwable getSpecificCause() {
		return specificCause;
	}
	
	public boolean isLocalizableMessage() {
		return localizableMessage;
	}
	
	public NgaiException setLocalizableMessage(boolean localizableMessage) {
		this.localizableMessage = localizableMessage;
		return this;
	}
	
}
