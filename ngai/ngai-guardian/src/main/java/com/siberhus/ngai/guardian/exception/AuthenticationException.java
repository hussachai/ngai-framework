package com.siberhus.ngai.guardian.exception;


public class AuthenticationException extends GuardianException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}
	
	
}
