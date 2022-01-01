package com.test.exception;

public class AccountNotFoundException extends ValidationException {

	public AccountNotFoundException(String message, String errorCode) {
		super(message, errorCode);
	}

}
