package com.ggbg.note.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ggbg.note.domain.entity.AccountBandEntity;

public interface AccountBandRepo extends JpaRepository<AccountBandEntity, String> {
	@Query(value = "SELECT * FROM account_band where account_no = :accountNo and account_status = 2", nativeQuery = true)
	List<AccountBandEntity> findAllAccountBandByAccountNo(int accountNo);
	
	@Query(value = "SELECT * FROM account_band where account_no = :accountNo", nativeQuery = true)
	List<AccountBandEntity> findAllAccountBandByAccountNoWithoutStatus(int accountNo);
	
	@Query(value = "SELECT count(*) FROM account_band where account_no = :accountNo and band_no = :bandNo", nativeQuery = true)
	int findAccountBandByAccountNoAndBandNo(int accountNo, int bandNo);

	@Query(value = "SELECT count(*) FROM account_band where account_no = :accountNo and band_no = :bandNo and account_status != 2", nativeQuery = true)
	int findAccountBandToVerify(int accountNo, int bandNo);
	
	@Transactional
	@Modifying
	@Query(value = "delete from account_band where account_no = :account_no and band_no = :band_no", nativeQuery = true)
	int deleteAccountBandByNo(int account_no, int band_no);
	
	@Transactional
	@Modifying
	@Query(value = "update account_band set account_status = 1 where account_no = :account_no and band_no = :band_no", nativeQuery = true)
	int updateAccountBandByNo(int account_no, int band_no);
}
