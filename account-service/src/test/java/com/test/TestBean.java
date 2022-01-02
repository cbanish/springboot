package com.test;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import com.test.model.Account;
import com.test.model.ErrorResponse;
import com.test.model.TransferRequest;
import com.test.model.TransferResult;

public class TestBean {

	@Test
	public void testModels() {
		BeanTester beanTester = new BeanTester();
		beanTester.testBean(Account.class);
		beanTester.testBean(ErrorResponse.class);
		beanTester.testBean(TransferRequest.class);
		beanTester.testBean(TransferResult.class);
	}
	
	@Test
	public void testEqualsHashcode() {
		Account obj1=new Account(Long.valueOf(10000236121L),"Gupta", "Mumbai North","Savings","122, North Mumbai",new BigDecimal(50000));
		Account obj2=new Account(Long.valueOf(10000236121L),"Gupta", "Mumbai North","Savings","122, North Mumbai",new BigDecimal(50000));
		Assertions.assertTrue(obj1.equals(obj2));  
		Assertions.assertNotNull(obj1.hashCode()); 
	}

}
