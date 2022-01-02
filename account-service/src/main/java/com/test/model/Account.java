package com.test.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
public class Account {

	@Id
	@Column(name = "ACCOUNT_NO")
	@NotNull
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
	

	public Account(@NotNull Long accountNo, String name, String branch, String accountType, String address,
			@Min(value = 0, message = "Account balance should not be negative") BigDecimal balance) {
		super();
		this.accountNo = accountNo;
		this.name = name;
		this.branch = branch;
		this.accountType = accountType;
		this.address = address;
		this.balance = balance;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNo == null) ? 0 : accountNo.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountNo == null) {
			if (other.accountNo != null)
				return false;
		} else if (!accountNo.equals(other.accountNo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
