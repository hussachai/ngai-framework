package com.siberhus.ngai.exception;

public class NgaiBeanException extends NgaiRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NgaiBeanException() {
		super();
	}
	
	public NgaiBeanException(String message, Throwable specificCause) {
		super(message, specificCause);
	}

	public NgaiBeanException(String message) {
		super(message);
	}

	public NgaiBeanException(Throwable specificCause) {
		super(specificCause);
	}
	
	
	
}
