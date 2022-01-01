package com.test.exception;

public class InsufficientBalanceException extends ValidationException {

	public InsufficientBalanceException(String message, String errorCode) {
		super(message, errorCode);
	}

}
