package com.bence.mate.spring.exception;

public class ItemNotFoundException extends RuntimeException {

	public ItemNotFoundException() {
		super("Item not found");
	}

	public ItemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ItemNotFoundException(String message) {
		super(message);
	}

	public ItemNotFoundException(Throwable cause) {
		super(cause);
	}
}
