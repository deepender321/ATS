package com.leverage.ApplicationServices.Exception;

public class Exception {
	
	public static class EmailAlreadyExistsException extends RuntimeException {
	    public EmailAlreadyExistsException(String message) {
	        super(message);
	    }
	}

	public static class MobileNumberAlreadyExistsException extends RuntimeException {
	    public MobileNumberAlreadyExistsException(String message) {
	        super(message);
	    }
	}

}
