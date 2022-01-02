package com.test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.test.model.TransferRequest;
import com.test.model.TransferResult;
import com.test.repository.AccountsRepository;
import com.test.service.AccountsService;
import com.test.service.AccountsServiceImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class ConcurrentTransactionTest {
	@InjectMocks
	AccountsService accountService = new AccountsServiceImpl();

	@Autowired
	private AccountsRepository accountsRepository;
	
	@Test
	public void testConcurrent() {
		ExecutorService service = Executors.newFixedThreadPool(10);
		Runnable runnableTask = () -> {
			try {
				ReflectionTestUtils.setField(accountService, "accountsRepository", accountsRepository);
				TransferRequest req = new TransferRequest();
				req.setAccountFrom(Long.valueOf(10000236121L));
				req.setAccountTo(Long.valueOf(10000235422L));
				req.setTransferAmt(new BigDecimal(5000));
				TransferResult result = accountService.transferAmount(req);
				Assertions.assertThat(result).hasFieldOrPropertyWithValue("accountFromBalance", new BigDecimal(45000))
						.hasFieldOrPropertyWithValue("accountToBalance", new BigDecimal(28000));
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		
		int numberOfThreads = 10;
		for (int i = 0; i < numberOfThreads; i++) {
			service.execute(runnableTask);
		}
		service.shutdownNow();
	}

}
