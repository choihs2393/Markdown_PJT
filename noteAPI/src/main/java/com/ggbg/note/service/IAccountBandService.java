package com.ggbg.note.service;

import java.util.List;

import com.ggbg.note.domain.dto.BandMemberDTO;
import com.ggbg.note.domain.dto.PrimitiveAccountDTO;

public interface IAccountBandService {
	public List<BandMemberDTO> getBandMember(int bandNo);
	
	public BandMemberDTO inviteBandMember(String email, int bandNo, int accountNo);
	
	public List<PrimitiveAccountDTO> findAccountList(String email);
}
