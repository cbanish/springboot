package com.test.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Data;


@Entity
@Table(name="ACCOUNT")
@Data
public class Account {

	@Id
	@Column(name = "ACCOUNT_NO")
	private Long accountNo;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "BRANCH")
	private String branch;
	
	@Column(name = "ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name = "ADDRESS")
	private String address;
	
	@Column(name = "BALANCE")
	@Min(value = 0, message = "Account balance should not be negative")
	private BigDecimal balance;

}
