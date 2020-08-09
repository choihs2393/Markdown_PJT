package com.ggbg.note.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ggbg.note.bean.Account;
import com.ggbg.note.bean.Band;

public interface BandRepo extends JpaRepository<Band, Long> {
	Optional<Band> findBandByNo(int band_no);
	
	@Query(value = "SELECT * "
			+ "FROM band "
			+ "where band_no in (select band_no from account_band where account_no = ?1 and account_status = 2) ", nativeQuery = true)
	List<Band> findAllBandStatusByAccountNo(int no);
	
	@Query(value = "SELECT * "
			+ "FROM band "
			+ "where band_no in (select band_no from account_band where account_no = ?1 and account_status != 2)", nativeQuery = true)
	List<Band> findAllBandByAccountNo(int no);
	
	@Transactional
	@Modifying
	@Query(value = "delete from band where band_no = :band_no", nativeQuery = true)
	int deleteBandByNo(int band_no);
	
	@Transactional
	@Modifying
	@Query(value = "update band set band_name = :band_name where band_no = :band_no", nativeQuery = true)
	int updateBandByNo(String band_name, int band_no);
}
