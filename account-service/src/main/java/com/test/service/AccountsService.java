package com.test.service;

import com.test.exception.AccountNotFoundException;
import com.test.exception.BusinessException;
import com.test.exception.InsufficientBalanceException;
import com.test.model.Account;
import com.test.model.TransferRequest;
import com.test.model.TransferResult;

public interface AccountsService {

	public Account getAccountDetails(Long accountNo);

	public TransferResult transferAmount(TransferRequest transfer)
			throws InsufficientBalanceException, AccountNotFoundException, BusinessException;
}
