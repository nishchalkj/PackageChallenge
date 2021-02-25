package com.mobiquity.packer.exception;

/**
 * This is the custom exception class for the API
 * 
 * @author Nishchal
 *
 */
public class APIException extends Exception {

	private static final long serialVersionUID = 772674880029444245L;

	public APIException(String message, Exception e) {
		super(message, e);
	}

	public APIException(String message) {
		super(message);
	}
}
