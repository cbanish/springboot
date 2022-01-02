package com.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.test.controller.AccountsController;
import com.test.exception.AccountNotFoundException;
import com.test.model.Account;
import com.test.model.TransferRequest;
import com.test.model.TransferResult;
import com.test.repository.AccountsRepository;
import com.test.service.AccountsService;
import com.test.service.AccountsServiceImpl;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(AccountsControllerTest.class);
	
	@InjectMocks
	AccountsController accountsController;
	@MockBean
	private AccountsService acctService;
	
	@MockBean
	private AccountsRepository accountsRepository;
	
	@BeforeAll
	public void init(){
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		ReflectionTestUtils.setField(accountsController, "accountService", acctService);
	}
	
	@Test
	public void testGetAccountDetails() throws Exception {

		Account acct = getUserAccount();
		Long acctNo = Long.valueOf(10000236121L);
		Mockito.when(acctService.getAccountDetails(Mockito.any(Long.class))).thenReturn(acct);
		ResponseEntity<Account> responseEntity = accountsController.getAccountDetails(acctNo);

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(202);
		assertThat(responseEntity.getBody().getName()).isEqualTo("Gupta");
	}
	
	@Test
	public void testGetAccountDetailsException() throws Exception {

		Long acctNo = Long.valueOf(10000236121L);
		Mockito.when(acctService.getAccountDetails(Mockito.any(Long.class))).thenThrow(AccountNotFoundException.class);
		Assertions.assertThrows(AccountNotFoundException.class, () -> {
			accountsController.getAccountDetails(acctNo);
	  });

	}
	
	@Test
	public void tesTransferMoney() throws Exception {
		TransferResult result=getTransferResult();
		Mockito.when(acctService.transferAmount(Mockito.any(TransferRequest.class))).thenReturn(result);
		ResponseEntity<TransferResult> responseEntity = accountsController.transferMoney(new TransferRequest());

		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(202);
		assertThat(responseEntity.getBody().getAccountFromBalance()).isEqualTo(new BigDecimal(45000));
	}
	
	@Test
	public void tesTransferMoneyException() throws Exception {
		TransferRequest req1=new TransferRequest();
		Mockito.when(acctService.transferAmount(req1)).thenThrow(AccountNotFoundException.class);
		Assertions.assertThrows(AccountNotFoundException.class, () -> {
			accountsController.transferMoney(req1);
	  });
	}

	private Account getUserAccount() {
		Account acct=new Account();
		acct.setAccountNo(Long.valueOf(10000236121L));
		acct.setName("Gupta");
		acct.setBranch("Mumbai North");
		acct.setAccountType("Savings");
		acct.setAddress("122, North Mumbai");
		acct.setBalance(new BigDecimal(50000));
		return acct;
	}
	
	private TransferResult getTransferResult() {
		TransferResult tr= new TransferResult();
		tr.setAccountFrom(Long.valueOf(10000236121L));
		tr.setAccountFromBalance(new BigDecimal(45000));
		tr.setAccountTo(Long.valueOf(10000235422L));
		tr.setAccountToBalance(new BigDecimal(28000));
		return tr;
	}
	
}
