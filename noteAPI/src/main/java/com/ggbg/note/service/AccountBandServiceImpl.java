package com.ggbg.note.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggbg.note.domain.dto.BandMemberDTO;
import com.ggbg.note.domain.dto.PrimitiveAccountDTO;
import com.ggbg.note.domain.entity.AccountBandEntity;
import com.ggbg.note.domain.entity.AccountEntity;
import com.ggbg.note.exception.InternalServerException;
import com.ggbg.note.exception.UnAuthorizationException;
import com.ggbg.note.repository.AccountBandRepo;
import com.ggbg.note.repository.AccountRepo;
import com.ggbg.note.repository.BandMemberRepo;
import com.ggbg.note.repository.BandRepo;
import com.ggbg.note.util.MapperUtil;

@Service
public class AccountBandServiceImpl implements IAccountBandService{
	@Autowired
	private MapperUtil mapperUtil;
	
	@Autowired
	private BandMemberRepo bandMemberRepo;
	
	@Autowired
	private BandRepo bandRepo;
	
	@Autowired
	AccountBandRepo accountBandRepo;
	
	@Autowired
	private AccountRepo accountRepo;

	@Override
	public List<BandMemberDTO> getBandMember(int bandNo) {
		List<BandMemberDTO> list = bandMemberRepo.getBandMemberDTOList(bandNo);
		return list;
	}
	
	@Override
	public BandMemberDTO inviteBandMember(String email, int bandNo, int accountNo) {
		
		Integer masterNo = -1;
		masterNo = bandRepo.findMasterNoByBandNo(bandNo);
		if(masterNo == null)
			throw new InternalServerException("InviteBandMember exception");
		if(masterNo != accountNo)
			throw new UnAuthorizationException(email + " : is not master");
		
		if(accountBandRepo.findAccountBandByAccountNoAndBandNo(accountNo, bandNo) != 0) {
			return null;
		}
		
		Optional<AccountEntity> optional = accountRepo.findAccountByEmail(email);
		
		BandMemberDTO bandMemberDTO = new BandMemberDTO();
		
		
		if(optional.isPresent()) {
			AccountEntity accountEntity = optional.get();
			bandMemberDTO.setNo(accountEntity.getNo());
			bandMemberDTO.setName(accountEntity.getName());
			bandMemberDTO.setEmail(accountEntity.getEmail());
			bandMemberDTO.setStatus(2);
		}
		
		accountBandRepo.save(AccountBandEntity.builder()
				.bandNo(bandNo)
				.accountNo(accountNo)
				.status(2)
				.build());
		
		return bandMemberDTO;
	}
	
	@Override
	public List<PrimitiveAccountDTO> findAccountList(String email) {
		List<PrimitiveAccountDTO> list = new ArrayList<PrimitiveAccountDTO>();
		list = bandMemberRepo.getAPrimitiveAccountDTOList(email);
		return list;
	}
}
