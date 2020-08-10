package com.ggbg.note.service;

import java.util.List;

import com.ggbg.note.domain.dto.BandMemberDTO;
import com.ggbg.note.domain.dto.PrimitiveAccountDTO;

public interface IAccountBandService {
	public List<BandMemberDTO> getBandMember(int bandNo);
	
	public BandMemberDTO inviteBandMember(String email, int masterNo, int bandNo);
	
	public List<PrimitiveAccountDTO> findAccountList(String email);
	
	public boolean deleteMember(int masterNo, int accountNo, int bandNo);
	
	public boolean acceptInvite(int accountNo, int bandNo);
	public boolean declineInvite(int accountNo, int bandNo);
}
