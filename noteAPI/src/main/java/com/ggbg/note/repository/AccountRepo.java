package com.ggbg.note.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ggbg.note.bean.Account;


public interface AccountRepo  extends JpaRepository<Account, String> {
	Optional<Account> findAccountByEmail(String email);
    Optional<Account> findAccountByEmailAndPassword(String email, String password);
}
