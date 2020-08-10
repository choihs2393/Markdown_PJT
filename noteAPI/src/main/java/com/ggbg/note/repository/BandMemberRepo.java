package com.ggbg.note.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import com.ggbg.note.domain.dto.BandMemberDTO;
import com.ggbg.note.domain.dto.PrimitiveAccountDTO;

@Repository
public class BandMemberRepo{
	
	@PersistenceContext EntityManager em;
	
	public List<BandMemberDTO> getBandMemberDTOList(int bandNo){
		String str = "select a.account_no, a.account_email, a.account_name, ab.account_status"
				+ " from account a inner join account_band ab"
				+ " on ab.band_no ="+ bandNo + ""
				+ " where a.account_no = ab.account_no";	
		
		JpaResultMapper jpaResultMapper = new JpaResultMapper();
		Query query = em.createNativeQuery(str);
		List<BandMemberDTO> list = jpaResultMapper.list(query, BandMemberDTO.class);
		return list;
	}
	
	public List<PrimitiveAccountDTO> getAPrimitiveAccountDTOList(String email){
		String str = "select account_no, account_email, account_name"
				+ " from account"
				+ " where account_email like '%" + email +"%'";	
		
		JpaResultMapper jpaResultMapper = new JpaResultMapper();
		Query query = em.createNativeQuery(str);
		List<PrimitiveAccountDTO> list = jpaResultMapper.list(query, PrimitiveAccountDTO.class);
		return list;
	}
	
}
