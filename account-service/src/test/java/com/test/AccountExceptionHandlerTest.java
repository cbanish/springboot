package com.test;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.constant.AccountConstant;
import com.test.exception.AccountExceptionHandler;
import com.test.exception.BusinessException;
import com.test.exception.ValidationException;
@ExtendWith(MockitoExtension.class)
public class AccountExceptionHandlerTest {
	@InjectMocks
	AccountExceptionHandler exceptionHandler;
	@Mock
	HttpServletRequest req;
	
	@Test
	public void testHandleBusinessException(){
		exceptionHandler.handleBusinessException(req, new BusinessException("test", AccountConstant.SYSTEM_ERROR));
	}
	
	@Test
	public void testHandleValidationException(){
		exceptionHandler.handleValidationException(req, new ValidationException("test", AccountConstant.CLIENT_ERROR1));
	}
	
	@Test
	public void testHandleGenericException(){
		exceptionHandler.handleGenericException(req, new ValidationException("test", AccountConstant.SYSTEM_ERROR));
	}
}
