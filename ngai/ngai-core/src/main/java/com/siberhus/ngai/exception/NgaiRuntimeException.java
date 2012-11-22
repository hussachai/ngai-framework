package com.siberhus.ngai.exception;

public class NgaiRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Throwable specificCause;
	
	private boolean localizableMessage;
	
	public NgaiRuntimeException() {
		super();
	}
	
	public NgaiRuntimeException(String message, Throwable specificCause) {
		super(message, specificCause);
		this.specificCause = specificCause;
	}
	
	public NgaiRuntimeException(String message) {
		super(message);
	}

	public NgaiRuntimeException(Throwable specificCause) {
		super(specificCause);
		this.specificCause = specificCause;
	}
	
	public Throwable getSpecificCause() {
		return specificCause;
	}

	public boolean isLocalizableMessage() {
		return localizableMessage;
	}
	
	public NgaiRuntimeException setLocalizableMessage(boolean localizableMessage) {
		this.localizableMessage = localizableMessage;
		return this;
	}
	
}
