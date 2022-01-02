package com.test;

import java.math.BigDecimal;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.test.constant.AccountConstant;
import com.test.exception.AccountNotFoundException;
import com.test.exception.InsufficientBalanceException;
import com.test.model.Account;
import com.test.model.TransferRequest;
import com.test.model.TransferResult;
import com.test.repository.AccountsRepository;
import com.test.service.AccountsService;
import com.test.service.AccountsServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountsServiceTest {

	@InjectMocks
	AccountsService accountService = new AccountsServiceImpl();

	@Mock
	private AccountsRepository repo;

	@BeforeAll
	public void init() {
		ReflectionTestUtils.setField(accountService, "accountsRepository", repo);
	}

	@Test
	public void testGetAccountDetails1() throws Exception {
		Mockito.when(repo.findById(Long.valueOf(10000236121L))).thenReturn(getUserAccount());
		Account acct = accountService.getAccountDetails(Long.valueOf(10000236121L));
		Assertions.assertThat(acct).hasFieldOrPropertyWithValue("name", "Gupta")
				.hasFieldOrPropertyWithValue("accountType", "Savings");
	}

	@Test
	public void testGetAccountDetails2ExceptionThrown() throws Exception {
		Mockito.when(repo.findById(Mockito.any(Long.class)))
				.thenThrow(new AccountNotFoundException("Account not found", AccountConstant.CLIENT_ERROR1));

		Assertions.assertThatExceptionOfType(AccountNotFoundException.class)
				.isThrownBy(() -> accountService.getAccountDetails(Long.valueOf(1000L)));
	}

	@Test
	public void testTransferAmountAccountFromException() {
		TransferRequest req = new TransferRequest();
		req.setAccountFrom(Long.valueOf(1000L));
		req.setAccountTo(Long.valueOf(10000235422L));
		req.setTransferAmt(new BigDecimal(5000));
		Mockito.when(repo.findById(Long.valueOf(1000L))).thenThrow(AccountNotFoundException.class);
		Assertions.assertThatExceptionOfType(AccountNotFoundException.class)
				.isThrownBy(() -> accountService.transferAmount(req));
	}

	@Test
	public void testTransferAmountAccountToException() {
		TransferRequest req = new TransferRequest();
		req.setAccountFrom(Long.valueOf(10000236121L));
		req.setAccountTo(Long.valueOf(2000L));
		req.setTransferAmt(new BigDecimal(5000));
		Mockito.when(repo.findById(Long.valueOf(10000236121L))).thenReturn(getUserAccount());
		Mockito.when(repo.findById(Long.valueOf(2000L))).thenThrow(AccountNotFoundException.class);
		Assertions.assertThatExceptionOfType(AccountNotFoundException.class)
				.isThrownBy(() -> accountService.transferAmount(req));
	}

	@Test
	public void testTransferAmountInsufficientBalanceException() {
		TransferRequest req = new TransferRequest();
		req.setAccountFrom(Long.valueOf(10000236121L));
		req.setAccountTo(Long.valueOf(10000235422L));
		req.setTransferAmt(new BigDecimal(350000));
		Mockito.when(repo.findById(Long.valueOf(10000236121L))).thenReturn(getUserAccount());
		Mockito.when(repo.findById(Long.valueOf(10000235422L))).thenReturn(getUserAccount2());
		Assertions.assertThatExceptionOfType(InsufficientBalanceException.class)
				.isThrownBy(() -> accountService.transferAmount(req));
	}

	@Test
	public void testTransferAmount() {
		TransferRequest req = new TransferRequest();
		req.setAccountFrom(Long.valueOf(10000236121L));
		req.setAccountTo(Long.valueOf(10000235422L));
		req.setTransferAmt(new BigDecimal(5000));
		Mockito.when(repo.findById(Long.valueOf(10000236121L))).thenReturn(getUserAccount());
		Mockito.when(repo.findById(Long.valueOf(10000235422L))).thenReturn(getUserAccount2());
		TransferResult result = accountService.transferAmount(req);
		Assertions.assertThat(result).hasFieldOrPropertyWithValue("accountFromBalance", new BigDecimal(45000))
				.hasFieldOrPropertyWithValue("accountToBalance", new BigDecimal(28000));
	}

	private Optional<Account> getUserAccount() {
		Account acct = new Account();
		acct.setAccountNo(Long.valueOf(10000236121L));
		acct.setName("Gupta");
		acct.setBranch("Mumbai North");
		acct.setAccountType("Savings");
		acct.setAddress("122, North Mumbai");
		acct.setBalance(new BigDecimal(50000));
		return Optional.of(acct);
	}

	private Optional<Account> getUserAccount2() {
		Account acct = new Account();
		acct.setAccountNo(Long.valueOf(10000235422L));
		acct.setName("Paul");
		acct.setBranch("Chennai");
		acct.setAccountType("Savings");
		acct.setAddress("122, Chennai");
		acct.setBalance(new BigDecimal(23000));
		return Optional.of(acct);
	}

}
