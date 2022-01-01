package com.test.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransferRequest {
	
	@NotNull
	@ApiModelProperty(required = true)
	private Long accountFrom;

	@NotNull
	@ApiModelProperty(required = true)
	private Long accountTo;

	@NotNull
	@ApiModelProperty(required = true)
	private BigDecimal transferAmt;

}
