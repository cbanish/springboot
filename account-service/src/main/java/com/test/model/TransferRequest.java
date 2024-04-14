package com.test.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferRequest {
	
	@NotNull
	private Long accountFrom;

	@NotNull
	private Long accountTo;

	@NotNull
	private BigDecimal transferAmt;

}
