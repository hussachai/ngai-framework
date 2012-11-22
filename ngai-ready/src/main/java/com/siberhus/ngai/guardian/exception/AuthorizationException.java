package com.siberhus.ngai.guardian.exception;


public class AuthorizationException extends GuardianException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}
	
	
}
