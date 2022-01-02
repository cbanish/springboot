package com.test.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.test.model.Account;

public interface AccountsRepository extends JpaRepository<Account, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public Account save(Account entity);
}
