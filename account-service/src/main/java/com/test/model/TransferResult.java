package com.test.model;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class TransferResult {
	
	private Long accountFrom;
	
	private BigDecimal accountFromBalance;

	private Long accountTo;
	
	private BigDecimal accountToBalance;
}
