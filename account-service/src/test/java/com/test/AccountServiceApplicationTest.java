package com.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AccountServiceApplicationTest {

	@Test
	public void testMain(){
		AccountServiceApplication.main(new String[]{"test"});
	}

}
