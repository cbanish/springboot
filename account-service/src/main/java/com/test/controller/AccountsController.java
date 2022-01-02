package com.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.exception.AccountNotFoundException;
import com.test.exception.BusinessException;
import com.test.exception.InsufficientBalanceException;
import com.test.model.Account;
import com.test.model.TransferRequest;
import com.test.model.TransferResult;
import com.test.service.AccountsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/accounts")
@Api(tags = { "Accounts Controller"}, description = "Provide APIs for account operations")
public class AccountsController {
	
	private static final Logger log = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsService accountService;

	@GetMapping("/{accountNo}/details")
	@ApiOperation(value = "Get account details by accountNo", response = Account.class, produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code=200,message = "ACCEPTED"),@ApiResponse(code = 404, message = "Account validation failed")})
	public ResponseEntity<Account> getAccountDetails(
			@ApiParam(name =  "accountNo",type = "long", required = true) @PathVariable Long accountNo) throws Exception {
		return new ResponseEntity<>(accountService.getAccountDetails(accountNo), HttpStatus.ACCEPTED);
	}
	
	@PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API for transfering money between accounts", response = TransferResult.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code=200,message = "ACCEPTED" ),@ApiResponse(code = 404, message = "Account validation failed")})
	public ResponseEntity<TransferResult> transferMoney(@RequestBody TransferRequest request) throws Exception {

		try {
			TransferResult result = accountService.transferAmount(request);			
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (AccountNotFoundException | InsufficientBalanceException e) {
			log.error("Fail to transfer balances due to validation failure");
			throw e;
		} catch (BusinessException cbEx) {
			log.error("Fail to transfer balances due to system failure");
			throw cbEx;
		}
	}
}
