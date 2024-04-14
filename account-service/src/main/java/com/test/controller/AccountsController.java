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


@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private static final Logger log = LoggerFactory.getLogger(AccountsController.class);

    @Autowired
    private AccountsService accountService;

    @GetMapping("/{accountNo}/details")
    public ResponseEntity<Account> getAccountDetails(
            @PathVariable Long accountNo) throws Exception {
        return new ResponseEntity<>(accountService.getAccountDetails(accountNo), HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
