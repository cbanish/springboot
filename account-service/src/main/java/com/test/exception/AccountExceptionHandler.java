package com.test.exception;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.model.ErrorResponse;

@ControllerAdvice
public class AccountExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(AccountExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity handleGenericException(HttpServletRequest req, Exception e){
		log.error("Exception error message: " + e.getMessage());
        
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        Map<String,Object> error = Collections.singletonMap("error", errorMsg);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
	
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
    public ResponseEntity<ErrorResponse> handleBusinessException(HttpServletRequest req, Exception e){
		BusinessException busExp = (BusinessException) e;
        String errorMsg = (busExp.getMessage() == null) ? busExp.getClass().getSimpleName() : e.getMessage();
        log.error("Exception error code: " + busExp.getErrorCode() + ":" + busExp.getMessage());
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(busExp.getErrorCode());
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
	
	@ExceptionHandler(value = { ValidationException.class,AccountNotFoundException.class, InsufficientBalanceException.class})
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleValidationException(HttpServletRequest req, Exception e){
		ValidationException valExp = (ValidationException) e;
        String errorMsg = (valExp.getMessage() == null) ? valExp.getClass().getSimpleName() : e.getMessage();
        log.error("Exception error code: " + valExp.getErrorCode() + ":" + valExp.getMessage());
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(valExp.getErrorCode());
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
