package com.siberhus.ngai.guardian.exception;


public class AccessDeniedException extends GuardianException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AccessDeniedException(String message) {
		super(message);
	}

	public AccessDeniedException(Throwable cause) {
		super(cause);
	}
	
	
}
