package com.test.service;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.constant.AccountConstant;
import com.test.exception.AccountNotFoundException;
import com.test.exception.BusinessException;
import com.test.exception.InsufficientBalanceException;
import com.test.model.Account;
import com.test.model.TransferRequest;
import com.test.model.TransferResult;
import com.test.repository.AccountsRepository;

@Service
public class AccountsServiceImpl implements AccountsService {

	private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);
	ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Autowired
	private AccountsRepository accountsRepository;

	@Transactional
	@Override
	public TransferResult transferAmount(TransferRequest transfer)
			throws InsufficientBalanceException, AccountNotFoundException, BusinessException {
		TransferResult result = new TransferResult();
		log.info("transferAmount method called");
		Lock writeLock = lock.writeLock();

		Account accountFrom = accountsRepository.findById(transfer.getAccountFrom())
				.orElseThrow(() -> new AccountNotFoundException(
						"Account number: " + transfer.getAccountFrom() + " does not exist.",
						AccountConstant.CLIENT_ERROR1));

		Account accountTo = accountsRepository.findById(transfer.getAccountTo())
				.orElseThrow(() -> new AccountNotFoundException(
						"Account number: " + transfer.getAccountTo() + " does not exist.",
						AccountConstant.CLIENT_ERROR1));

		if (accountFrom.getBalance().compareTo(transfer.getTransferAmt()) < 0) {
			throw new InsufficientBalanceException(
					"Account number: " + transfer.getAccountFrom() + " doesnt have sufficient balance to transfer.",
					AccountConstant.CLIENT_ERROR2);
		}
		accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getTransferAmt()));
		accountTo.setBalance(accountTo.getBalance().add(transfer.getTransferAmt()));
		try {
			writeLock.lock();
			accountsRepository.save(accountFrom);
			accountsRepository.save(accountTo);
			result.setAccountFrom(accountFrom.getAccountNo());
			result.setAccountTo(accountTo.getAccountNo());
			result.setAccountFromBalance(accountFrom.getBalance());
			result.setAccountToBalance(accountTo.getBalance());
		} catch (Exception e) {
			throw new BusinessException("Encountered error during transaction.", AccountConstant.SYSTEM_ERROR);
		} finally {
			writeLock.unlock();
		}
		return result;
	}

	@Override
	public Account getAccountDetails(Long accountNo) throws AccountNotFoundException {
		Optional<Account> account = null;
		Lock readLock = lock.readLock();
		readLock.lock();
		try {
			account = accountsRepository.findById(accountNo);
			if (!account.isPresent()) {
				throw new AccountNotFoundException("Account Number " + accountNo + " does not exist.",
						AccountConstant.CLIENT_ERROR1);
			}
		} finally {
			readLock.unlock();
		}
		return account.get();
	}
}
