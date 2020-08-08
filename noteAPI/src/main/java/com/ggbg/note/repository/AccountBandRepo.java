package com.ggbg.note.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ggbg.note.bean.AccountBand;

public interface AccountBandRepo extends JpaRepository<AccountBand, String> {
	@Query(value = "SELECT * FROM account_band where account_no = :accountNo and account_status = 2", nativeQuery = true)
	List<AccountBand> findAllAccountBandByAccountNo(int accountNo);
	
	@Query(value = "SELECT * FROM account_band where account_no = :accountNo", nativeQuery = true)
	List<AccountBand> findAllAccountBandByAccountNoWithoutStatus(int accountNo);
}
