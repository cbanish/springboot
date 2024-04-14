package com.test.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferResult {
	@NotNull
	private Long accountFrom;
	
	private BigDecimal accountFromBalance;
	@NotNull
	private Long accountTo;
	
	private BigDecimal accountToBalance;
}
