package com.siberhus.ngai.guardian.exception;

import com.siberhus.ngai.exception.NgaiRuntimeException;

public class GuardianException extends NgaiRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GuardianException() {
		super();
	}

	public GuardianException(String message, Throwable specificCause) {
		super(message, specificCause);
	}

	public GuardianException(String message) {
		super(message);
	}

	public GuardianException(Throwable specificCause) {
		super(specificCause);
	}
	
	
}
