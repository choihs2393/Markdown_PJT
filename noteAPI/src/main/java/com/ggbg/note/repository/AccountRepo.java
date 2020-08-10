package com.ggbg.note.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ggbg.note.domain.entity.AccountEntity;


public interface AccountRepo extends JpaRepository<AccountEntity, String> {
	Optional<AccountEntity> findAccountByEmail(String email);
	
	@Query(value = "SELECT account_email, account_name FROM account where account_email = :email", nativeQuery = true) 
    Map<String, Object> findInfoByEmail(@Param("email") String email);
	
    @Query(value = "SELECT account_no, account_name FROM account where account_email = :email", nativeQuery = true) 
    Map<String, Object> findByEmail(@Param("email") String email);
    
}
