package com.ggbg.note.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ggbg.note.bean.Account;


public interface AccountRepo  extends JpaRepository<Account, String> {
	Optional<Account> findAccountByEmail(String email);
    Optional<Account> findAccountByEmailAndPassword(String email, String password);
    
    @Query(value = "SELECT account_name FROM account where account_email = :email", nativeQuery = true) 
    String findNameByEmail(@Param("email") String email);
}
