package com.ggbg.note.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ggbg.note.domain.entity.AccountBandEntity;

public interface AccountBandRepo extends JpaRepository<AccountBandEntity, String> {
	@Query(value = "SELECT * FROM account_band where account_no = :accountNo and account_status = 2", nativeQuery = true)
	List<AccountBandEntity> findAllAccountBandByAccountNo(int accountNo);
	
	@Query(value = "SELECT * FROM account_band where account_no = :accountNo", nativeQuery = true)
	List<AccountBandEntity> findAllAccountBandByAccountNoWithoutStatus(int accountNo);
}
