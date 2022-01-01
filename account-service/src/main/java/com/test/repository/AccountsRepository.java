package com.test.repository;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import com.test.model.Account;

public interface AccountsRepository extends JpaRepository<Account, Long>{
	
	@Lock(LockModeType.PESSIMISTIC_READ)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	public Optional<Account> findById(Long customerNo);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public Account save(Account entity);
}
